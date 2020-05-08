package smt666.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * DTO-<font color="#f0eb16" size=5>部门信息需要的常用参数</font>
 * 部门所需要传入的参数的合法性检验的参数类。
 * @author 卢2714065385
 * @date 2020-05-04 8:41
 */
@Getter
@Setter
@ToString
public class DeptParam {
    /**新增时不需要但是<font color="#f0eb16" size=5>更新</font>时会用到不id 。*/
    private Integer id;
    /**<font color="#f0eb16" size=5>部门名称</font>*/
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15, min = 2, message = "部门名称的长度需要在2~15个字之间")
    private String name;
    /**<font color="#f0eb16" size=5>上级部门ID</font>如果parentId没有值传过来那么就会是个null值，但是一旦传到数据表SQL中就会报错，所以我， 在此处给个默认缺省值0（即如果没有值那么默认为0）*/
    private Integer parentId = 0;
    /**<font color="#f0eb16" size=5>顺序</font>*/
    @NotNull(message = "展示顺序不可以为空")private Integer seq;
    /**<font color="#f0eb16" size=5>备注信息</font>*/
    @Length(max = 150, message = "备注的内容只允许输入150个汉字之内")
    private String remark;
}
