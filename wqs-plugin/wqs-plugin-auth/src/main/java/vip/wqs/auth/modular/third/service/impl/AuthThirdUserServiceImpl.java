
package vip.wqs.auth.modular.third.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import vip.wqs.auth.modular.third.entity.AuthThirdUser;
import vip.wqs.auth.modular.third.mapper.AuthThirdUserMapper;
import vip.wqs.auth.modular.third.service.AuthThirdUserService;

/**
 * 第三方登录Service接口实现类
 *
 * @author jetox
 * @date 2022/7/8 16:20
 **/
@Service
public class AuthThirdUserServiceImpl extends ServiceImpl<AuthThirdUserMapper, AuthThirdUser> implements AuthThirdUserService {

    @Override
    public void insertAuthThirdUser(String userId, AuthUser authUser, String platform) {
        AuthThirdUser authThirdUser = new AuthThirdUser();
        authThirdUser.setThirdId(authUser.getUuid());
        authThirdUser.setUserId(userId);
        authThirdUser.setAvatar(authUser.getAvatar());
        authThirdUser.setName(authUser.getUsername());
        authThirdUser.setNickname(authUser.getNickname());
        authThirdUser.setGender(ObjectUtil.isEmpty(authUser.getGender()) ? null : authUser.getGender().getDesc());
        authThirdUser.setCategory(platform);
        authThirdUser.setExtJson(JSONUtil.toJsonStr(authUser.getRawUserInfo()));
        this.save(authThirdUser);
    }
}
