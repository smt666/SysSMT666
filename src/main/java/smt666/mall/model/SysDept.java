package smt666.mall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * `sys_dept`表对应的<font color="#f0eb16" size=5>SysDept</font>（entry实体）类<br/><br/>
 * 【<font color="#ff69f1">@Builder</font>】Builder 使用创建者模式又叫建造者模式。<br/>
 *     简单来说，就是一步步创建一个对象，它对用户屏蔽了里面构建的细节，<br/>
 *     但却可以精细地控制对象的构造过程。
 * 【<font color="#ff69f1">@NoArgsConstructor</font>】无参构造器。不需要参数的构造方法。<br/>
 * 【<font color="#ff69f1">@AllArgsConstructor</font>】全参构造器。需要所有参数的构造方法。<br/>
 * 【<font color="#ff69f1">@ToString</font>】toString方法。
 * @author 卢2714065385
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysDept {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}