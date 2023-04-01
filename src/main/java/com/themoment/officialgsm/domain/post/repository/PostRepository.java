package com.themoment.officialgsm.domain.post.repository;

import com.themoment.officialgsm.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
