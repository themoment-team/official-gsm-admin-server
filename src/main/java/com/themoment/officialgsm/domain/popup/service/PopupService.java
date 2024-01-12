package com.themoment.officialgsm.domain.popup.service;

import com.themoment.officialgsm.domain.board.dto.FileDto;
import com.themoment.officialgsm.domain.popup.dto.AddPopupRequest;
import com.themoment.officialgsm.domain.popup.entity.Popup;
import com.themoment.officialgsm.domain.popup.repository.PopupRepository;
import com.themoment.officialgsm.global.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private final AwsS3Util awsS3Util;

    @Transactional
    public void addPopup(AddPopupRequest addPopupRequest, MultipartFile image) {
        FileDto fileDto = awsS3Util.fileUploadProcess(List.of(image)).get(0);
        Popup popup = Popup.builder()
                .popupImageUrl(fileDto.getFileUrl())
                .popupTitle(addPopupRequest.getTitle())
                .popupLink(addPopupRequest.getLink())
                .popupExpirationDateTime(addPopupRequest.getExpirationDateTime())
                .build();

        popupRepository.save(popup);
    }
}
