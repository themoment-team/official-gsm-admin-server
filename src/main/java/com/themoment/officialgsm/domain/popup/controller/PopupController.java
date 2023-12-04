package com.themoment.officialgsm.domain.popup.controller;

import com.themoment.officialgsm.domain.popup.dto.AddPopupRequest;
import com.themoment.officialgsm.domain.popup.service.PopupService;
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

@RestController
@RequestMapping("/api/popup")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @PostMapping
    @Operation(
            summary = "팝업 등록 요청",
            description = "팝업을 등록하는 api",
            tags = {"Popup Controller"}
    )
    public ResponseEntity<Void> popupAdd(
            @Parameter(
                    name = "info", description = "popup의 링크와 만료기간 - form-data", in = ParameterIn.PATH
            )
            @RequestPart(value = "info", required = true) AddPopupRequest addPopupRequest,
            @Parameter(
                    name = "image", description = "popup의 image - form-data", in = ParameterIn.PATH,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        popupService.addPopup(addPopupRequest, image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
