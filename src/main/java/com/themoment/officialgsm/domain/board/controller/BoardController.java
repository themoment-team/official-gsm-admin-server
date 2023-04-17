package com.themoment.officialgsm.domain.board.controller;

import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.dto.response.PostListResponse;
import com.themoment.officialgsm.domain.board.entity.post.Category;
import com.themoment.officialgsm.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<Page<PostListResponse>> postList(@RequestParam int pageNumber, @RequestParam Category category) {
        Page<PostListResponse> postList = boardService.findPosts(pageNumber, category);
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> postAdd(@RequestPart("content") AddPostRequest addPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        boardService.addPost(addPostRequest, multipartFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{postSeq}")
    public ResponseEntity<Void> postModify(@PathVariable Long postSeq, @RequestPart("content") ModifyPostRequest modifyPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        boardService.modifyPost(postSeq, modifyPostRequest, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{postSeq}")
    public ResponseEntity<Void> postDelete(@PathVariable Long postSeq) {
        boardService.removePost(postSeq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
