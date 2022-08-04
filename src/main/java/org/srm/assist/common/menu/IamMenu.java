package org.srm.assist.common.menu;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 
 *
 * @author xiang.li@going-link.com 2021-05-07 18:39:49
 */
@Table(name = "iam_menu")
public class IamMenu {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_H_QUICK_INDEX = "hQuickIndex";
    public static final String FIELD_FD_LEVEL = "fdLevel";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_SORT = "sort";
    public static final String FIELD_IS_DEFAULT = "isDefault";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_ROUTE = "route";
    public static final String FIELD_H_CUSTOM_FLAG = "hCustomFlag";
    public static final String FIELD_H_TENANT_ID = "hTenantId";
    public static final String FIELD_H_LEVEL_PATH = "hLevelPath";
    public static final String FIELD_H_VIRTUAL_FLAG = "hVirtualFlag";
    public static final String FIELD_H_CONTROLLER_TYPE = "hControllerType";
    public static final String FIELD_H_PERMISSION_TYPE = "hPermissionType";
    public static final String FIELD_H_DESCRIPTION = "hDescription";
    public static final String FIELD_H_ENABLED_FLAG = "hEnabledFlag";
    public static final String FIELD_CATEGORY = "category";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    private String code;
    @NotBlank
    private String name;
    private String hQuickIndex;
    @NotBlank
    private String fdLevel;
    @NotNull
    private Long parentId;
    @NotBlank
    private String type;
    private Long sort;
    @NotNull
    private Integer isDefault;
    private String icon;
    private String route;
    @NotNull
    private Integer hCustomFlag;
    @NotNull
    private Long hTenantId;
    private String hLevelPath;
    private Integer hVirtualFlag;
    private String hControllerType;
    private String hPermissionType;
    private String hDescription;
    @NotNull
    private Integer hEnabledFlag;
    private String category;

//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------


    /**
     * @return 
     */
    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }
    /**
     * @return 
     */
    public String getCode() {
            return code;
    }

    public void setCode(String code) {
            this.code = code;
    }
    /**
     * @return 菜单名
     */
    public String getName() {
            return name;
    }

    public void setName(String name) {
            this.name = name;
    }
    /**
     * @return 款速索引
     */
    public String getHQuickIndex() {
            return hQuickIndex;
    }

    public void setHQuickIndex(String hQuickIndex) {
            this.hQuickIndex = hQuickIndex;
    }
    /**
     * @return 菜单层级
     */
    public String getFdLevel() {
            return fdLevel;
    }

    public void setFdLevel(String fdLevel) {
            this.fdLevel = fdLevel;
    }
    /**
     * @return 父级菜单id
     */
    public Long getParentId() {
            return parentId;
    }

    public void setParentId(Long parentId) {
            this.parentId = parentId;
    }
    /**
     * @return 菜单类型， 包括三种（root, dir, menu）
     */
    public String getType() {
            return type;
    }

    public void setType(String type) {
            this.type = type;
    }
    /**
     * @return 菜单顺序
     */
    public Long getSort() {
            return sort;
    }

    public void setSort(Long sort) {
            this.sort = sort;
    }
    /**
     * @return 是否是默认菜单,0不是默认菜单，1是默认菜单
     */
    public Integer getIsDefault() {
            return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
            this.isDefault = isDefault;
    }
    /**
     * @return 图标的code值
     */
    public String getIcon() {
            return icon;
    }

    public void setIcon(String icon) {
            this.icon = icon;
    }
    /**
     * @return 
     */
    public String getRoute() {
            return route;
    }

    public void setRoute(String route) {
            this.route = route;
    }
    /**
     * @return 客户化菜单标识
     */
    public Integer getHCustomFlag() {
            return hCustomFlag;
    }

    public void setHCustomFlag(Integer hCustomFlag) {
            this.hCustomFlag = hCustomFlag;
    }
    /**
     * @return 客户化菜单租户标识
     */
    public Long getHTenantId() {
            return hTenantId;
    }

    public void setHTenantId(Long hTenantId) {
            this.hTenantId = hTenantId;
    }
    /**
     * @return 
     */
    public String getHLevelPath() {
            return hLevelPath;
    }

    public void setHLevelPath(String hLevelPath) {
            this.hLevelPath = hLevelPath;
    }
    /**
     * @return 是否虚拟菜单, 虚拟菜单不参与左侧菜单栏展示
     */
    public Integer getHVirtualFlag() {
            return hVirtualFlag;
    }

    public void setHVirtualFlag(Integer hVirtualFlag) {
            this.hVirtualFlag = hVirtualFlag;
    }
    /**
     * @return 控制类型
     */
    public String getHControllerType() {
            return hControllerType;
    }

    public void setHControllerType(String hControllerType) {
            this.hControllerType = hControllerType;
    }
    /**
     * @return 权限控制类型列表，多种类型逗号分隔，可以为api,button,table,formItem,fields
     */
    public String getHPermissionType() {
            return hPermissionType;
    }

    public void setHPermissionType(String hPermissionType) {
            this.hPermissionType = hPermissionType;
    }
    /**
     * @return 
     */
    public String getHDescription() {
            return hDescription;
    }

    public void setHDescription(String hDescription) {
            this.hDescription = hDescription;
    }
    /**
     * @return 
     */
    public Integer getHEnabledFlag() {
            return hEnabledFlag;
    }

    public void setHEnabledFlag(Integer hEnabledFlag) {
            this.hEnabledFlag = hEnabledFlag;
    }
    /**
     * @return 项目层菜单分类，可以为AGILE，PROGRAM，ANALYTICAL
     */
    public String getCategory() {
            return category;
    }

    public void setCategory(String category) {
            this.category = category;
    }
}
