package glints.test.service;

import glints.test.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> findAll(Integer page);
    User findById(Integer id);
    Boolean delete(Integer userId);
    User save(User user);
}
