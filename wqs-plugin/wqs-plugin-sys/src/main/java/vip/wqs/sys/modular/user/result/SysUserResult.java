
package vip.wqs.sys.modular.user.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户结果集
 *
 * @author jetox
 * @date 2022/7/8 13:22
 **/
@Getter
@Setter
public class SysUserResult {

    /** id */
    @Schema(description = "id")
    private String id;

    /** 头像 */
    @Schema(description = "头像")
    private String avatar;

    /** 签名 */
    @Schema(description = "签名")
    private String signature;

    /** 账号 */
    @Schema(description = "账号")
    private String account;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

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
    @Schema(description = "入职日期")
    private String entryDate;

    /** 组织id */
    @Schema(description = "组织id")
    private String orgId;

    /** 职位id */
    @Schema(description = "职位id")
    private String positionId;

    /** 职级 */
    @Schema(description = "职级")
    private String positionLevel;

    /** 主管id */
    @Schema(description = "主管id")
    private String directorId;

    /** 上次登录ip */
    @Schema(description = "上次登录ip")
    private String lastLoginIp;

    /** 上次登录地点 */
    @Schema(description = "上次登录地点")
    private String lastLoginAddress;

    /** 上次登录时间 */
    @Schema(description = "上次登录时间")
    private Date lastLoginTime;

    /** 上次登录设备 */
    @Schema(description = "上次登录设备")
    private String lastLoginDevice;

    /** 最新登录ip */
    @Schema(description = "最新登录ip")
    private String latestLoginIp;

    /** 最新登录地点 */
    @Schema(description = "最新登录地点")
    private String latestLoginAddress;

    /** 最新登录时间 */
    @Schema(description = "最新登录时间")
    private Date latestLoginTime;

    /** 最新登录设备 */
    @Schema(description = "最新登录设备")
    private String latestLoginDevice;

    /** 用户状态 */
    @Schema(description = "用户状态")
    private String userStatus;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /* ====额外的字段==== */

    /** 组织名称 */
    @Schema(description = "组织名称")
    private String orgName;

    /** 职位名称 */
    @Schema(description = "职位名称")
    private String positionName;

    /** 主管名称 */
    @Schema(description = "主管名称")
    private String directorName;
}
