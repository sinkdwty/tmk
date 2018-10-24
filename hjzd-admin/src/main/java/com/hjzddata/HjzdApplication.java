package com.hjzddata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @author hjzd
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication
public class HjzdApplication {

    private final static Logger logger = LoggerFactory.getLogger(HjzdApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HjzdApplication.class, args);
        logger.info("HjzdApplication is success!");
    }
}
