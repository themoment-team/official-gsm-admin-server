package com.themoment.officialgsm.domain.board.controller;

import com.themoment.officialgsm.domain.board.dto.request.AddPostRequest;
import com.themoment.officialgsm.domain.board.dto.request.ModifyPostRequest;
import com.themoment.officialgsm.domain.board.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "게시물 등록 요청",
            description = "게시물을 등록하는 api",
            tags = {"Board Controller"}
    )
    public ResponseEntity<Void> postAdd(
            @Parameter(
                    name = "content", description = "post의 content - form-data", in = ParameterIn.PATH
            )
            @RequestPart("content") AddPostRequest addPostRequest,
            @Parameter(
                    name = "file", description = "post의 file - form-data", in = ParameterIn.PATH,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        boardService.addPost(addPostRequest, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{postSeq}")
    @Operation(
            summary = "게시물 수정 요청",
            description = "게시물을 수정하는 api",
            tags = {"Board Controller"}
    )
    public ResponseEntity<Void> postModify(
            @Parameter(name = "postSeq", description = "post의 seq값", in = ParameterIn.PATH)
            @PathVariable Long postSeq,
            @Parameter(
                    name = "content", description = "post의 content - form-data", in = ParameterIn.PATH
            )
            @RequestPart("content") ModifyPostRequest modifyPostRequest,
            @Parameter(
                    name = "file", description = "post의 file - form-data", in = ParameterIn.PATH,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        boardService.modifyPost(postSeq, modifyPostRequest, files);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{postSeq}")
    @Operation(
            summary = "게시물 삭제 요청",
            description = "게시물을 삭제하는 api",
            tags = {"Board Controller"}
    )
    public ResponseEntity<Void> postDelete(
            @Parameter(name = "postSeq", description = "post의 seq값", in = ParameterIn.PATH)
            @PathVariable Long postSeq
    ) {
        boardService.removePost(postSeq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
