package com.codingfactory.job.controller;

import com.codingfactory.job.model.JobPostActivity;
import com.codingfactory.job.model.JobSeekerProfile;
import com.codingfactory.job.model.SeekerSave;
import com.codingfactory.job.model.Users;
import com.codingfactory.job.services.JobPostActivityService;
import com.codingfactory.job.services.JobSeekerProfileService;
import com.codingfactory.job.services.SeekerSaveService;
import com.codingfactory.job.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final SeekerSaveService seekerSaveService;

    public SeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, SeekerSaveService seekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.seekerSaveService = seekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, SeekerSave seekerSave) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                seekerSave.setJob(jobPostActivity);
                seekerSave.setUserId(seekerProfile.get());
            } else {
                throw new RuntimeException("User not found");
            }
            seekerSaveService.addNew(seekerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {

        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUserProfile();

        List<SeekerSave> seekerSaveList = seekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for (SeekerSave seekerSave : seekerSaveList) {
            jobPost.add(seekerSave.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }
}
