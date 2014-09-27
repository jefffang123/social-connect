package demo.service;

import demo.domain.User;
import org.springframework.data.repository.Repository;

interface UserRepository extends Repository<User, Long> {

    User findByUsername(String username);
}
