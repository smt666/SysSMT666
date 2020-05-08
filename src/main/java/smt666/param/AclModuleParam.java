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
 * @date 2020-05-05 7:41
 */
@Getter
@Setter
@ToString
public class AclModuleParam {

    private Integer id;
    @NotBlank(message = "权限模块的名称不能为空")
    @Length(min = 2, max = 20, message = "权限模块的名称只能在2~20字以内")
    private String name;
    private Integer parentId = 0;
    private String level;
    @NotNull(message = "权限模块展示顺序的数值不能为空")
    private Integer seq;
    @NotNull(message = "权限模块的使用状态的数值不能为空")
    @Min(value = 0, message = "权限模块状态的可选值为[1]启用、[0]禁用这两种状态之一")
    @Max(value = 1, message = "权限模块状态的可选值为[1]启用、[0]禁用这两种状态之一")
    private Integer status;
    @Length(max = 200, message = "权限模块的备注信息不允许超过200个字")
    private String remark;

}
