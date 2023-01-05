package com.urz.oproject.service;

import com.urz.oproject.model.AppUser;
import com.urz.oproject.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Setter
    public static AppUser loggedUser;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getUsers(){
        List<AppUser> appUsers = userRepository.findAll();
        return appUsers;
    }

    public AppUser addUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

}
