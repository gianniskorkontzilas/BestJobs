package com.codingfactory.job.repository;

import com.codingfactory.job.model.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {
}
