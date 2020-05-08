package smt666.common.beans;

import lombok.Getter;

/**
 * 枚举。redis缓存时的Key的前缀。
 */
@Getter
public enum CacheKeyConstants {

    /**
     * 系统相关的权限的前缀。
     */
    SYSTEM_ACLS,

    /* 缓存用户的权限。 */
    USER_ACLS;

}
