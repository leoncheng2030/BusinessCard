
package vip.wqs.client.modular.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.wqs.client.modular.user.param.*;
import vip.wqs.client.modular.user.result.ClientUserPicValidCodeResult;
import vip.wqs.client.modular.user.service.ClientUserService;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;

/**
 * C端用户个人控制器
 *
 * @author jetox
 * @date 2022/4/22 9:34
 **/
@Tag(name = "C端用户个人控制器")
@RestController
@Validated
public class ClientUserCenterController {

    @Resource
    private ClientUserService clientUserService;

    /**
     * 获取图片验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "获取图片验证码")
    @GetMapping("/client/userCenter/getPicCaptcha")
    public CommonResult<ClientUserPicValidCodeResult> getPicCaptcha() {
        return CommonResult.data(clientUserService.getPicCaptcha());
    }

    /**
     * 找回密码获取手机验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "找回密码获取手机验证码")
    @GetMapping("/client/userCenter/findPasswordGetPhoneValidCode")
    public CommonResult<String> findPasswordGetPhoneValidCode(@Valid ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam) {
        return CommonResult.data(clientUserService.findPasswordGetPhoneValidCode(clientUserGetPhoneValidCodeParam));
    }

    /**
     * 找回密码获取邮箱验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "找回密码获取邮箱验证码")
    @GetMapping("/client/userCenter/findPasswordGetEmailValidCode")
    public CommonResult<String> findPasswordGetEmailValidCode(@Valid ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam) {
        return CommonResult.data(clientUserService.findPasswordGetEmailValidCode(clientUserGetEmailValidCodeParam));
    }

    /**
     * 通过手机号找回用户密码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "通过手机号找回用户密码")
    @CommonLog("通过手机号找回用户密码")
    @PostMapping("/client/userCenter/findPasswordByPhone")
    public CommonResult<String> findPasswordByPhone(@RequestBody @Valid ClientUserFindPwdByPhoneParam clientUserFindPwdByPhoneParam) {
        clientUserService.findPasswordByPhone(clientUserFindPwdByPhoneParam);
        return CommonResult.ok();
    }

    /**
     * 通过邮箱找回用户密码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "通过邮箱找回用户密码")
    @CommonLog("通过邮箱找回用户密码")
    @PostMapping("/client/userCenter/findPasswordByEmail")
    public CommonResult<String> findPasswordByEmail(@RequestBody @Valid ClientUserFindPwdByEmailParam clientUserFindPwdByEmailParam) {
        clientUserService.findPasswordByEmail(clientUserFindPwdByEmailParam);
        return CommonResult.ok();
    }

    /**
     * 修改密码获取手机验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "修改密码获取手机验证码")
    @GetMapping("/client/userCenter/updatePasswordGetPhoneValidCode")
    public CommonResult<String> updatePasswordGetPhoneValidCode(@Valid ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam) {
        return CommonResult.data(clientUserService.updatePasswordGetPhoneValidCode(clientUserGetPhoneValidCodeParam));
    }

    /**
     * 修改密码获取邮箱验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "修改密码获取邮箱验证码")
    @GetMapping("/client/userCenter/updatePasswordGetEmailValidCode")
    public CommonResult<String> updatePasswordGetEmailValidCode(@Valid ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam) {
        return CommonResult.data(clientUserService.updatePasswordGetEmailValidCode(clientUserGetEmailValidCodeParam));
    }

    /**
     * 通过验证旧密码修改用户密码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "通过验证旧密码修改用户密码")
    @CommonLog("通过验证旧密码修改用户密码")
    @PostMapping("/client/userCenter/updatePasswordByOld")
    public CommonResult<String> updatePasswordByOld(@RequestBody @Valid ClientUserUpdatePwdByOldParam clientUserUpdatePwdByOldParam) {
        clientUserService.updatePasswordByOld(clientUserUpdatePwdByOldParam);
        return CommonResult.ok();
    }

    /**
     * 通过验证手机号修改用户密码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "通过验证手机号修改用户密码")
    @CommonLog("通过验证手机号修改用户密码")
    @PostMapping("/client/userCenter/updatePasswordByPhone")
    public CommonResult<String> updatePasswordByPhone(@RequestBody @Valid ClientUserUpdatePwdByPhoneParam clientUserUpdatePwdByPhoneParam) {
        clientUserService.updatePasswordByPhone(clientUserUpdatePwdByPhoneParam);
        return CommonResult.ok();
    }

    /**
     * 通过验证邮箱修改用户密码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "通过验证邮箱修改用户密码")
    @CommonLog("通过验证邮箱修改用户密码")
    @PostMapping("/client/userCenter/updatePasswordByEmail")
    public CommonResult<String> updatePasswordByEmail(@RequestBody @Valid ClientUserUpdatePwdByEmailParam clientUserUpdatePwdByEmailParam) {
        clientUserService.updatePasswordByEmail(clientUserUpdatePwdByEmailParam);
        return CommonResult.ok();
    }

    /**
     * 绑定手机号获取手机验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "绑定手机号获取手机验证码")
    @GetMapping("/client/userCenter/bindPhoneGetPhoneValidCode")
    public CommonResult<String> bindPhoneGetPhoneValidCode(@Valid ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam) {
        return CommonResult.data(clientUserService.bindPhoneGetPhoneValidCode(clientUserGetPhoneValidCodeParam));
    }

    /**
     * 修改绑定手机号获取手机验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "修改绑定手机号获取手机验证码")
    @GetMapping("/client/userCenter/updateBindPhoneGetPhoneValidCode")
    public CommonResult<String> updateBindPhoneGetPhoneValidCode(@Valid ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam) {
        return CommonResult.data(clientUserService.updateBindPhoneGetPhoneValidCode(clientUserGetPhoneValidCodeParam));
    }

    /**
     * 绑定手机号
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "绑定手机号")
    @CommonLog("绑定手机号")
    @PostMapping("/client/userCenter/bindPhone")
    public CommonResult<String> bindPhone(@RequestBody @Valid ClientUserBindPhoneParam clientUserBindPhoneParam) {
        clientUserService.bindPhone(clientUserBindPhoneParam);
        return CommonResult.ok();
    }

    /**
     * 绑定邮箱获取邮箱验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "绑定邮箱获取邮箱验证码")
    @GetMapping("/client/userCenter/bindEmailGetEmailValidCode")
    public CommonResult<String> bindEmailGetEmailValidCode(@Valid ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam) {
        return CommonResult.data(clientUserService.bindEmailGetEmailValidCode(clientUserGetEmailValidCodeParam));
    }

    /**
     * 修改绑定邮箱获取邮箱验证码
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "修改绑定邮箱获取邮箱验证码")
    @GetMapping("/client/userCenter/updateBindEmailGetEmailValidCode")
    public CommonResult<String> updateBindEmailGetEmailValidCode(@Valid ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam) {
        return CommonResult.data(clientUserService.updateBindEmailGetEmailValidCode(clientUserGetEmailValidCodeParam));
    }

    /**
     * 绑定邮箱
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "绑定邮箱")
    @CommonLog("绑定邮箱")
    @PostMapping("/client/userCenter/bindEmail")
    public CommonResult<String> bindEmail(@RequestBody @Valid ClientUserBindEmailParam clientUserBindEmailParam) {
        clientUserService.bindEmail(clientUserBindEmailParam);
        return CommonResult.ok();
    }

    /**
     * 修改用户头像
     *
     * @author jetox
     * @date 2021/10/13 14:01
     **/
    @Operation(summary = "修改用户头像")
    @CommonLog("修改用户头像")
    @PostMapping("/client/userCenter/updateAvatar")
    public CommonResult<String> updateAvatar(@RequestPart("file") MultipartFile file) {
        return CommonResult.data(clientUserService.updateAvatar(file));
    }

    /**
     * 编辑个人信息
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑个人信息")
    @CommonLog("编辑个人信息")
    @PostMapping("/client/userCenter/updateUserInfo")
    public CommonResult<String> updateUserInfo(@RequestBody @Valid ClientUserUpdateInfoParam clientUserUpdateInfoParam) {
        clientUserService.updateUserInfo(clientUserUpdateInfoParam);
        return CommonResult.ok();
    }

    /**
     * 根据id获取头像
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "根据id获取头像")
    @GetMapping("/client/userCenter/getAvatarById")
    public CommonResult<String> getAvatarById(@Valid ClientUserIdParam clientUserIdParam) {
        return CommonResult.data(clientUserService.getAvatarById(clientUserIdParam));
    }

    /**
     * 判断当前用户是否需要绑定手机号
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "判断当前用户是否需要绑定手机号")
    @GetMapping("/client/userCenter/isUserNeedBindPhone")
    public CommonResult<Boolean> isUserNeedBindPhone() {
        return CommonResult.data(clientUserService.isUserNeedBindPhone());
    }

    /**
     * 判断当前用户是否需要绑定邮箱
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "判断当前用户是否需要绑定邮箱")
    @GetMapping("/client/userCenter/isUserNeedBindEmail")
    public CommonResult<Boolean> isUserNeedBindEmail() {
        return CommonResult.data(clientUserService.isUserNeedBindEmail());
    }

    /**
     * 判断当前用户密码是否过期
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "判断当前用户密码是否过期")
    @GetMapping("/client/userCenter/isUserPasswordExpired")
    public CommonResult<Boolean> isUserPasswordExpired() {
        return CommonResult.data(clientUserService.isUserPasswordExpired());
    }
}
