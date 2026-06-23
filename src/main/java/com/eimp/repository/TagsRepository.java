package com.eimp.repository;

import com.eimp.entity.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<TagsEntity,Long> {
}
