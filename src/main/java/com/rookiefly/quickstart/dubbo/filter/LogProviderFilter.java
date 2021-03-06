package com.rookiefly.quickstart.dubbo.filter;

import com.rookiefly.quickstart.dubbo.exception.BizException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;

@Activate(
        group = {CommonConstants.PROVIDER},
        order = 100
)
public class LogProviderFilter extends AbstractLogDubboFilter {

    @Override
    protected boolean isProvider() {
        return true;
    }

    @Override
    public boolean needPrintErrorLog(Throwable e) {
        return !(e instanceof BizException);
    }

    @Override
    protected String getExceptionCode(Throwable e) {
        return e instanceof BizException ? ((BizException) e).getErrorCode().getCode() : super.getExceptionCode(e);
    }
}