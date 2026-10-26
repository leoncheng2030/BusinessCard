
package vip.wqs.client.modular.user.provider;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.client.ClientUserApi;
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.service.ClientUserService;
import vip.wqs.common.exception.CommonException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户API接口提供者
 *
 * @author jetox
 * @date 2022/6/20 18:24
 **/
@Service
public class ClientUserApiProvider implements ClientUserApi {

    @Resource
    private ClientUserService clientUserService;

    @Override
    public JSONObject getUserByIdWithoutException(String userId) {
        ClientUser clientUser = clientUserService.getById(userId);
        if(ObjectUtil.isNotEmpty(clientUser)) {
            return JSONUtil.parseObj(clientUser);
        }
        return null;
    }

    @Override
    public List<JSONObject> getUserListByIdListWithoutException(List<String> userIdList) {
        return clientUserService.listByIds(userIdList).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public JSONObject getUserByIdWithException(String userId) {
        return JSONUtil.parseObj(clientUserService.queryEntity(userId));
    }

    @Override
    public List<JSONObject> getUserListByIdWithException(List<String> userIdList) {
        HashSet<String> userIdSet = CollectionUtil.newHashSet(userIdList);
        List<ClientUser> clientUserList = clientUserService.listByIds(userIdSet);
        if(clientUserList.size() != userIdSet.size()) {
            throw new CommonException("某用户不存在，id值集合为：{}", userIdSet);
        }
        return clientUserList.stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> listUserWithoutCurrent() {
        return clientUserService.list(new LambdaQueryWrapper<ClientUser>()
                .select(ClientUser::getId, ClientUser::getAccount, ClientUser::getName, ClientUser::getAvatar)
                .ne(ClientUser::getId, StpUtil.getLoginId()))
                .stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public JSONObject getOrCreateClientUserExt(String userId) {
        return JSONUtil.parseObj(clientUserService.getOrCreateClientUserExt(userId));
    }

    @Override
    public List<JSONObject> userSelector(String searchKey) {
        LambdaQueryWrapper<ClientUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ClientUser::getId, ClientUser::getName, ClientUser::getAccount, ClientUser::getAvatar);
        if (ObjectUtil.isNotEmpty(searchKey)) {
            queryWrapper.and(w -> w
                    .like(ClientUser::getName, searchKey)
                    .or().like(ClientUser::getAccount, searchKey));
        }
        queryWrapper.orderByAsc(ClientUser::getSortCode);
        return clientUserService.list(queryWrapper).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }
}
