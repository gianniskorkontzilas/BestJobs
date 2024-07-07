package com.codingfactory.job.controller;

import com.codingfactory.job.model.Recruiter;
import com.codingfactory.job.model.Users;
import com.codingfactory.job.repository.UsersRepository;
import com.codingfactory.job.services.RecruiterService;
import com.codingfactory.job.services.exceptions.RecruiterAlreadyExistsException;
import com.codingfactory.job.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterController {

    private final UsersRepository usersRepository;
    private final RecruiterService recruiterService;

    @Autowired
    public RecruiterController(UsersRepository usersRepository, RecruiterService recruiterService) {
        this.usersRepository = usersRepository;
        this.recruiterService = recruiterService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not " + "found user"));
            Optional<Recruiter> recruiter = recruiterService.getOne(users.getUserId());

            if (!recruiter.isEmpty())
                model.addAttribute("profile", recruiter.get());

        }

        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(Recruiter recruiter, @RequestParam("image") MultipartFile multipartFile, Model model) throws RecruiterAlreadyExistsException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not " + "found user"));
            recruiter.setUserId(users);
            recruiter.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", recruiter);
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiter.setProfilePhoto(fileName);
        }
        Recruiter savedUser = recruiterService.addNew(recruiter);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/dashboard/";
    }
}






