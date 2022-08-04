package org.srm.assist.i18n.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class I18nCollectorContext {

    public static final String TYPE_PROMOTE = "prompt";

    public static final String TYPE_LOV_VIEW = "lovView";

    public static final String TYPE_LOV_VALUE = "lovValue";

    public static final String TYPE_ERROR_CODE = "errorCode";
    //类型，区分消息多语言，值集等
    private String type;

    //多语言模块key
    private String[] promptKey;

    //值级视图编码
    private String lovViewCode;

    //多语言模块key
    private String[] lovCodes;


    //租户id
    @JsonIgnore
    private Long tenantId;

    //菜单id
    private Long menuId;

    //菜单编码
    private String menuCode;

    //错误消息编码
    private String errorCode;


    public String[] getPromptKey() {
        return promptKey;
    }

    public void setPromptKey(String[] promptKey) {
        this.promptKey = promptKey;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getLovViewCode() {
        return lovViewCode;
    }

    public void setLovViewCode(String lovViewCode) {
        this.lovViewCode = lovViewCode;
    }

    public String[] getLovCodes() {
        return lovCodes;
    }

    public void setLovCodes(String[] lovCodes) {
        this.lovCodes = lovCodes;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
