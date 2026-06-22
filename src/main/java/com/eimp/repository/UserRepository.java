package com.eimp.repository;

import com.eimp.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u " +
            "JOIN FETCH u.userProfile p " +
            "WHERE u.id = :id")
    UsersEntity findUserWithProfile(@Param("id") Long id);

    @Query("SELECT u FROM UsersEntity u LEFT JOIN FETCH u.userProfile")
    List<UsersEntity> findAllUsersWithProfiles();

    Optional<UsersEntity> findByEmail(String username);

    boolean existsByEmail(String username);


}
