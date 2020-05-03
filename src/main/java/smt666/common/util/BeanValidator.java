package smt666.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import smt666.common.exception.ParamException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author 卢2714065385
 */
public class BeanValidator {

    /** 我们通过ValidatorFactory对象使用工厂模式创建出对象。*/
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 单个检验的方法。
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        // 获取到检验的结果。
        Set validataResult = validator.validate(t, groups);
        if (validataResult.isEmpty()) {
            // 如果当前校验没有错误信息的话，那么返回的是一个空的Map值，即空值。
            return Collections.emptyMap();
        }
        // 如果当前检验是有值的话（即整个时候代表有错误了的情况）。
        LinkedHashMap errors = Maps.newLinkedHashMap();
        // 遍历结果。
        Iterator iterator = validataResult.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation violation = (ConstraintViolation) iterator.next();
            // 把收集到的每一条错误的信息，存放早Map集合errors中。
            // key[violation.getPropertyPath().toString()] 就是有问题的字段。
            // value[violation.getMessage()] 就是错误的信息。
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    /**
     * 多个检验的方法。
     * @param collection
     * @return Map&lt;String, String> 【key】表示有问题的字段，【value】是错误的详细信息——原因。
     */
    public static Map<String, String> validateList(Collection<?> collection) {
        // 先配置当前传入的这些collection错误信息是否为空值，正确就会导致空值的结果（没有错误即空值）。
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if ( !iterator.hasNext() ) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * 当我们需要检验一个类是否合法时，我们只需要调用此方法即可。
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if ( objects!=null && objects.length>0 ){
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }

    public static void checkParams(Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);
        // 判断
//        if ( map != null && map.entrySet().size()>0 ) {
//            // 如果map有错误信息的话，那么我们直接抛出异常。
//            throw new ParamException(map.toString());
//        }
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
