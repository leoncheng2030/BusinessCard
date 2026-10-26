
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户编辑参数
 *
 * @author jetox
 * @date 2022/7/26 15:43
 **/
@Getter
@Setter
public class SysUserEditParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    /** 账号 */
    @Schema(description = "账号")
    @NotBlank(message = "account不能为空")
    private String account;

    /** 姓名 */
    @Schema(description = "姓名")
    @NotBlank(message = "name不能为空")
    private String name;

    /** 组织id */
    @Schema(description = "组织id")
    @NotBlank(message = "orgId不能为空")
    private String orgId;

    /** 职位id */
    @Schema(description = "职位id")
    @NotBlank(message = "positionId不能为空")
    private String positionId;

    /** 职级 */
    @Schema(description = "职级")
    private String positionLevel;

    /** 主管id */
    @Schema(description = "主管id")
    private String directorId;

    /** 头像 */
    @Schema(description = "头像，图片base64")
    private String avatar;

    /** 签名 */
    @Schema(description = "签名，图片base64")
    private String signature;

    /** 昵称 */
    @Schema(description = "昵称")
    private String nickname;

    /** 性别 */
    @Schema(description = "性别")
    private String gender;

    /** 出生日期 */
    @Schema(description = "出生日期")
    private String birthday;

    /** 民族 */
    @Schema(description = "民族")
    private String nation;

    /** 手机 */
    @Schema(description = "手机")
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 员工编号 */
    @Schema(description = "员工编号")
    private String empNo;

    /** 入职日期 */
    @Schema(description = "员工编号")
    private String entryDate;

    /** 兼任信息 */
    @Schema(description = "兼任信息")
    private String positionJson;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

}
