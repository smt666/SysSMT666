package smt666.mall.service;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import smt666.common.beans.PageQuery;
import smt666.common.beans.PageResult;
import smt666.common.exception.ParamException;
import smt666.common.util.BeanValidator;
import smt666.common.util.IpUtil;
import smt666.common.util.RequestHolder;
import smt666.mall.dao.SysAclMapper;
import smt666.mall.model.SysAcl;
import smt666.mall.utils.GeneratePassword;
import smt666.param.AclParam;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(AclParam param) {
        // 对传入的权限点请求的参数进行校验。
        BeanValidator.checkParams(param);
        // 如果经过检查，没有发现需要的参数的话，那么我们就直接抛出异常。
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        // 组装数据。
        SysAcl acl = SysAcl.builder()
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null, acl);
    }

    public void update(AclParam param) {
        BeanValidator.checkParams(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder()
                .id(param.getId())
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);
    }

    /**
     *
     * @param aclModuleId
     * @param name
     * @param id
     * @return
     */
    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    /**
     * 每个权限点，都有自己的唯一的编号值（code值）。<br/>
     * 它代表了当前这个权限点，是唯一标识当前权限的数值编号。
     * @return
     */
    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        System.out.println(dateFormat.format(new Date()));
        //输出结果 20200506165554
        System.out.println(dateFormat.format(new Date()) + "_" + GeneratePassword.randomCode());
        //输出结果 20200506165554_Gh4WQIZ
        return dateFormat.format(new Date()) + "_" + GeneratePassword.randomCode();
    }

    /**
     * 根据权限模块id获取当前模块下的所有权限点的列表信息。
     * @param aclModuleId
     * @param page
     * @return
     */
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page) {
        // 对分页信息进行检验。
        BeanValidator.checkParams(page);
        // 在确保分页信息参数没有问题后我们计算出当前的总模块数。
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        // 如果有查询到模块的信息，那么我们继续查询当前模块下的所有的权限点信息。
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
