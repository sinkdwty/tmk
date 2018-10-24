package com.hjzddata.generator.action;


import com.hjzddata.generator.action.config.HjzdGeneratorConfig;

/**
 * 代码生成器,可以生成实体,dao,service,controller,html,js
 *
 * @author hjzd
 * @Date 2017/5/21 12:38
 */
public class HjzdCodeGenerator {

    public static void main(String[] args) {

        /**
         * Mybatis-Plus的代码生成器:
         *      mp的代码生成器可以生成实体,mapper,mapper对应的xml,service
         */
        HjzdGeneratorConfig hjzdGeneratorConfig = new HjzdGeneratorConfig();
        hjzdGeneratorConfig.doMpGeneration();

        /**
         * hjzd的生成器:
         *      hjzd的代码生成器可以生成controller,html页面,页面对应的js
         */
        hjzdGeneratorConfig.doHjzdGeneration();
    }

}