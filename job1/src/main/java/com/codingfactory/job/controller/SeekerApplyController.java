package com.codingfactory.job.controller;

import com.codingfactory.job.model.*;
import com.codingfactory.job.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class SeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final SeekerApplyService seekerApplyService;
    private final SeekerSaveService seekerSaveService;
    private final RecruiterService recruiterService;
    private final JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    public SeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, SeekerApplyService seekerApplyService, SeekerSaveService seekerSaveService, RecruiterService recruiterService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.seekerApplyService = seekerApplyService;
        this.seekerSaveService = seekerSaveService;
        this.recruiterService = recruiterService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) throws Exception {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        List<SeekerApply> seekerApplyList = seekerApplyService.getJobCandidates(jobDetails);
        List<SeekerSave> seekerSaveList = seekerSaveService.getJobCandidates(jobDetails);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                Recruiter user = recruiterService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", seekerApplyList);
                }
            } else {
                JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;
                    for (SeekerApply seekerApply : seekerApplyList) {
                        if (seekerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            exists = true;
                            break;
                        }
                    }
                    for (SeekerSave seekerSave : seekerSaveList) {
                        if (seekerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }

        SeekerApply jobSeekerApply = new SeekerApply();
        model.addAttribute("applyJob", jobSeekerApply);



        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, SeekerApply seekerApply) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                seekerApply.setUserId(seekerProfile.get());
                seekerApply.setJob(jobPostActivity);
                seekerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            seekerApplyService.addNew(seekerApply);
        }

        return "redirect:/dashboard/";
    }
}
