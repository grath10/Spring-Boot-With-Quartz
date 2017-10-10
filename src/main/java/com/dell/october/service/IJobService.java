package com.dell.october.service;

import com.dell.october.entity.JobAndTrigger;
import com.github.pagehelper.PageInfo;

public interface IJobService {
    PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
