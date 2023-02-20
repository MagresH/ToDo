package com.sample.todoproject.service;

import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Setter
    public static AppUser loggedUser;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public AppUser addAppUser(AppUser appUser) {return userRepository.save(appUser);}
    public AppUser getAppUserByUsername(String username){
        return userRepository.findAppUserByUsername(username)
                .orElse(null);
    }
}
