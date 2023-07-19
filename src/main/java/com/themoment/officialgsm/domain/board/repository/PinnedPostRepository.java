package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.entity.post.PinnedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostRepository extends JpaRepository<PinnedPost, Long> {
}
