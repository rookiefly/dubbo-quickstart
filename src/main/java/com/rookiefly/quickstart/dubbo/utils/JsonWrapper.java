package com.rookiefly.quickstart.dubbo.utils;

import com.alibaba.fastjson.JSON;
import com.rookiefly.quickstart.dubbo.remote.api.RpcResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Collection;

final public class JsonWrapper extends JSON {

    protected static final Logger logger = LoggerFactory.getLogger(JsonWrapper.class);

    /**
     * 输出日志时如果遇到数组、列表，那么长度超过该size就不输出日志了
     */
    private static volatile int printLogArraySizeLimit = 200;

    /**
     * 过滤异常和大的数据对象
     * <p>
     * 注意：该方法返回的字符串不一定是可逆的
     *
     * @param object
     * @param logMaxSize 日志输出的最大长度
     * @return
     */
    public static String toJsonWithoutExceptionAndBigData(Object object, int logMaxSize) {
        if (object == null || logMaxSize <= 0) {
            return null;
        }

        /**
         * 一般都是RpcResult，所以我们真正要检查的是其中的data字段
         */
        Object dataObject = object instanceof RpcResult ? ((RpcResult) object).getData() : object;
        if (dataObject == null) {
            // 如果数据对象是null，那么直接返回object（即RpcResult）的json格式
            return JSON.toJSONString(object);
        }

        Class<?> dataObjectClazz = dataObject.getClass();

        if (dataObjectClazz.isArray() && (dataObject instanceof byte[] || dataObject instanceof Byte[] || Array.getLength(dataObject) > printLogArraySizeLimit)) {
            return "this array is byte[] or size:" + Array.getLength(dataObject) + " too big, can not print.";
        }

        if (dataObject instanceof Collection<?> && ((Collection<?>) dataObject).size() > printLogArraySizeLimit) {
            return "this list size:" + ((Collection<?>) dataObject).size() + " is too big, can not print.";
        }

        try {
            // 为了防止日志成为瓶颈，这里对大数据对象进行截取
            String result = JSON.toJSONString(object);
            return (StringUtils.isBlank(result) || result.length() <= logMaxSize) ? result :
                    result.substring(0, logMaxSize) + " ...略... ";

        } catch (Exception e) {
            logger.error("JsonWrapper#toJsonWithoutExceptionAndBigData 将其转换成Json串异常：" + object, e);
        }

        return null;
    }
}