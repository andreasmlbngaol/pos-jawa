package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;

import java.util.List;


public class Admin extends User {

    public Admin(
        Long id,
        String username,
        String name,
        boolean mustChangePassword
    ) {
        super(id, username, name, Role.Admin, mustChangePassword);
    }

    public Admin(Users user) {
        super(user.getId(), user.getUsername(), user.getName(), Role.Admin, user.isMustChangePassword());
    }

    public String resetPasswordAndGetOtp(Long userId) {
        return UserRepository.resetPasswordAndGetOtp(userId);
    }

    public void softDelete(Long userId) {
        UserRepository.softDelete(userId);
    }

    public List<User> getAllUsers() {
        return UserRepository.getAllUsers();
    }

    public void changeName(Long userId, String newName) {
        UserRepository.changeName(userId, newName);
    }

    public String addCashierAndGetOtp(String username, String name) {
        return UserRepository.addCashierAndGetOtp(username, name);
    }
}
