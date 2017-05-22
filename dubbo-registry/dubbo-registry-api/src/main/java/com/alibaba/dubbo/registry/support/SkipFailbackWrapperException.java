package com.alibaba.dubbo.registry.support;

/**
 * Wrapper�쳣������ָʾ {@link FailbackRegistry}����Failback��
 * <p>
 * NOTE: �����ҵ������������ָʾ��ʽ��
 *
 * @author ding.lid
 * @see FailbackRegistry
 */
public class SkipFailbackWrapperException extends RuntimeException {
    public SkipFailbackWrapperException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // do nothing
        return null;
    }
}
