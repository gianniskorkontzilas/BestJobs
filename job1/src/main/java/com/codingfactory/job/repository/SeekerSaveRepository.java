package com.codingfactory.job.repository;


import com.codingfactory.job.model.JobPostActivity;
import com.codingfactory.job.model.JobSeekerProfile;
import com.codingfactory.job.model.SeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeekerSaveRepository extends JpaRepository<SeekerSave, Integer> {

    public List<SeekerSave> findByUserId(JobSeekerProfile userAccountId);

    List<SeekerSave> findByJob(JobPostActivity job);

}