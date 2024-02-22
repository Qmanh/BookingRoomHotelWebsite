package com.data.websitehotel.service;

import com.data.websitehotel.model.User;
import com.data.websitehotel.request.ChangePasswordRequests;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    User registerUser(User user);

    List<User> getAllUsers();
    void deleteUser(String email);
    User getUser(String email);
    void changePassword(ChangePasswordRequests request);
    String forgotPassword(String email);
}
