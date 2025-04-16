package com.judizi.base_api.controller.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class ApiResponseSnippet {
    public static List<FieldDescriptor> getApiInformationFields() {
        return List.of(
                fieldWithPath("code").description("어플리케이션 http status code"),
                fieldWithPath("message").description("어플리케이션 에러 메세지"),
                fieldWithPath("data").description("결과 데이터"),
                fieldWithPath("data.serviceName").description("서비스 명"),
                fieldWithPath("data.serviceVersion").description("서비스 버전"));
    }
}
