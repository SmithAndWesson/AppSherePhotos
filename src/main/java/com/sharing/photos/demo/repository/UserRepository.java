package com.sharing.photos.demo.repository;

import com.sharing.photos.demo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByUsername(String userName);
  Optional<User> findUserByEmail(String email);
  Optional<User> findUserById(Long id);

}
