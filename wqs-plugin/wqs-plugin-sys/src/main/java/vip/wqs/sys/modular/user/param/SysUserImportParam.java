
package vip.wqs.sys.modular.user.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户导入参数
 *
 * @author jetox
 * @date 2022/7/8 13:22
 **/
@Getter
@Setter
public class SysUserImportParam {

    /** 账号 */
    private String account;

    /** 姓名 */
    private String name;

    /** 组织名称 */
    private String orgName;

    /** 职位名称 */
    private String positionName;

    /** 手机 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 主管名称 */
    private String directorName;

    /** 员工编号 */
    private String empNo;

    /** 入职日期 */
    private String entryDate;

    /** 职级 */
    private String positionLevel;

    /** 昵称 */
    private String nickname;

    /** 性别 */
    private String gender;

    /** 出生日期 */
    private String birthday;

    /** 民族 */
    private String nation;

}
