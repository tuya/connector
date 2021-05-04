package com.tuya.connector.api.config;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/22 8:36 下午
 */
public class Logging {

    public enum Level {

        /**
         * the logging level of error
         */
        ERROR,

        /**
         * the logging level of warn
         */
        WARN,

        /**
         * the logging level of info
         */
        INFO,

        /**
         * the logging level of debug
         */
        DEBUG,

        /**
         * the logging level of trace
         */
        TRACE;
    }

    public enum Strategy {

        /**
         * the logging strategy of none
         */
        NONE,

        /**
         * the logging strategy of basic
         */
        BASIC,

        /**
         * the logging strategy of headers
         */
        HEADERS,

        /**
         * the logging strategy of body
         */
        BODY
    }
}
