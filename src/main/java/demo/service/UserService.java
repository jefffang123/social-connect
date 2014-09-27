package demo.service;

import demo.domain.User;

public interface UserService {

    User findByUsername(String username);
}
