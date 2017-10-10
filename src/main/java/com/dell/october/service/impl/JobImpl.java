package com.dell.october.service.impl;

import com.dell.october.entity.JobAndTrigger;
import com.dell.october.mapper.JobMapper;
import com.dell.october.service.IJobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JobImpl implements IJobService{
    @Autowired
    private JobMapper jobMapper;

    @Override
    public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobAndTrigger> list = jobMapper.getJobDetails();
        PageInfo<JobAndTrigger> page = new PageInfo<JobAndTrigger>(list);
        return page;
    }
}
