package com.pseudocritics.database;

import com.pseudocritics.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailOrUserName(String email, String username);

    @Query("SELECT u FROM User u WHERE u.userName = :emailOrUserName OR u.email = :emailOrUserName")
    User getUserByEmailOrUserName(String emailOrUserName);

}