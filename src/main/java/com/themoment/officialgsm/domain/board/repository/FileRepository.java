package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
