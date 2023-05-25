package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
