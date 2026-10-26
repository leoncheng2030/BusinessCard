package vip.wqs.dev.modular.dict.service.impl;

import cn.hutool.core.lang.tree.Tree;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.dev.modular.dict.entity.DevDict;
import vip.wqs.dev.modular.dict.enums.DevDictCategoryEnum;
import vip.wqs.dev.modular.dict.param.DevDictAddParam;
import vip.wqs.dev.modular.dict.param.DevDictEditParam;
import vip.wqs.dev.modular.dict.param.DevDictIdParam;
import vip.wqs.dev.modular.dict.param.DevDictTreeParam;
import vip.wqs.dev.modular.dict.service.BizDictService;
import vip.wqs.dev.modular.dict.service.DevDictService;

import java.util.List;

/**
 * 业务字典服务实现（业务自助）
 *
 * <p>复用 DevDictService 的底层增删改查（重复校验/编码生成/翻译缓存刷新），
 * 在入口处追加 BIZ 链校验：仅 BIZ 分类链上的字典可操作，FRM 框架字典平台受控。</p>
 *
 * @author wqs
 * @date 2026/9/13 21:00
 */
@Service
public class BizDictServiceImpl implements BizDictService {

    /** 字典树根节点父id */
    private static final String ROOT_PARENT_ID = "0";

    /** 链深保护上限（防脏数据成环） */
    private static final int MAX_DEPTH = 10;

    @Resource
    private DevDictService devDictService;

    @Override
    public List<Tree<String>> tree() {
        DevDictTreeParam treeParam = new DevDictTreeParam();
        treeParam.setCategory(DevDictCategoryEnum.BIZ.getValue());
        return devDictService.tree(treeParam);
    }

    @Override
    public void add(DevDictAddParam devDictAddParam) {
        // 仅允许业务字典，分类强制 BIZ（防参数误传 FRM）
        devDictAddParam.setCategory(DevDictCategoryEnum.BIZ.getValue());
        if(!ROOT_PARENT_ID.equals(devDictAddParam.getParentId())) {
            // 挂到已有节点下：父节点必须在 BIZ 链上
            assertBizChain(devDictAddParam.getParentId());
        }
        devDictService.add(devDictAddParam);
    }

    @Override
    public void edit(DevDictEditParam devDictEditParam) {
        // 仅允许业务字典，分类强制 BIZ
        devDictEditParam.setCategory(DevDictCategoryEnum.BIZ.getValue());
        // 编辑目标必须在 BIZ 链上
        assertBizChain(devDictEditParam.getId());
        if(!ROOT_PARENT_ID.equals(devDictEditParam.getParentId())) {
            assertBizChain(devDictEditParam.getParentId());
        }
        devDictService.edit(devDictEditParam);
    }

    @Override
    public void delete(List<DevDictIdParam> devDictIdParamList) {
        for(DevDictIdParam idParam : devDictIdParamList) {
            // 删除目标必须在 BIZ 链上
            assertBizChain(idParam.getId());
            // 类型根存在子项时禁止删除（防子项悬空成孤儿数据）
            DevDict node = devDictService.queryEntity(idParam.getId());
            if(ROOT_PARENT_ID.equals(node.getParentId())) {
                long childCount = devDictService.count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DevDict>()
                        .eq(DevDict::getParentId, node.getId()));
                if(childCount > 0) {
                    throw new CommonException("业务字典类型[{}]下存在字典项，请先删除子项", node.getDictLabel());
                }
            }
        }
        devDictService.delete(devDictIdParamList);
    }

    /**
     * 断言字典节点属于 BIZ 分类链（沿 parentId 向上寻根，根节点分类必须为 BIZ）
     *
     * @param dictId 待校验字典id
     */
    private void assertBizChain(String dictId) {
        DevDict node = devDictService.queryEntity(dictId);
        DevDict root = node;
        int depth = 0;
        while(!ROOT_PARENT_ID.equals(root.getParentId())) {
            root = devDictService.queryEntity(root.getParentId());
            if(++depth > MAX_DEPTH) {
                throw new CommonException("字典层级非法，id值为：{}", dictId);
            }
        }
        if(!DevDictCategoryEnum.BIZ.getValue().equals(root.getCategory())) {
            throw new CommonException("仅允许操作业务字典，id值为：{}", dictId);
        }
    }
}
