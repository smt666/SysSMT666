package smt666.common.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * <font color="yellow">JSON字符串和Java Object类对象之间互相转换的工具类。</font><br/>
 * A tool class for converting between JSON strings and Java Object objects.<br/>
 * @author 卢2714065385
 * @date 2020-05-03 15:33
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    // 初始化设置configs
    static {
        //如果遇到了序列化的字段，那么就排除掉为空值的字段。
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    // 核心的转换的方法。
    /**
     * <font color="yellow">把对象Object转换成字符串String 。</font><br/>
     * Convert the object Object into a string String.
     * @param src <font color="yellow">表示将要传入的对象，我们调用时需要给定一个对象让方法执行转换工作。</font>Indicates the object to be passed in. When we call, we need to give an object to let the method perform the conversion work.
     * @param <T> <font color="yellow">表示将要参与转换的对象可以是任何类型的对象，但是在使用时，必须要指定一个具体对象类型。</font>Indicates that the object to be converted can be any type of object, but in use, a specific object type must be specified.
     * @return <font color="yellow">返回值是String字符串数据类型。</font>The return value is String data type.
     */
    public static <T> String objectToString(T src) {
        if ( src== null ) {
            return null;
        }
        try {
            // 如果有传入对象（对象不为空值的话）。
            // 使用三目运算，如果传入的对象它是一个String类型就直接强制类型成字符串类型，如果类型不一致那么直接将对象转换成字符串。
            return src instanceof  String ? (String)src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("【将对象解析为字符串异常parse object to string exception 】error：", e);
            //可以直接返回null值表示转换失败了，或者也可以抛出异常。
            return null;
        }
    }

    /**
     * <font color="yellow">将字符串转换成为对象。</font>Convert string to object.
     * @param src <font color="yellow">传入的字符串内容。</font>The incoming string content.</font>
     * @param tTypeReference <font color="yellow">封装了给定目标对象的TypeReference类型的对象参数。【导入import org.codehaus.jackson.type.TypeReference包】</font>Object parameters of the TypeReference type of a given target object are encapsulated.
     * @param <T> <font color="yellow">目标对象。我们在调用时需要指定最终被转换成的对象数据类型，把它封装到TypeReference中交给方法执行转换工作。</font>target. When we call, we need to specify the object data type that is finally converted, encapsulate it into TypeReference and give it to the method to perform the conversion work.
     * @return <font color="yellow">返回的值就是我们传入方法参数中的对象的类型。</font>The value returned is the type of object we passed in the method parameter.
     */
    public static <T> T stringToObject(String src, TypeReference<T> tTypeReference) {
        if ( src==null || tTypeReference==null ) {
            return null;
        }
        try {
            return (T) (tTypeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, tTypeReference));
        } catch (Exception e) {
            log.warn("将字符串解析为对象异常[parse string to object exception]，String:{}, TypeReference<T>:{}, error:{}", src, tTypeReference.getType(), e);
            return null;
        }
    }

}
