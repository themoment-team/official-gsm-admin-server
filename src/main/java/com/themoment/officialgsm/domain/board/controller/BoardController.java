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
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<Void> postAdd(@RequestBody AddPostRequest addPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        boardService.addPost(addPostRequest, multipartFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/modify/{postSeq}")
    public ResponseEntity<Void> postModify(@PathVariable Long postSeq, @RequestBody ModifyPostRequest modifyPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        boardService.modifyPost(postSeq, modifyPostRequest, multipartFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{boardSeq}")
    public ResponseEntity<Void> postDelete(@PathVariable Long boardSeq) {
        boardService.removePost(boardSeq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
