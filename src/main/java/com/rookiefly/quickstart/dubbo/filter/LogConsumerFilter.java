package com.rookiefly.quickstart.dubbo.filter;

import com.rookiefly.quickstart.dubbo.exception.BizException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;

@Activate(
        group = {CommonConstants.CONSUMER},
        order = 100
)
public class LogConsumerFilter extends AbstractLogDubboFilter {

    @Override
    protected boolean isProvider() {
        return false;
    }

    @Override
    public boolean needPrintErrorLog(Throwable e) {
        return !(e instanceof BizException);
    }

    @Override
    protected Integer getExceptionCode(Throwable e) {
        return e instanceof BizException ? ((BizException) e).getCode() : super.getExceptionCode(e);
    }
}