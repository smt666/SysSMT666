package smt666.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DTO-<font color="#f0eb16" size=5>用户信息需要的常用参数</font>
 *  用户所需要传入的参数的合法性检验的参数类。
 * ==============================================
 * 说明一下，在这个DTO中password为什么不组织进来。
 * 因为，我们在对用户进行管理时是不会对用户的登录密码进行管理的。
 * 并且更新用户信息操作时也不会去更新用户密码的。
 * @author 卢2714065385
 * @date 2020-05-04 18:14
 */
@Getter
@Setter
public class UserParam {
    private Integer id;
    @NotBlank(message = "请输入您的用户名/账号！")
    @Length(min = 0, max = 20, message = "用户名或者账号的长度需要在20字以内！")
    private String username;
    @NotBlank(message = "请填写您的手机号！")
    @Length(max = 13, message = "手机号长度不可超过13位！")
    private String telephone;
    @NotBlank(message = "请输入您的邮箱地址！")
    @Length(min = 5, max = 50, message = "邮箱地址的长度需要在5~50个字符之间！")
    private String mail;
    @NotNull(message = "必须填写您所在部门的编号!")
    private Integer deptId;
    @NotNull(message = "必须指定当前用户的状态！")
    @Min(value = 0, message = "您只能选择【[0]禁用、[1]正常、[2]黑名单】其中之一的数字！")
    @Max(value = 2, message = "您只能选择【[0]禁用、[1]正常、[2]黑名单】其中之一的数字！")
    private Integer status;
    @Length(min = 0, max = 200, message = "备注信息的长度需要在200个字以内！")
    private String remark = "";
}
