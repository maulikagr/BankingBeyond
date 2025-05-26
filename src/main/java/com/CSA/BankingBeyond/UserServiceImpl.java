package com.CSA.BankingBeyond;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}
