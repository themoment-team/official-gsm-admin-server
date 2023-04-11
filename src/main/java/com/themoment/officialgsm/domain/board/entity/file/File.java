package com.themoment.officialgsm.domain.board.entity.file;

import com.themoment.officialgsm.domain.board.entity.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;

    private String fileUrl;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileExtension fileExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq")
    private Post post;
}
