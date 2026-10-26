
package vip.wqs.mobile.modular.resource.provider;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.mobile.api.MobileButtonApi;
import vip.wqs.mobile.modular.resource.entity.MobileButton;
import vip.wqs.mobile.modular.resource.service.MobileButtonService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 移动端按钮API接口提供者
 *
 * @author jetox
 * @date 2023/1/31 10:12
 **/
@Service
public class MobileButtonApiProvider implements MobileButtonApi {

    @Resource
    private MobileButtonService mobileButtonService;

    @Override
    public List<String> listButtonCodeListByIdList(List<String> buttonIdList) {
        return mobileButtonService.listByIds(buttonIdList).stream().map(MobileButton::getCode).collect(Collectors.toList());
    }
}
