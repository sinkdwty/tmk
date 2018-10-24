package com.hjzddata.core.common.constant.dictmap.factory;

import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.IConstantFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;

import java.lang.reflect.Method;

/**
 * 字典字段的包装器(从ConstantFactory中获取包装值)
 *
 * @author fengshuonan
 * @date 2017-05-06 15:12
 */
public class DictFieldWarpperFactory {

    public static Object createFieldWarpper(Object parameter, String methodName) {
        IConstantFactory constantFactory = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, parameter.getClass());
            return method.invoke(constantFactory, parameter);
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
                return method.invoke(constantFactory, Integer.parseInt(parameter.toString()));
            } catch (Exception e1) {
                throw new HjzdException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
