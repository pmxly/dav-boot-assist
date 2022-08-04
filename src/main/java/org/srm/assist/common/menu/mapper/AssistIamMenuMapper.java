package org.srm.assist.common.menu.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.assist.common.menu.IamMenu;

/**
 * Mapper
 *
 * @author xiang.li@going-link.com 2021-05-07 18:39:49
 */
public interface AssistIamMenuMapper {
    IamMenu selectById(@Param("menuId") Long menuId);
}

