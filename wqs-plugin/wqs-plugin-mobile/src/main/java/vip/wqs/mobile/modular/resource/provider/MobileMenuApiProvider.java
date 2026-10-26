
package vip.wqs.mobile.modular.resource.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.mobile.api.MobileMenuApi;
import vip.wqs.mobile.modular.resource.entity.MobileMenu;
import vip.wqs.mobile.modular.resource.service.MobileMenuService;

import java.util.List;

/**
 * 移动端菜单API接口提供者
 *
 * @author jetox
 * @date 2023/1/31 10:12
 **/
@Service
public class MobileMenuApiProvider implements MobileMenuApi {

    @Resource
    private MobileMenuService mobileMenuService;

    @Override
    public List<JSONObject> mobileMenuTreeSelector() {
        return mobileMenuService.mobileMenuTreeSelector();
    }

    @Override
    public List<JSONObject> mobileMenuTreeSelector(List<JSONObject> originDataList) {
        return mobileMenuService.mobileMenuTreeSelector(BeanUtil.copyToList(originDataList, MobileMenu.class));
    }

    @Override
    public List<Tree<String>> loginMobileMenuTree(List<String> menuIdList) {
        return mobileMenuService.loginMobileMenuTree(menuIdList);
    }
}
