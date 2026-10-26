
package vip.wqs.client.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.client.modular.user.entity.ClientUserPassword;
import vip.wqs.client.modular.user.mapper.ClientUserPasswordMapper;
import vip.wqs.client.modular.user.service.ClientUserPasswordService;
import vip.wqs.common.util.CommonCryptogramUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * C端用户密码Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class ClientUserPasswordServiceImpl extends ServiceImpl<ClientUserPasswordMapper, ClientUserPassword> implements ClientUserPasswordService {

    @Override
    public void insertUserPasswordHistory(String userId, String newPassword) {
        ClientUserPassword clientUserPassword = new ClientUserPassword();
        clientUserPassword.setUserId(userId);
        clientUserPassword.setPassword(CommonCryptogramUtil.doHashValue(newPassword));
        this.save(clientUserPassword);
    }

    @Override
    public List<String> getUserPasswordHistoryLimit(String userId, int limitValue) {
        return this.page(new Page<>(1, limitValue), new LambdaQueryWrapper<ClientUserPassword>()
                        .eq(ClientUserPassword::getUserId, userId).orderByDesc(ClientUserPassword::getCreateTime))
                .getRecords().stream().map(ClientUserPassword::getPassword).collect(Collectors.toList());
    }
}
