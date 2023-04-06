package com.themoment.officialgsm.domain.board.controller;

import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> postAdd(@RequestPart("content") AddPostRequest addPostRequest, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        boardService.addPost(addPostRequest, multipartFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
