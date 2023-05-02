package com.themoment.officialgsm.domain.board.repository;

import com.themoment.officialgsm.domain.board.entity.file.File;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<File> fileList) {
        String sql = "INSERT INTO file (file_url, file_name, file_extension, post_seq) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                fileList,
                fileList.size(),
                (PreparedStatement ps, File file) -> {
                    ps.setString(1, file.getFileUrl());
                    ps.setString(2, file.getFileName());
                    ps.setString(3, file.getFileExtension().toString());
                    ps.setLong(4, file.getPost().getPostSeq());
                });
    }
}
