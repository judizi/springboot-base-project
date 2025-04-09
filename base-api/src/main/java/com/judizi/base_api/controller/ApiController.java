package com.judizi.base_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.judizi.base_api.configuration.ApiConfiguration;
import com.judizi.base_api.model.dto.ResponseDto;
import com.judizi.base_api.model.response.ApiInformation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiConfiguration apiConfiguration;

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> getApi() {
        ResponseDto<?> responseDto = 
            ResponseDto.builder()
                .code(200)
                .message("")
                .data(
                    ApiInformation.builder()
                        .serviceName(apiConfiguration.getName())
                        .serviceVersion(apiConfiguration.getVersion())
                        .build())
                .build();
        return ResponseEntity.ok().body(responseDto);
    }
}
