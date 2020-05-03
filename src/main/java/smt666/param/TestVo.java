package smt666.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 校验参数是否合法的测试案例类。
 * @author 卢2714065385
 */
@Getter
@Setter
public class TestVo {

    /** 【判断字符串是否为空】表示整个msg属性不能为空值。当使用引用类型时需要使用@NotBlank这个注解。 */
    @NotBlank
    private String msg;

    /** 【判断一个对象是否为空】不允许id的值为null值。当使用Java包装类型时需要使用@NotNull这个注解。 */
    @NotNull(message = "id不能为空值[You have not entered the id value yet!]。")
    @Max(value = 10, message = "id不能大于10[id value must be less than 10]。")
    @Min(value = 0, message = "id值必须大于或者等于0[id value must be greater than or equal to 0]。")
    private Integer id;

    /** 【配置数组、集合是否为空】不能为空值 。当使用List时需要使用@NotEmpty这个注解。*/
//    @NotEmpty
//    private List<String> str;
}
