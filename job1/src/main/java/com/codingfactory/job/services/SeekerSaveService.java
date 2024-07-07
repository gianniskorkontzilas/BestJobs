package com.codingfactory.job.services;

import com.codingfactory.job.model.JobPostActivity;
import com.codingfactory.job.model.JobSeekerProfile;
import com.codingfactory.job.model.SeekerSave;
import com.codingfactory.job.repository.SeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeekerSaveService {

    private final SeekerSaveRepository seekerSaveRepository;

    public SeekerSaveService(SeekerSaveRepository seekerSaveRepository) {
        this.seekerSaveRepository = seekerSaveRepository;
    }

    public List<SeekerSave> getCandidatesJob(JobSeekerProfile userAccountId) {
        return seekerSaveRepository.findByUserId(userAccountId);
    }

    public List<SeekerSave> getJobCandidates(JobPostActivity job) {
        return seekerSaveRepository.findByJob(job);
    }

    public void addNew(SeekerSave seekerSave) {
        seekerSaveRepository.save(seekerSave);
    }
}
