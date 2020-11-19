package com.rookiefly.quickstart.dubbo.constants;

public final class LogKey {
    public static final String DEFAULT_KEY = "_msg";
    public static final String TRACE_ID = "traceid";
    public static final String REQUEST = "request";
    public static final String REQUEST_BODY = "request_body";
    public static final String RESPONSE = "response";
    public static final String PROC_TIME = "proc_time";
    public static final String REMOTE_APPLICATION = "remote_application";
    public static final String APPLICATION = "application";
    public static final String SERVICE_NAME = "service_name";
    public static final String METHOD_NAME = "method_name";
    public static final String ERROR_NO = "errno";
    public static final String ERROR_MSG = "errmsg";
    public static final String TABLE_NAME = "table";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String VERSION = "version";
    public static final String MQ_TOPIC = "topic";
    public static final String PRODUCER_GROUP = "p_group";
    public static final String CONSUMER_GROUP = "c_group";
    public static final String QUEUE_ID = "q_id";
    public static final String OFFSET = "offset";
    public static final String MQ_BODY = "mq_body";
    public static final String TIP = "tip";
    public static final String TIMESTAMP = "timestamp";
    public static final String APP_TYPE = "app_type";

    private LogKey() {
    }
}