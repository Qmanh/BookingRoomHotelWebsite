package com.data.websitehotel.service;

import com.data.websitehotel.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);

    List<User> getAllUsers();
    void deleteUser(String email);
    User getUser(String email);
}
