
package vip.wqs.sys.modular.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import vip.wqs.common.handler.CommonSm4CbcTypeHandler;
import vip.wqs.common.pojo.CommonEntity;
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.position.entity.SysPosition;

import java.util.Date;

/**
 * 用户实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName(value = "SYS_USER", autoResultMap = true)
public class SysUser extends CommonEntity {

    /** id */
    @TableId
    @Schema(description = "id")
    private String id;

    /** 头像 */
    @Schema(description = "头像")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String avatar;

    /** 签名 */
    @Schema(description = "签名")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String signature;

    /** 账号 */
    @Schema(description = "账号")
    private String account;

    /** 密码 */
    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 昵称 */
    @Schema(description = "昵称")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String nickname;

    /** 性别 */
    @Schema(description = "性别")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.DICTIONARY,key = "GENDER")
    private String gender;

    /** 出生日期 */
    @Schema(description = "出生日期")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String birthday;

    /** 民族 */
    @Schema(description = "民族")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String nation;

    /** 手机 */
    @Schema(description = "手机")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS, typeHandler = CommonSm4CbcTypeHandler.class)
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String email;

    /** 员工编号 */
    @Schema(description = "员工编号")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String empNo;

    /** 入职日期 */
    @Schema(description = "入职日期")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String entryDate;

    /** 组织id */
    @Schema(description = "组织id")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.SIMPLE, target = SysOrg.class, fields = "name", alias = "org", ref = "orgName")
    private String orgId;

    /** 职位id */
    @Schema(description = "职位id")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.SIMPLE, target = SysPosition.class, fields = "name", alias = "position", ref = "positionName")
    private String positionId;

    /** 职级 */
    @Schema(description = "职级")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String positionLevel;

    /** 主管id */
    @Schema(description = "主管id")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "name", alias = "director", ref = "directorName")
    private String directorId;

    /** 兼任信息 */
    @Schema(description = "兼任信息")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String positionJson;

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

    /** 组织名称 */
    @Schema(description = "组织名称")
    @TableField(exist = false)
    private String orgName;

    /** 职位名称 */
    @Schema(description = "职位名称")
    @TableField(exist = false)
    private String positionName;

    /** 主管名称 */
    @Schema(description = "主管名称")
    @TableField(exist = false)
    private String directorName;

}
