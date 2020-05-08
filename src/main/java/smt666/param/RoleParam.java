package smt666.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 卢2714065385
 * @date 2020-05-06 20:46
 */
@Setter
@Getter
@ToString
public class RoleParam {

    private Integer id;

    /**<font color="#f0eb16" size=5>部门名称</font>*/
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 20, min = 2, message = "角色名称的长度需要在2~20个字之间")
    private String name;

    @NotNull(message = "请指定角色的状态")
    @Min(value = 0, message = "权限点状态不合法(可选数值为：[0]‘禁用’、[1]‘正常’)")
    @Max(value = 1, message = "权限点状态不合法(可选数值为：[0]‘禁用’、[1]‘正常’)")
    private Integer status;

    @Min(value = 1, message = "角色类型不合法(可选数值为：[1]‘后台管理员’、[2]‘总经理’、[3]‘人事财务办’、[4]‘研发生产’、[5]‘营销企划’、、[6]‘特惠VIP’、[7]‘铂金会员’、[8]‘钻石会员’、[9]‘其他’)")
    @Max(value = 9, message = "角色类型不合法(可选数值为：[1]‘后台管理员’、[2]‘总经理’、[3]‘人事财务办’、[4]‘研发生产’、[5]‘营销企划’、、[6]‘特惠VIP’、[7]‘铂金会员’、[8]‘钻石会员’、[9]‘其他’)")
    private Integer type = 1;

    /**<font color="#f0eb16" size=5>备注信息</font>*/
    @Length(max = 200, message = "角色的备注的内容只允许输入150个汉字之内")
    private String remark;
}
