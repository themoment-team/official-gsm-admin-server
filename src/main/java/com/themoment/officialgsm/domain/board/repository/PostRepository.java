package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
