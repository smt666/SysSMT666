package smt666.mall.utils;

import org.apache.commons.lang3.StringUtils;

import javax.print.DocFlavor;

/**
 * 业务环境中使用的工具类。
 * @author 卢2714065385
 * @date 2020-05-04 4:11
 */
public class LevelUtil {
    /**
     * 自定义各个层级之间的分隔符【.】点，我们使用点类分隔上下层目录。<br/>
     * Customize the delimiter [.] Point between each level, we use the point class to separate the upper and lower directories.
     */
    public final static String SEPARATOR = ".";

    /**
     * 定义ROOT的ID，从0开始。
     * 代表部门的层级，0就表示是第一级目录或者第1层部门。
     */
    public final static  String ROOT = "0";

    /**
     * 定义部门的Level计算规则。
     * 效果为：
     *    0        首层用第0位表示，
     *    0.1      一共2层
     *    0.1.2    一共3层
     *    0.1.2.3  一共4层
     *    0.4      一共2层
     * @param parentLevel 上级部门的level排序。
     * @param parentId 上级部门的ID 。
     * @return
     */
    public static String calculateLevel(String parentLevel, Integer parentId) {
        // 如果当前parentLevel是空值的话。
        if (StringUtils.isBlank(parentLevel)) {
            // 如果上级部门的排序号是一个空值，说明当前就是第一层，那么我们就直接返回0（即返回常量ROOT即可）。
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }

}
