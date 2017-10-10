package com.dell.october.mapper;

import com.dell.october.entity.JobAndTrigger;

import java.util.List;

public interface JobMapper {
    List<JobAndTrigger> getJobDetails();
}
