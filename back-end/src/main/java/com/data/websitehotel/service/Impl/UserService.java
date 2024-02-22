package com.data.websitehotel.service.Impl;

import com.data.websitehotel.exception.IllegalStateException;
import com.data.websitehotel.exception.UserAlreadyExistsException;
import com.data.websitehotel.model.Role;
import com.data.websitehotel.model.User;
import com.data.websitehotel.repository.RoleRepository;
import com.data.websitehotel.repository.UserRepository;
import com.data.websitehotel.request.ChangePasswordRequests;
import com.data.websitehotel.security.user.HotelUserDetails;
import com.data.websitehotel.service.IUserService;
import com.data.websitehotel.utils.Email;
import com.data.websitehotel.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;


    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + "already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if(theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void changePassword(ChangePasswordRequests request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var user = (HotelUserDetails) auth.getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(),user.getPassword())){
            throw new IllegalStateException("Wrong password");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }


       User updateUser= userRepository.findByEmail(user.getEmail()).get();
       updateUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
       userRepository.save(updateUser);
    }

    @Override
    public String forgotPassword(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(
                () -> new RuntimeException("User not found with this email: "+ email));

            String newPassword = RandomStringGenerator.generateRandomString();

            User user = userRepository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(newPassword));


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(Email.writeSubject(user.getFirstName()+""+user.getLastName()));
            message.setText(Email.writeEmailSendPassword(newPassword));

            mailSender.send(message);
            userRepository.save(user);

        return "Please check your email to set new password";
    }
}
