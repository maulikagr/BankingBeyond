package com.CSA.BankingBeyond;

public interface UserService {
    User findByUsername(String username);
    void save(User user);
}
