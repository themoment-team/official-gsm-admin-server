package com.themoment.officialgsm.domain.board.entity.post;

import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.board.entity.file.File;
import com.themoment.officialgsm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<File> files = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    public void update(String postTitle, String postContent, Category category) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.category = category;
    }

}
