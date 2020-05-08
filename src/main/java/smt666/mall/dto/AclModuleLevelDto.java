package smt666.mall.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import smt666.mall.model.SysAclModule;

import java.util.List;

/**
 * @author 卢2714065385
 * @date 2020-05-05 14:40
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {

    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclDtoList = Lists.newArrayList();

    public static AclModuleLevelDto adpt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
