package com.codingfactory.job.repository;

import com.codingfactory.job.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
}