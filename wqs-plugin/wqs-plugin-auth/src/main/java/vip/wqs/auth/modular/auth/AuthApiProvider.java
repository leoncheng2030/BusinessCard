
package vip.wqs.auth.modular.auth;


import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.auth.api.AuthApi;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.auth.modular.login.enums.AuthDeviceTypeEnum;
import vip.wqs.auth.modular.login.enums.AuthStrategyWhenNoUserWithPhoneOrEmailEnum;
import vip.wqs.auth.modular.login.param.AuthAccountPasswordLoginParam;
import vip.wqs.auth.modular.login.service.AuthService;
import vip.wqs.auth.modular.third.service.AuthThirdService;

import java.util.List;

/**
 * 认证鉴权API实现类
 *
 * @author yubaoshan
 * @date 2024/7/18 17:35
 **/
@Service
public class AuthApiProvider implements AuthApi {

    @Resource
    private AuthThirdService authThirdService;

    @Resource
    private AuthService authService;

    @Override
    public JSONObject getUserSessionCount() {
        JSONObject resultObj = new JSONObject();
        List<JSONObject> sessionListB = StpUtil.searchSessionId("", -1, -1, true).stream().map(sessionId -> {
            JSONObject jsonObject = JSONUtil.createObj();
            String userId = StrUtil.split(sessionId, StrUtil.COLON).get(3);
            SaSession saSession = StpUtil.getSessionByLoginId(userId, false);
            int tokenCount = saSession.getTerminalList().size();
            long createTime = saSession.getCreateTime();
            jsonObject.set("userId", userId);
            jsonObject.set("tokenCount", tokenCount);
            jsonObject.set("createTime", DateTime.of(createTime));
            return jsonObject;
        }).toList();

        List<JSONObject> sessionListC = StpClientUtil.searchSessionId("", -1, -1, true).stream().map(sessionId -> {
            JSONObject jsonObject = JSONUtil.createObj();
            String userId = StrUtil.split(sessionId, StrUtil.COLON).get(3);
            SaSession saSession = StpClientUtil.getSessionByLoginId(userId, false);
            int tokenCount = saSession.getTerminalList().size();
            long createTime = saSession.getCreateTime();
            jsonObject.set("userId", userId);
            jsonObject.set("tokenCount", tokenCount);
            jsonObject.set("createTime", DateTime.of(createTime));
            return jsonObject;
        }).toList();
        resultObj.set("backUserSessionCount" ,sessionListB.size());
        resultObj.set("clientUserSessionCount" ,sessionListC.size());
        return resultObj;
    }

    @Override
    public Long getThirdUserCount() {
        return authThirdService.count();
    }

    @Override
    public boolean getDefaultCaptchaOpenForB() {
        return authService.getDefaultCaptchaOpenForB();
    }

    @Override
    public boolean getDefaultCaptchaOpenForC() {
        return authService.getDefaultCaptchaOpenForC();
    }

    @Override
    public void validValidCode(String phoneOrEmail, String validCode, String validCodeReqNo) {
        authService.validValidCode(phoneOrEmail, validCode, validCodeReqNo);
    }

    @Override
    public String doLoginForB(String account, String password, String validCode, String validCodeReqNo) {
        AuthAccountPasswordLoginParam authAccountPasswordLoginParam = new AuthAccountPasswordLoginParam();
        authAccountPasswordLoginParam.setAccount(account);
        authAccountPasswordLoginParam.setPassword(password);
        authAccountPasswordLoginParam.setValidCode(validCode);
        authAccountPasswordLoginParam.setValidCodeReqNo(validCodeReqNo);
        return authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.B.getValue());
    }

    @Override
    public String doLoginForC(String account, String password, String validCode, String validCodeReqNo) {
        AuthAccountPasswordLoginParam authAccountPasswordLoginParam = new AuthAccountPasswordLoginParam();
        authAccountPasswordLoginParam.setAccount(account);
        authAccountPasswordLoginParam.setPassword(password);
        authAccountPasswordLoginParam.setValidCode(validCode);
        authAccountPasswordLoginParam.setValidCodeReqNo(validCodeReqNo);
        return authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.C.getValue());
    }

    @Override
    public String doLoginByIdForB(String userId, String device) {
        return authService.doLoginById(userId, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(), SaClientTypeEnum.B.getValue());
    }

    @Override
    public String doLoginByIdForC(String userId, String device) {
        return authService.doLoginById(userId, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(), SaClientTypeEnum.C.getValue());
    }

    @Override
    public String doLoginByAccountForB(String account, String device) {
        return authService.doLoginByAccount(account, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(), SaClientTypeEnum.B.getValue());
    }

    @Override
    public String doLoginByAccountForC(String account, String device) {
        return authService.doLoginByAccount(account, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(), SaClientTypeEnum.C.getValue());
    }

    @Override
    public String doLoginByPhoneForB(String phone, String device) {
        return authService.doLoginByPhone(phone, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(),
                SaClientTypeEnum.B.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.AUTO_CREATE_USER.getValue());
    }

    @Override
    public String doLoginByPhoneForC(String phone, String device) {
        return authService.doLoginByPhone(phone, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(),
                SaClientTypeEnum.C.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.AUTO_CREATE_USER.getValue());
    }

    @Override
    public String doLoginByEmailForB(String email, String device) {
        return authService.doLoginByEmail(email, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(),
                SaClientTypeEnum.B.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.AUTO_CREATE_USER.getValue());
    }

    @Override
    public String doLoginByEmailForC(String email, String device) {
        return authService.doLoginByEmail(email, ObjectUtil.isNotEmpty(device)?device:AuthDeviceTypeEnum.PC.getValue(),
                SaClientTypeEnum.C.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.AUTO_CREATE_USER.getValue());
    }

    @Override
    public void removeThirdUserByUserIdList(List<String> userIdList) {
        authThirdService.deleteByUserIdList(userIdList);
    }
}
