package smt666.mall.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import smt666.mall.model.SysDept;

import java.util.List;

/**
 *  dept部门相关的<font color="#f0eb16" size=5>DeptLevelDto</font>的DTO数据传输类。<br/>
 *  【<font color="#ff69f1">@Getter</font>】【<font color="#ff69f1">@Setter</font>】【<font color="#ff69f1">@ToString</font>】<br/>
 *  <br/><br/>
 *  DTO（Data Transfer Object）数据传输对象的模型，它可以将数据封装成普通的JavaBeans在J2EE多个层级间传输。<br/>
 *  使用DTO好处：<br/>&nbsp;&nbsp;&nbsp;&nbsp;
 *     1、依据现有的类代码即可方便构造出DTO对象，无需重新进行分析。<br/>&nbsp;&nbsp;&nbsp;&nbsp;
 *     2、减少请求的次数，大大提高效率。<br/>&nbsp;&nbsp;&nbsp;&nbsp;
 *     3、按需组织DTO对象：页面需要的字段我才组织进来，不需要的就不组织到DTO中。
 *             有效避免传统直接传输整个表字段造成数据表结构的泄漏隐患，提供安全性。<br/>&nbsp;&nbsp;&nbsp;&nbsp;
 *     4、一般会使用DTO类extends继承entry实体类，并且在DTO类中复制基础的Set、Get等方法。
 *     当我们在业务逻辑或者交互层需要用到一些数据库中也不存在的字段时，我们也可以组织到DTO类中，
 *     这些封装在DTO中的所有字段，就相当于是经过处理了的数据库中的字段，实质意义就是便于数据交互，提高效率。<br/>
 *     <br/>DTO (Data Transfer Object) data transfer object model, which can encapsulate data into ordinary JavaBeans and transfer between multiple levels of J2EE.
 *     <br/>&nbsp;&nbsp;&nbsp;&nbsp;Benefits of using DTO:
 *     <br/>1. The DTO object can be easily constructed based on the existing class code without re-analysis.
 *     <br/>2. Reduce the number of requests, greatly improve efficiency.
 *     <br/>3. Organize DTO objects on demand: I only organize the fields that are needed on the page. Those that are not needed are not organized in DTO, which effectively avoids the hidden danger of data table structure caused by traditional direct transmission of the entire table field and provides security.
 *     <br/>4. Generally, the DTO class extends is used to inherit the entry entity class, and the basic Set, Get and other methods are copied in the DTO class. When we need to use some fields that do not exist in the database in the business logic or interaction layer, we can also be organized into DTO classes.  All the fields encapsulated in DTO are equivalent to processed databases The essential meaning of the field is to facilitate data interaction and improve efficiency.
 * @author 卢2714065385
 * @date 2020-05-04 8:51
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    /**
     * 自己包含了自己：组成一个树形结构。自己包含了自己：组成一个树形结构。Self contains himself: forming a tree structure.
     */
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 将SysDept类复制给DeptLevelDto类。如果我们传入一个部门的对象，那么就会copy成本地的一个DTO对象。<br/>
     * Copy the SysDept class to the DeptLevelDto class. If we pass in an object of a department, then a DTO object of local cost will be copied.
     * @param dept
     * @return
     */
    public static DeptLevelDto copyToDeptDTO(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        // 将SysDept类复制给DeptLevelDto类。如果我们传入一个部门的对象，那么就会copy成本地的一个DTO对象。
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
