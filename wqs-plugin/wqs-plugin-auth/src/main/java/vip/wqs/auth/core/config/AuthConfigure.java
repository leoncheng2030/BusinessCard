
package vip.wqs.auth.core.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.strategy.SaAnnotationStrategy;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.core.util.StpClientLoginUserUtil;
import vip.wqs.auth.core.util.StpLoginUserUtil;

import java.util.List;

/**
 * SaToken鉴权配置
 *
 * @author jetox
 * @date 2021/10/9 14:24
 **/
@Configuration
public class AuthConfigure implements WebMvcConfigurer {

    /**
     * 注册Sa-Token的注解拦截器，打开注解式鉴权功能
     * <p>
     * 注解的方式有以下几中，注解既可以加在接口方法上，也可加在Controller类上：
     * 1.@SaCheckLogin: 登录认证 —— 只有登录之后才能进入该方法（常用）
     * 2.@SaCheckRole("admin"): 角色认证 —— 必须具有指定角色标识才能进入该方法（常用）
     * 3.@SaCheckPermission("user:add"): 权限认证 —— 必须具有指定权限才能进入该方法（常用）
     * 4.@SaCheckSafe: 二级认证校验 —— 必须二级认证之后才能进入该方法
     * 5.@SaCheckBasic: HttpBasic认证 —— 只有通过 Basic 认证后才能进入该方法
     * </p>
     * 在Controller中创建一个接口，默认不需要登录也不需要任何权限都可以访问的，只有加了上述注解才会校验
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关，只是说明哪些接口不需要被拦截器拦截，此处都拦截)
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Primary
    @Bean("stpLogic")
    public StpLogic getStpLogic(SaTokenConfig saTokenConfig) {
        // 重写Sa-Token的StpLogic，默认客户端类型为B
        return new StpLogic(SaClientTypeEnum.B.getValue()).setConfig(saTokenConfig);
    }

    @Bean("stpClientLogic")
    public StpLogic getStpClientLogic(SaTokenConfig saTokenConfig) {

        // 重写Sa-Token的StpLogic，默认客户端类型为C
        return new StpLogic(SaClientTypeEnum.C.getValue()).setConfig(saTokenConfig);
    }

    @SuppressWarnings("ALL")
    @Autowired
    public void rewriteSaStrategy() {
        // 重写Sa-Token的注解处理器，增加注解合并功能
        SaAnnotationStrategy.instance.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
    }

    /**
     * 权限认证接口实现类，集成权限认证功能
     *
     * @author jetox
     * @date 2022/7/7 16:16
     **/
    @Component
    public static class StpInterfaceImpl implements StpInterface {

        /**
         * 返回一个账号所拥有的权限码集合，直接从TokenSession读取
         */
        @Override
        public List<String> getPermissionList(Object loginId, String loginType) {
            if (SaClientTypeEnum.B.getValue().equals(loginType)) {
                Object loginUser = StpLoginUserUtil.getLoginUser();
                if(ObjectUtil.isEmpty(loginUser)) {
                    return CollectionUtil.newArrayList();
                }
                return StpLoginUserUtil.getLoginUser().getPermissionCodeList();
            } else {
                Object clientLoginUser = StpClientLoginUserUtil.getClientLoginUser();
                if(ObjectUtil.isEmpty(clientLoginUser)) {
                    return CollectionUtil.newArrayList();
                }
                return StpClientLoginUserUtil.getClientLoginUser().getPermissionCodeList();
            }
        }

        /**
         * 返回一个账号所拥有的角色标识集合
         */
        @Override
        public List<String> getRoleList(Object loginId, String loginType) {
            if (SaClientTypeEnum.B.getValue().equals(loginType)) {
                return StpLoginUserUtil.getLoginUser().getRoleCodeList();
            } else {
                return StpClientLoginUserUtil.getClientLoginUser().getRoleCodeList();
            }
        }
    }
}
