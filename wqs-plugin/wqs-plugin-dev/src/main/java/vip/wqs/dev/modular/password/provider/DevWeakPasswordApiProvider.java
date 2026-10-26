
package vip.wqs.dev.modular.password.provider;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.dev.api.DevWeakPasswordApi;
import vip.wqs.dev.modular.password.entity.DevWeakPassword;
import vip.wqs.dev.modular.password.service.DevWeakPasswordService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 弱密码库API接口实现类
 *
 * @author jetox
 * @date 2022/6/17 14:43
 **/
@Service
public class DevWeakPasswordApiProvider implements DevWeakPasswordApi {

    @Resource
    private DevWeakPasswordService devWeakPasswordService;

    @Override
    public List<String> weakPasswordList() {
        return devWeakPasswordService.list().stream().map(DevWeakPassword::getPassword).collect(Collectors.toList());
    }
}
