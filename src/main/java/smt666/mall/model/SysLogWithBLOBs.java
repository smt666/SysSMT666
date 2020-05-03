package smt666.mall.model;

/**
 * `sys_log`这张数据表，它生成了两个Object POJO类：SysLog、SysLogWithBLOBs类。<br/>
 * 这是因为，我们数据库里面的字段的数据类型，如果是一个<font color="red" size=5>text</font>类型的话，<br/>
 * 那么generator代码自动生成工具就会把text当成一个单独的字段，单独生成一个类：例如当前这个SysLogWithBLOBs类，<br/>
 * 并且继承原来的类（SysLog）。
 */
public class SysLogWithBLOBs extends SysLog {
    private String oldValue;

    private String newValue;

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}