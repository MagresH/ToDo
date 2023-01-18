package com.sample.todoproject.service;

import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {
    @Setter
    public static AppUser loggedUser;
    private final UserRepository userRepository;

    @Autowired
    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public AppUser addAppUser(AppUser appUser) {return userRepository.save(appUser);}
    public AppUser getAppUserByUsername(String username){
        return userRepository.findAll().stream()
                .filter(appUser -> appUser.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
