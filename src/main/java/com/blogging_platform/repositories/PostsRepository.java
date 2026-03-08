package com.blogging_platform.repositories;

import com.blogging_platform.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p where lower(p.title) LIKE lower(CONCAT('%', :term, '%')) or lower(p.content) LIKE lower(CONCAT('%', :term, '%')) or lower(p.category) like lower(CONCAT('%', :term, '%'))")
    List<Post> getPostsHavingTerm(@Param("term")String term);
}
