package com.CSA.BankingBeyond;

/*
 * This is the service class for the user.
 * It is used to find a user by username.
 * It is also used to save a user.
 */

public interface UserService {
    User findByUsername(String username);
    void save(User user);
}
