package glints.test.repo;

import glints.test.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo  extends CrudRepository<User,Integer>{

    @Query(value = "SELECT * FROM USER ", countQuery = "SELECT count(*) FROM USER",  nativeQuery = true)
    Page<User> findAll(Pageable pageable);

}
