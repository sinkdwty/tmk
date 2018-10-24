package com.hjzddata;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Hjzd Web程序启动类
 *
 * @author fengshuonan
 * @date 2017-05-21 9:43
 */
public class HjzdServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HjzdApplication.class);
    }
}
