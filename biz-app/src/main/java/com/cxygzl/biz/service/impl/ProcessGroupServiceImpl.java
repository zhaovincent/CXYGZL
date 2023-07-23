package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.mapper.ProcessGroupMapper;
import com.cxygzl.biz.service.IProcessGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.common.dto.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-25
 */
@Service
public class ProcessGroupServiceImpl extends ServiceImpl<ProcessGroupMapper, ProcessGroup> implements IProcessGroupService {

    /**
     * 组列表
     *
     * @return
     */
    @Override
    public R<List<ProcessGroup>> queryList() {
        List<ProcessGroup> processGroupList = this.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

        return com.cxygzl.common.dto.R.success(processGroupList);
    }

    /**
     * 新增表单分组
     *
     * @param processGroup 分组名
     * @return 添加结果
     */
    @Override
    public R create(ProcessGroup processGroup) {
        ProcessGroup pg = new ProcessGroup();
        pg.setSort(0);
        pg.setGroupName(processGroup.getGroupName());

        this.save(pg);
        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 删除分组
     *
     * @param id
     * @return
     */
    @Override
    public R delete(long id) {
        this.removeById(id);
        return com.cxygzl.common.dto.R.success();
    }
}
