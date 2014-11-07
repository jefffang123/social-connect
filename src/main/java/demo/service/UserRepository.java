package demo.service;

import demo.domain.User;
import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
