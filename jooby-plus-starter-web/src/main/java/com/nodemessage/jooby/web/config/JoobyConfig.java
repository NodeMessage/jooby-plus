package com.nodemessage.jooby.web.config;

import io.jooby.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wjsmc
 * @date 2022/8/11 15:11
 * @description
 **/
public class JoobyConfig {

    private Logger logger = LoggerFactory.getLogger(JoobyConfig.class);

    private Jooby jooby;

    public JoobyConfig(Jooby jooby) {
        this.jooby = jooby;
    }

    public JoobyConfig before(Route.Before before) {
        logger.info("before setting successfully");
        jooby.before(before);
        return this;
    }

    public JoobyConfig enableGracefulShutdown() {
        logger.info("enable GracefulShutdown successfully");
        jooby.install(new GracefulShutdown());
        return this;
    }

    public JoobyConfig enableAccessLog() {
        logger.info("enable AccessLog successfully");
        jooby.decorator(new AccessLogHandler());
        return this;
    }

    public JoobyConfig enableCors(Cors cors) {
        logger.info("enable enableCors successfully");
        jooby.decorator(new CorsHandler(cors));
        return this;
    }

    public JoobyConfig enableCors() {
        logger.info("enable enableCors successfully");
        jooby.decorator(new CorsHandler());
        return this;
    }

    public JoobyConfig addErrorGlobal(Class<Throwable> throwable, ErrorHandler errorHandler) {
        logger.info("add errorGlobal successfully");
        jooby.error(throwable, errorHandler);
        return this;
    }

    public JoobyConfig addErrorGlobal(ErrorHandler errorHandler) {
        logger.info("add errorGlobal successfully");
        jooby.error(errorHandler);
        return this;
    }

    public JoobyConfig addErrorGlobal(StatusCode statusCode, ErrorHandler errorHandler) {
        logger.info("add errorGlobal successfully");
        jooby.error(statusCode, errorHandler);
        return this;
    }
}
