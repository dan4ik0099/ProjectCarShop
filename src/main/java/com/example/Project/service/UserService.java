package com.example.Project.service;

import com.example.Project.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);

}
