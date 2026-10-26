
package vip.wqs.client.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户添加参数
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
public class ClientUserAddParam {

    /** 账号 */
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "account不能为空")
    private String account;

    /** 姓名 */
    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "name不能为空")
    private String name;

    /** 密码 */
    @Schema(description = "密码")
    private String password;

    /** 头像 */
    @Schema(description = "头像，图片base64")
    private String avatar;

    /** 昵称 */
    @Schema(description = "昵称")
    private String nickname;

    /** 性别 */
    @Schema(description = "性别")
    private String gender;

    /** 年龄 */
    @Schema(description = "年龄")
    private String age;

    /** 出生日期 */
    @Schema(description = "出生日期")
    private String birthday;

    /** 手机 */
    @Schema(description = "手机")
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

}
