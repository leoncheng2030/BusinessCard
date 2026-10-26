
package vip.wqs.sys.modular.resource.enums;

import lombok.Getter;

/**
 * 菜单是否与
 *
 * @author yubaoshan
 * @date 2024/9/17 00:14
 **/
@Getter
public enum SysMenuWhetherEnum {

    /** 写入 */
    YES("YES"),

    /** 不写入 */
    NO("NO");

    private final String value;

    SysMenuWhetherEnum(String value) {
        this.value = value;
    }
}
