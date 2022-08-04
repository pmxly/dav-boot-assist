package org.srm.assist.common.menu;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.ServerRequestUtils;
import org.springframework.stereotype.Component;
import org.srm.assist.common.menu.mapper.AssistIamMenuMapper;

import javax.servlet.ServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class MenuHelper {
    private static final String MENU_ID_CODE_CACHE_KEY = "sast:menu_id_code:";
    private static final String MENU_ID_CODE_CACHE_SHAM_VALUE = "NIL";
    private static final String MENU_ID_KEY = "H-Menu-Id";
    private final RedisHelper redisHelper;
    private final AssistIamMenuMapper assistIamMenuMapper;

    public MenuHelper(RedisHelper redisHelper, AssistIamMenuMapper assistIamMenuMapper) {
        this.redisHelper = redisHelper;
        this.assistIamMenuMapper = assistIamMenuMapper;
    }

    public static long getMenuId(ServletRequest servletRequest) {
        long menuId = -1;

        if (Objects.isNull(servletRequest)) {
            return menuId;
        }
        String menuIdStr = servletRequest.getParameter(MENU_ID_KEY.toLowerCase());
        if (StringUtils.isBlank(menuIdStr)) {
            menuIdStr = ServerRequestUtils.getHeaderValue(servletRequest, MENU_ID_KEY);
        }
        try {
            if (Objects.nonNull(menuIdStr)) {
                menuId = Long.parseLong(menuIdStr);
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        return menuId;
    }

    /**
     * 根据菜单id查询菜单code
     *
     * @param menuId 菜单id，不为null
     * @return 菜单code，可能为null
     */
    public Optional<String> getMenuCodeById(Long menuId) {
        String menuCode = redisHelper.strGet(MENU_ID_CODE_CACHE_KEY + menuId);
        if (Objects.nonNull(menuCode)) {
            if (Objects.equals(menuCode, MENU_ID_CODE_CACHE_SHAM_VALUE)) {
                return Optional.empty();
            }
            return Optional.of(menuCode);
        }
        IamMenu iamMenu = assistIamMenuMapper.selectById(menuId);
        if (Objects.isNull(iamMenu) || Objects.isNull(iamMenu.getCode())) {
            redisHelper.strSet(MENU_ID_CODE_CACHE_KEY + menuId, MENU_ID_CODE_CACHE_SHAM_VALUE, 10, TimeUnit.MINUTES);
            return Optional.empty();
        }
        menuCode = iamMenu.getCode();
        redisHelper.strSet(MENU_ID_CODE_CACHE_KEY + menuId, menuCode);
        return Optional.of(menuCode);
    }
}
