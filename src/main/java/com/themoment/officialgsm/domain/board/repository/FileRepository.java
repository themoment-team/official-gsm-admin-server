package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {

    @Modifying
    @Query("DELETE FROM File f WHERE f.fileUrl IN :fileUrls")
    void deleteByFileUrls(@Param("fileUrls") List<String> fileUrls);
}
