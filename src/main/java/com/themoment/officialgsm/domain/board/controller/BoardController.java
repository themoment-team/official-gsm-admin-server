package com.themoment.officialgsm.domain.board.controller;

import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> postAdd(@RequestPart("content") AddPostRequest addPostRequest, @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage, @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        boardService.addPost(addPostRequest, bannerImage, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{postSeq}")
    public ResponseEntity<Void> postModify(@PathVariable Long postSeq, @RequestPart("content") ModifyPostRequest modifyPostRequest, @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage, @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        boardService.modifyPost(postSeq, modifyPostRequest, bannerImage, files);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{postSeq}")
    public ResponseEntity<Void> postDelete(@PathVariable Long postSeq) {
        boardService.removePost(postSeq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
