package com.codingfactory.job.services;

import com.codingfactory.job.model.JobPostActivity;
import com.codingfactory.job.model.SeekerApply;
import com.codingfactory.job.model.JobSeekerProfile;
import com.codingfactory.job.repository.SeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeekerApplyService {

    private final SeekerApplyRepository seekerApplyRepository;

    @Autowired
    public SeekerApplyService(SeekerApplyRepository seekerApplyRepository) {
        this.seekerApplyRepository = seekerApplyRepository;
    }

    public List<SeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId) {
        return seekerApplyRepository.findByUserId(userAccountId);
    }

    public List<SeekerApply> getJobCandidates(JobPostActivity job) {
        return seekerApplyRepository.findByJob(job);
    }

    public void addNew(SeekerApply seekerApply) {
        seekerApplyRepository.save(seekerApply);
    }
}
