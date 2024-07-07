package com.codingfactory.job.services;

import com.codingfactory.job.model.Recruiter;
import com.codingfactory.job.model.Users;
import com.codingfactory.job.repository.RecruiterRepository;
import com.codingfactory.job.repository.UsersRepository;
import com.codingfactory.job.services.exceptions.RecruiterAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public RecruiterService(RecruiterRepository recruiterRepository, UsersRepository usersRepository) {
        this.recruiterRepository = recruiterRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<Recruiter> getOne(Integer id) throws Exception{
        return recruiterRepository.findById(id);
    }

    public Recruiter addNew(Recruiter recruiter) throws RecruiterAlreadyExistsException {
        return recruiterRepository.save(recruiter);
    }

    public Recruiter getCurrentRecruiterProfile() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<Recruiter> recruiter = getOne(users.getUserId());
            return recruiter.orElse(null);
        } else return null;
    }
}
