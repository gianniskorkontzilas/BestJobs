package com.codingfactory.job.repository;

import com.codingfactory.job.model.JobPostActivity;
import com.codingfactory.job.model.JobSeekerProfile;
import com.codingfactory.job.model.SeekerApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeekerApplyRepository extends JpaRepository<SeekerApply, Integer> {

    List<SeekerApply> findByUserId(JobSeekerProfile userId);

    List<SeekerApply> findByJob(JobPostActivity job);
}
