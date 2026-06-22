package com.eimp.repository;

import com.eimp.entity.UserProfilesEntity;
import com.eimp.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfilesEntity, Long> {

    Optional<UserProfilesEntity> findByUserId(Long userId);



}
