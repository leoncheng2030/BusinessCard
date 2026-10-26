package vip.wqs.dev.modular.dict.service;

import cn.hutool.core.lang.tree.Tree;
import vip.wqs.dev.modular.dict.param.DevDictAddParam;
import vip.wqs.dev.modular.dict.param.DevDictEditParam;
import vip.wqs.dev.modular.dict.param.DevDictIdParam;

import java.util.List;

/**
 * 业务字典服务（业务自助）
 *
 * <p>BIZ 分类字典自助维护：
 * 仅允许操作 BIZ 分类链上的字典（类型根及字典项），FRM 框架字典平台受控不可操作。</p>
 *
 * @author wqs
 * @date 2026/9/13 21:00
 */
public interface BizDictService {

    /**
     * 获取业务字典树（仅 BIZ 分类）
     */
    List<Tree<String>> tree();

    /**
     * 添加业务字典（parentId=0 时自建 BIZ 类型根，否则挂到 BIZ 链节点下）
     */
    void add(DevDictAddParam devDictAddParam);

    /**
     * 编辑业务字典（仅 BIZ 链上的字典项）
     */
    void edit(DevDictEditParam devDictEditParam);

    /**
     * 删除业务字典（仅 BIZ 链上的字典项；类型根存在子项时禁止删除）
     */
    void delete(List<DevDictIdParam> devDictIdParamList);
}
