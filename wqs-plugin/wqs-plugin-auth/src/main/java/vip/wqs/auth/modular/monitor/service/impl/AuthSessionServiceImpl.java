
package vip.wqs.auth.modular.monitor.service.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.auth.api.SaBaseLoginUserApi;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.auth.modular.monitor.param.AuthExitSessionParam;
import vip.wqs.auth.modular.monitor.param.AuthExitTokenParam;
import vip.wqs.auth.modular.monitor.param.AuthSessionPageParam;
import vip.wqs.auth.modular.monitor.result.AuthSessionAnalysisResult;
import vip.wqs.auth.modular.monitor.result.AuthSessionPageResult;
import vip.wqs.auth.modular.monitor.service.AuthSessionService;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.common.util.CommonTimeFormatUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会话治理Service接口实现类
 *
 * @author jetox
 * @date 2022/6/24 22:25
 */
@Service
public class AuthSessionServiceImpl implements AuthSessionService {

    @Resource(name = "loginUserApi")
    private SaBaseLoginUserApi loginUserApi;

    @Resource(name = "clientLoginUserApi")
    private SaBaseLoginUserApi clientLoginUserApi;

    @Override
    public AuthSessionAnalysisResult analysis() {
        AuthSessionAnalysisResult authSessionAnalysisResult = new AuthSessionAnalysisResult();
        List<JSONObject> sessionListB = StpUtil.searchSessionId("", -1, -1, true).stream().map(sessionId -> {
            JSONObject jsonObject = JSONUtil.createObj();
            String userId = StrUtil.split(sessionId, StrUtil.COLON).get(3);
            SaSession saSession = StpUtil.getSessionByLoginId(userId, false);
            if (ObjectUtil.isNotEmpty(saSession)) {
                int tokenCount = saSession.getTerminalList().size();
                long createTime = saSession.getCreateTime();
                jsonObject.set("userId", userId);
                jsonObject.set("tokenCount", tokenCount);
                jsonObject.set("createTime", DateTime.of(createTime));
                return jsonObject;
            }
            return null;
        }).filter(ObjectUtil::isNotNull).toList();

        List<JSONObject> sessionListC = StpClientUtil.searchSessionId("", -1, -1, true).stream().map(sessionId -> {
            JSONObject jsonObject = JSONUtil.createObj();
            String userId = StrUtil.split(sessionId, StrUtil.COLON).get(3);
            SaSession saSession = StpClientUtil.getSessionByLoginId(userId, false);
            if (ObjectUtil.isNotEmpty(saSession)) {
                int tokenCount = saSession.getTerminalList().size();
                long createTime = saSession.getCreateTime();
                jsonObject.set("userId", userId);
                jsonObject.set("tokenCount", tokenCount);
                jsonObject.set("createTime", DateTime.of(createTime));
                return jsonObject;
            }
            return null;
        }).filter(ObjectUtil::isNotNull).toList();

        List<Integer> tokenCountList = CollectionUtil.newArrayList();
        tokenCountList.addAll(sessionListB.stream().map(jsonObject -> jsonObject.getInt("tokenCount")).toList());
        tokenCountList.addAll(sessionListC.stream().map(jsonObject -> jsonObject.getInt("tokenCount")).toList());
        CollectionUtil.sort(tokenCountList, Comparator.comparingInt(Integer::intValue));
        int currentSessionTotalCount = sessionListB.size() + sessionListC.size();
        authSessionAnalysisResult.setCurrentSessionTotalCount(Convert.toStr(currentSessionTotalCount));
        authSessionAnalysisResult.setMaxTokenCount(Convert.toStr(tokenCountList.get(tokenCountList.size() - 1)));
        List<Date> sessionCreateTimeList = CollectionUtil.newArrayList();
        sessionCreateTimeList.addAll(sessionListB.stream().map(jsonObject -> jsonObject.getDate("createTime")).toList());
        sessionCreateTimeList.addAll(sessionListC.stream().map(jsonObject -> jsonObject.getDate("createTime")).toList());
        DateTime oneHourAgo = DateUtil.offset(DateTime.now(), DateField.HOUR, -1);
        authSessionAnalysisResult.setOneHourNewlyAdded(Convert.toStr(sessionCreateTimeList.stream().filter(date -> DateUtil.compare(oneHourAgo, date) <= 0).count()));
        authSessionAnalysisResult.setProportionOfBAndC(sessionListB.size() + StrUtil.SLASH + sessionListC.size());
        return authSessionAnalysisResult;
    }

    @Override
    public Page<AuthSessionPageResult> pageForB(AuthSessionPageParam authSessionPageParam) {
        Page<AuthSessionPageResult> defaultPage = CommonPageRequest.defaultPage();
        long current = defaultPage.getCurrent();
        int total = StpUtil.searchSessionId("", -1, Convert.toInt(defaultPage.getSize()), true).size();
        if (ObjectUtil.isNotEmpty(total)) {
            defaultPage = new Page<>(current, defaultPage.getSize(), total);
            String keyword = "";
            if (ObjectUtil.isNotEmpty(authSessionPageParam.getUserId())) {
                keyword = authSessionPageParam.getUserId();
            }
            List<String> userIdList = StpUtil.searchSessionId(keyword,
                    Convert.toInt((current - 1) * defaultPage.getSize()),
                    Convert.toInt(defaultPage.getSize()), true).stream().map(sessionId ->
                    StrUtil.split(sessionId, StrUtil.COLON).get(3)).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(userIdList)) {
                List<JSONObject> userList = loginUserApi.listUserByUserIdList(userIdList);
                List<AuthSessionPageResult> authSessionPageResultList = userIdList.stream().map(userId -> {
                    JSONObject userJsonObject = userList.stream().filter(u -> u.getStr("id").equals(userId)).findFirst().orElse(null);
                    SaSession saSession = StpUtil.getSessionByLoginId(userId, false);
                    if (ObjectUtil.isEmpty(saSession)) {
                        return null;
                    }
                    AuthSessionPageResult authSessionPageResult;
                    if (ObjectUtil.isNotEmpty(userJsonObject)) {
                        authSessionPageResult = JSONUtil.toBean(userJsonObject, AuthSessionPageResult.class);
                    } else {
                        authSessionPageResult = new AuthSessionPageResult();
                        authSessionPageResult.setId(userId);
                        authSessionPageResult.setAccount("已删除用户");
                        authSessionPageResult.setName("已删除用户");
                    }
                    authSessionPageResult.setSessionId(saSession.getId());
                    authSessionPageResult.setSessionCreateTime(DateTime.of(saSession.getCreateTime()));
                    long sessionTimeOut = saSession.timeout();
                    if (sessionTimeOut == -1) {
                        authSessionPageResult.setSessionTimeout("永久");
                    } else {
                        authSessionPageResult.setSessionTimeout(CommonTimeFormatUtil.formatSeconds(sessionTimeOut));
                    }
                    List<AuthSessionPageResult.TokenSignInfo> tokenInfoList = saSession.getTerminalList().stream()
                            .filter(terminalInfo -> {
                                long tokenTimeout = SaManager.getSaTokenDao().getTimeout(StpUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()));
                                return tokenTimeout != -2;  // 过滤掉tokenTimeout为-2的元素
                            })
                            .map(terminalInfo -> {
                                AuthSessionPageResult.TokenSignInfo terminalInfoInfo = new AuthSessionPageResult.TokenSignInfo();
                                terminalInfoInfo.setTokenValue(terminalInfo.getTokenValue());
                                terminalInfoInfo.setTokenDevice(terminalInfo.getDeviceType());
                                long tokenTimeout = SaManager.getSaTokenDao().getTimeout(StpUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()));
                                long tokenTimeoutConfig = StpUtil.stpLogic.getConfigOrGlobal().getTimeout();
                                if (tokenTimeout == -1) {
                                    terminalInfoInfo.setTokenTimeout("永久");
                                    terminalInfoInfo.setTokenTimeoutPercent(100d);
                                } else {
                                    terminalInfoInfo.setTokenTimeout(CommonTimeFormatUtil.formatSeconds(SaManager.getSaTokenDao()
                                            .getTimeout(StpUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()))));
                                    if (tokenTimeoutConfig == -1) {
                                        terminalInfoInfo.setTokenTimeoutPercent(0d);
                                    } else {
                                        terminalInfoInfo.setTokenTimeoutPercent(NumberUtil.div(tokenTimeout, tokenTimeoutConfig));
                                    }
                                }
                                return terminalInfoInfo;
                            })
                            .collect(Collectors.toList());
                    authSessionPageResult.setTokenCount(tokenInfoList.size());
                    authSessionPageResult.setTokenSignList(tokenInfoList);
                    return authSessionPageResult;
                }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
                defaultPage.setRecords(authSessionPageResultList);
            }
        }
        return defaultPage;
    }

    @Override
    public Page<AuthSessionPageResult> pageForC(AuthSessionPageParam authSessionPageParam) {
        Page<AuthSessionPageResult> defaultPage = CommonPageRequest.defaultPage();
        long current = defaultPage.getCurrent();
        int total = StpClientUtil.searchSessionId("", -1, Convert.toInt(defaultPage.getSize()), true).size();
        if (ObjectUtil.isNotEmpty(total)) {
            defaultPage = new Page<>(current, defaultPage.getSize(), total);
            String keyword = "";
            if (ObjectUtil.isNotEmpty(authSessionPageParam.getUserId())) {
                keyword = authSessionPageParam.getUserId();
            }
            List<String> userIdList = StpClientUtil.searchSessionId(keyword,
                    Convert.toInt((current - 1) * defaultPage.getSize()),
                    Convert.toInt(defaultPage.getSize()), true).stream().map(sessionId ->
                    StrUtil.split(sessionId, StrUtil.COLON).get(3)).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(userIdList)) {
                List<JSONObject> userList = clientLoginUserApi.listUserByUserIdList(userIdList);
                List<AuthSessionPageResult> authSessionPageResultList = userIdList.stream().map(userId -> {
                    JSONObject userJsonObject = userList.stream().filter(u -> u.getStr("id").equals(userId)).findFirst().orElse(null);
                    SaSession saSession = StpClientUtil.getSessionByLoginId(userId, false);
                    if (ObjectUtil.isEmpty(saSession)) {
                        return null;
                    }
                    AuthSessionPageResult authSessionPageResult;
                    if (ObjectUtil.isNotEmpty(userJsonObject)) {
                        authSessionPageResult = JSONUtil.toBean(userJsonObject, AuthSessionPageResult.class);
                    } else {
                        authSessionPageResult = new AuthSessionPageResult();
                        authSessionPageResult.setId(userId);
                        authSessionPageResult.setAccount("已删除用户");
                        authSessionPageResult.setName("已删除用户");
                    }
                    authSessionPageResult.setSessionId(saSession.getId());
                    authSessionPageResult.setSessionCreateTime(DateTime.of(saSession.getCreateTime()));
                    long sessionTimeOut = saSession.timeout();
                    if (sessionTimeOut == -1) {
                        authSessionPageResult.setSessionTimeout("永久");
                    } else {
                        authSessionPageResult.setSessionTimeout(CommonTimeFormatUtil.formatSeconds(sessionTimeOut));
                    }
                    List<AuthSessionPageResult.TokenSignInfo> tokenInfoList = saSession.getTerminalList().stream().filter(terminalInfo -> {
                        long tokenTimeout = SaManager.getSaTokenDao().getTimeout(StpClientUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()));
                        return tokenTimeout != -2;  // 过滤掉tokenTimeout为-2的元素
                    }).map(terminalInfo -> {
                        AuthSessionPageResult.TokenSignInfo terminalInfoInfo = new AuthSessionPageResult.TokenSignInfo();
                        terminalInfoInfo.setTokenValue(terminalInfo.getTokenValue());
                        terminalInfoInfo.setTokenDevice(terminalInfo.getDeviceType());
                        long tokenTimeout = SaManager.getSaTokenDao().getTimeout(StpClientUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()));
                        long tokenTimeoutConfig = StpClientUtil.stpLogic.getConfigOrGlobal().getTimeout();
                        if (tokenTimeout == -1) {
                            terminalInfoInfo.setTokenTimeout("永久");
                            terminalInfoInfo.setTokenTimeoutPercent(100d);
                        } else {
                            terminalInfoInfo.setTokenTimeout(CommonTimeFormatUtil.formatSeconds(SaManager.getSaTokenDao()
                                    .getTimeout(StpClientUtil.stpLogic.splicingKeyTokenValue(terminalInfo.getTokenValue()))));
                            if (tokenTimeoutConfig == -1) {
                                terminalInfoInfo.setTokenTimeoutPercent(0d);
                            } else {
                                terminalInfoInfo.setTokenTimeoutPercent(NumberUtil.div(tokenTimeout, tokenTimeoutConfig));
                            }
                        }
                        return terminalInfoInfo;
                    }).collect(Collectors.toList());
                    authSessionPageResult.setTokenCount(tokenInfoList.size());
                    authSessionPageResult.setTokenSignList(tokenInfoList);
                    return authSessionPageResult;
                }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
                defaultPage.setRecords(authSessionPageResultList);
            }
        }
        return defaultPage;
    }

    @Override
    public void exitSessionForB(List<AuthExitSessionParam> authExitSessionParamList) {
        authExitSessionParamList.forEach(authExitSessionParam -> StpUtil.logout(authExitSessionParam.getUserId()));
    }

    @Override
    public void exitSessionForC(List<AuthExitSessionParam> authExitSessionParamList) {
        authExitSessionParamList.forEach(authExitSessionParam -> StpClientUtil.logout(authExitSessionParam.getUserId()));
    }

    @Override
    public void exitTokenForB(List<AuthExitTokenParam> authExitTokenParamList) {
        authExitTokenParamList.forEach(authExitTokenParam -> StpUtil.logoutByTokenValue(authExitTokenParam.getTokenValue()));
    }

    @Override
    public void exitTokenForC(List<AuthExitTokenParam> authExitTokenParamList) {
        authExitTokenParamList.forEach(authExitTokenParam -> StpClientUtil.logoutByTokenValue(authExitTokenParam.getTokenValue()));
    }
}
