package glints.test.service;

import glints.test.model.User;
import glints.test.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo repo;

    @Override
    public Page<User> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<User> userPages = repo.findAll(pageable);
        if(userPages.getContent().size() > 0){
            return userPages;
        }
        return null;
    }

    @Override
    public User findById(Integer id) {
        try {
            Optional<User> userOptonal = repo.findById(id);
            if(userOptonal.isPresent()){
                return userOptonal.get();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean delete(Integer userId) {
        if(repo.existsById(userId)) {
            repo.deleteById(userId);
            if (!repo.existsById(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User save(User user) {
        repo.save(user);
        if(repo.existsById(user.getId())){
            return user;
        }
        return null;
    }
}
