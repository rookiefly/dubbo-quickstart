package com.rookiefly.quickstart.dubbo.filter;

import com.rookiefly.quickstart.dubbo.constants.LogKey;
import com.rookiefly.quickstart.dubbo.utils.KeyValues;
import com.rookiefly.quickstart.dubbo.remote.api.RpcBizException;
import com.rookiefly.quickstart.dubbo.constants.RpcCode;
import com.rookiefly.quickstart.dubbo.remote.api.RpcResult;
import com.rookiefly.quickstart.dubbo.utils.JsonWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractLogDubboFilter implements Filter {

    protected final static String EXCEPTION_KEY = "exception";

    public static final String COM_CALLER_SUCC = "_com_dubbo_success";
    public static final String COM_CALLER_FAIL = "_com_dubbo_failure";
    public static final String COM_REQUEST_IN = "_com_request_in";
    public static final String COM_REQUEST_OUT = "_com_request_out";

    protected final static String REMOTE_APPLICATION_ATTACHMENT = "remoteApplication";

    private final static String newLine = System.getProperty("line.separator");

    private final static Logger providerLogger = LoggerFactory.getLogger("providerLogger");

    private final static Logger consumerLogger = LoggerFactory.getLogger("consumerLogger");

    /**
     * 是否是Provider
     * 使用RpcContext.isProvider()不会特别准，具体看代码实现
     *
     * @return
     */
    abstract protected boolean isProvider();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        RpcContext context = RpcContext.getContext();

        boolean isProvider = isProvider();

        /**
         * 将请求解析成字符串
         */
        String requestWrapper = requestWrapper(invoker, invocation);

        String interfaceSimpleName = invoker.getInterface().getSimpleName();

        /**
         * 上游调用方应用名字（只有当前应用是provider时才会有上游调用方）
         */
        String remoteApplication = null;
        String application = context.getUrl().getParameter(CommonConstants.APPLICATION_KEY);
        String group = context.getUrl().getParameter(CommonConstants.GROUP_KEY);

        if (isProvider) {
            remoteApplication = context.getAttachment(REMOTE_APPLICATION_ATTACHMENT);

            /**
             * 打印入口日志，如果是调度任务接口，那么还需要输出group
             */
            KeyValues keyValues = KeyValues.create(
                    LogKey.REMOTE_APPLICATION, remoteApplication,
                    LogKey.APPLICATION, application,
                    LogKey.SERVICE_NAME, invoker.getInterface().getSimpleName(),
                    LogKey.METHOD_NAME, invocation.getMethodName(),
                    LogKey.PRODUCER_GROUP, group);
            keyValues.addKeyValue(LogKey.REQUEST, requestWrapper);

            providerLogger.info(wrapLogMessage(COM_REQUEST_IN, "", keyValues));
        }

        Result result;
        long startTime = System.currentTimeMillis();
        RpcResult rpcResult = null;
        try {
            result = invoker.invoke(invocation);

            rpcResult = getRpcResult(result);
            /**
             * 如果result中有异常，或者result中的对象的返回码不是200，那么就做异常日志打印处理
             */
            if (result != null && (result.getException() != null || (rpcResult != null && rpcResult.getCode() != RpcCode.SUCCESS))) {
                printErrorOrWarnLog(application, remoteApplication, invoker, invocation, interfaceSimpleName,
                        startTime, group, result.getException(), rpcResult);

            } else {
                String responseWrapper = responseWrapper(invoker, invocation, result);

                KeyValues keyValues = KeyValues.create(
                        LogKey.APPLICATION, application,
                        LogKey.SERVICE_NAME, interfaceSimpleName,
                        LogKey.METHOD_NAME, invocation.getMethodName(),
                        LogKey.PRODUCER_GROUP, group);

                if (isProvider) {
                    keyValues.addKeyValue(LogKey.REMOTE_APPLICATION, remoteApplication)
                            .addKeyValue(LogKey.REQUEST, requestWrapper)
                            .addKeyValue(LogKey.RESPONSE, responseWrapper)
                            .addKeyValue(LogKey.PROC_TIME, System.currentTimeMillis() - startTime);
                    providerLogger.info(wrapLogMessage(COM_REQUEST_OUT, "success", keyValues));

                } else {
                    keyValues.addKeyValue(LogKey.REQUEST, requestWrapper).addKeyValue(LogKey.RESPONSE, responseWrapper)
                            .addKeyValue(LogKey.PROC_TIME, System.currentTimeMillis() - startTime);
                    consumerLogger.info(wrapLogMessage(COM_CALLER_SUCC, "success", keyValues));
                }
            }

        } catch (Throwable e) {
            printErrorOrWarnLog(application, remoteApplication, invoker, invocation, interfaceSimpleName,
                    startTime, group, e, rpcResult);
            throw e;
        } finally {
            //
        }

        return result;
    }

    /**
     * 打印错误或告警日志
     *
     * @param application
     * @param remoteApplication   上游调用者（即消费者）的应用名称，只有当前是provider时才会有该参数
     * @param invoker
     * @param invocation
     * @param interfaceSimpleName
     * @param startTime
     * @param dubboGroup
     * @param throwable           注意，该参数有可能传null
     * @param rpcResult           RpcResult中的非200错误码和描述，注意，该参数有可能传null
     */
    private void printErrorOrWarnLog(String application, String remoteApplication, Invoker<?> invoker,
                                     Invocation invocation, String interfaceSimpleName, long startTime,
                                     String dubboGroup, Throwable throwable, RpcResult rpcResult) {

        final KeyValues keyValues = KeyValues.create(
                LogKey.APPLICATION, application,
                LogKey.SERVICE_NAME, interfaceSimpleName,
                LogKey.METHOD_NAME, invocation.getMethodName(),
                LogKey.PRODUCER_GROUP, dubboGroup);

        boolean isProvider = isProvider();
        String msg = "fail";


        // 是打印error还是打印warn
        boolean needPrintErrorLog = throwable != null && needPrintErrorLog(throwable);

        /**
         * 优先判断RpcResult，如果为null，那么就使用异常码中code和msg
         * 注意：printExceptionLog方法入参 {@link throwable} 和 {@link codeAndMsg} 只会有一个不为空
         */
        Integer errorCode;
        String errorMsg;
        if (rpcResult != null) {
            errorCode = rpcResult.getCode();
            errorMsg = rpcResult.getMsg();

        } else {
            errorCode = getExceptionCode(throwable);
            errorMsg = errorMsgWrapper(throwable);
        }

        if (isProvider) {
            keyValues.addKeyValue(LogKey.REMOTE_APPLICATION, remoteApplication)
                    .addKeyValue(LogKey.REQUEST, requestWrapper(invoker, invocation))
                    .addKeyValue(LogKey.ERROR_NO, errorCode)
                    .addKeyValue(LogKey.ERROR_MSG, errorMsg)
                    .addKeyValue(LogKey.PROC_TIME, System.currentTimeMillis() - startTime);

            if (throwable != null) {
                // 这个是为了把脉的展示
                keyValues.addKeyValue(EXCEPTION_KEY, "");
            }

            if (needPrintErrorLog) {
                providerLogger.error(wrapLogMessage(COM_REQUEST_OUT, msg, keyValues), throwable);

            } else {
                providerLogger.warn(wrapLogMessage(COM_REQUEST_OUT, msg, keyValues), throwable);
            }

        } else {
            keyValues.addKeyValue(LogKey.REQUEST, requestWrapper(invoker, invocation))
                    .addKeyValue(LogKey.ERROR_NO, errorCode)
                    .addKeyValue(LogKey.ERROR_MSG, errorMsg)
                    .addKeyValue(LogKey.PROC_TIME, System.currentTimeMillis() - startTime);

            if (throwable != null) {
                // 这个是为了把脉的展示
                keyValues.addKeyValue(EXCEPTION_KEY, "");
            }

            if (needPrintErrorLog) {
                consumerLogger.error(wrapLogMessage(COM_CALLER_FAIL, msg, keyValues), throwable);

            } else {
                consumerLogger.warn(wrapLogMessage(COM_CALLER_SUCC, msg, keyValues), throwable);
            }
        }
    }

    /**
     * 如果response中包含敏感信息，则子类需要覆盖次方法
     *
     * @param invoker
     * @param invocation
     * @return
     */
    protected String requestWrapper(Invoker<?> invoker, Invocation invocation) {
        return JsonWrapper.toJsonWithoutExceptionAndBigData(invocation.getArguments(), getRequestLogPrintSize());
    }

    /**
     * 请求参数最大日志打印长度
     *
     * @return
     */
    protected int getRequestLogPrintSize() {
        return 20000;
    }

    /**
     * 应答最大日志打印长度
     *
     * @return
     */
    protected int getResponseLogPrintSize() {
        return 20000;
    }

    /**
     * 如果response中包含敏感信息，则子类需要覆盖次方法
     *
     * @param invoker
     * @param invocation
     * @param result     应答结果
     * @return
     */
    protected String responseWrapper(Invoker<?> invoker, Invocation invocation, Result result) {
        if (result == null) {
            return null;
        }

        return JsonWrapper.toJsonWithoutExceptionAndBigData(result.getValue(), getResponseLogPrintSize());
    }

    /**
     * 是否需要打印Error日志
     *
     * @param e 该异常是否需要打印Error日志，如果不是，则打印Warn日志
     * @return true-error日志，false-warn日志
     */
    protected boolean needPrintErrorLog(Throwable e) {
        return (e != null) && !(e instanceof RpcBizException);
    }

    /**
     * 错误信息包装
     *
     * @param e 抛出的异常
     * @return
     */
    protected String errorMsgWrapper(Throwable e) {
        if (e == null) {
            return "";
        }

        // 要求业务异常实现toString方法
        String exceptionMsg = e.toString();
        exceptionMsg = StringUtils.isBlank(exceptionMsg) ? "" : exceptionMsg;

        // 如果有多行，则只取第一行
        if (newLine != null && exceptionMsg.contains(newLine)) {
            exceptionMsg = exceptionMsg.substring(0, exceptionMsg.indexOf(newLine));
        }

        return exceptionMsg;
    }

    /**
     * 为了能在didi.log中解析出异常的code，这里提供口子让用户根据业务异常来返回异常码
     *
     * @param e
     * @return
     */
    protected Integer getExceptionCode(Throwable e) {
        // 先进行通用异常处理
        if (e instanceof RpcBizException) {
            return ((RpcBizException) e).getCode();

            // 处理dubbo的RpcException
        } else if (e instanceof RpcException) {
            return ((RpcException) e).getCode();
        }

        return null;
    }

    /**
     * 如果业务返回值是{@link RpcResult} ,那么从中获取code和msg
     *
     * @param result
     * @return null表示没有
     */
    private RpcResult getRpcResult(Result result) {
        if (result != null && result.getValue() instanceof RpcResult) {
            return (RpcResult) result.getValue();
        }

        return null;
    }

    protected String wrapLogMessage(String tag, Object msg, KeyValues keyValues) {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.isBlank(tag) ? "_undef" : tag);
        sb.append("||_msg=");
        sb.append(msg != null ? msg.toString() : "");
        if (keyValues != null && keyValues.getKeyValues().size() > 0) {
            List<Object> keyValueList = keyValues.getKeyValues();

            for (int i = 0; i < keyValueList.size() - 1; i += 2) {
                Object key = keyValueList.get(i);
                Object value = keyValueList.get(i + 1);
                if (key != null) {
                    sb.append("||" + key.toString() + "=" + value);
                }
            }
        }
        return sb.toString();
    }
}