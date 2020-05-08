package smt666.mall.service;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import smt666.common.beans.PageQuery;
import smt666.common.beans.PageResult;
import smt666.common.exception.ParamException;
import smt666.common.util.BeanValidator;
import smt666.common.util.IpUtil;
import smt666.common.util.RequestHolder;
import smt666.common.util.ShaSmt;
import smt666.mall.dao.SysUserMapper;
import smt666.mall.model.SysUser;
import smt666.param.UserParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 卢2714065385
 * @date 2020-05-04 19:52
 */
@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(UserParam param) {
        BeanValidator.checkParams(param);
        if ( checkTelephoneExist(param.getTelephone(), param.getId()) ) {
            // 如果手机号存在=的话，那么我们就抛出异常。
            throw new ParamException("手机号码已经被占用");
        }
        if ( checkEmailExist(param.getMail(), param.getId()) ) {
            throw new ParamException("此邮箱已经被占用（注册过）了");
        }

        /*
        关于password在实际处理中有许多种处理方式，比如：
        1、我们可以通过工具类生成一个password密码。生成密码后我们可以给用户发送邮件告诉用户新生成的密码。此方式适用于后台管理方式的密码的管理。
        2、第2种方式就是用户通过页面表单由用户自己输入一个password然后再确认一遍。此方式比较适用于社交平台的密码管理。
        此处演示中，我们给定一个密码，123456
         */
/*        String password = GeneratePassword.randomPassword();*/
        String password = "123456";
        // 密码通常是不会直接明文进行存储的，所以我们需要定义一个加密的password 。
        String encryptedPassword = ShaSmt.jdksha512(password);

        SysUser sysUser = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());
        // 在执行数据入库操作之前，我们需要向用户发送一封电子邮件，并且确保发送成功。
        // TODO: 这是因为，一旦用户如果误操作导致用户名或者密码丢失的话，那么如果不知道正确的Email的话就灾厄找不回来了。
        // 执行用户数据入库的方法。
        sysUserMapper.insertSelective(sysUser);
        sysLogService.saveUserLog(null, sysUser);
    }

    public void update (UserParam param) {
        BeanValidator.checkParams(param);
        if ( checkTelephoneExist(param.getTelephone(), param.getId()) ) {
            // 如果手机号存在=的话，那么我们就抛出异常。
            throw new ParamException("手机号码已经被占用");
        }
        if ( checkEmailExist(param.getMail(), param.getId()) ) {
            throw new ParamException("此邮箱已经被占用（注册过）了");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在！");
        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        // 获取到操作者的IP 。
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before, after);
    }

    /**
     * <font color="#f0eb16" size=3>检查<font color="#ff69f1" size=5>&nbsp;邮箱&nbsp;</font>是否<span style="color: #ff69f1;font-size: 15px;font-style: italic">存在</span>?</font>
     * @param mail
     * @param userId
     * @return 默认false，不存在。
     */
    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0 ;
    }

    /**
     * <font color="#f0eb16" size=3>检查<font color="#ff69f1" size=5>&nbsp;手机号&nbsp;</font>是否<span style="color: #ff69f1;font-size: 15px;font-style: italic">存在</span>?</font>
     * @param telephone
     * @param userId
     * @return 默认false，不存在。
     */
    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0 ;
    }

    public  SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.checkParams(page);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    /**
     * 获取所有用户信息的一个列表的方法。
     * @return 用户列表
     */
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
