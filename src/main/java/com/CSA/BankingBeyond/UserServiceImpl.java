package com.CSA.BankingBeyond;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/*
 * This is the service class for the user.
 * It is used to find a user by username.
 * It is also used to save a user.
 */

@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> users = new HashMap<>();

    public UserServiceImpl() {
        users.put("user1", new User("user1", "password1"));
        users.put("user2", new User("user2", "password2"));
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public void save(User user) {
        if (user != null && user.getUsername() != null) {
            users.put(user.getUsername(), user);
        }
    }
}
