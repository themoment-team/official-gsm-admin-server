package com.themoment.officialgsm.domain.post.domain;

import com.themoment.officialgsm.global.entity.BaseTimeEntity;
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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postSeq;

    private String postTitle;

    private String postContent;

    private String postWriter;

    @Enumerated(EnumType.STRING)
    private Category category;

}
