package com.judizi.base_api.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judizi.base_api.configuration.ApiConfiguration;
import com.judizi.base_api.controller.snippet.ApiResponseSnippet;
import com.judizi.base_api.model.dto.ResponseDto;
import com.judizi.base_api.model.response.ApiInformation;

@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ApiController.class)
@Import(ApiConfiguration.class)
class ApiControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.name}")
    String apiName;
    @Value("${api.version}")
    String apiVersion;

    String TAG = "";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    void getApi() throws Exception {
        // given
        ApiInformation expected = 
            ApiInformation.builder()
                .serviceName(apiName)
                .serviceVersion(apiVersion)
                .build();

        // when
        MvcResult mvcResult = mockMvc
                .perform(RestDocumentationRequestBuilders.get("/api"))
                .andDo(MockMvcRestDocumentationWrapper.document("get-api-information-api",
                        resource(
                            ResourceSnippetParameters.builder()
                                .summary("API 정보 조회")
                                .tag(TAG)
                                .responseSchema(Schema.schema("get-api-information-response"))
                                .responseFields(ApiResponseSnippet.getApiInformationFields())
                                .build())))
                .andDo(print())
                .andReturn();

        // then
        MockHttpServletResponse resultResponse = mvcResult.getResponse();
        assertThat(resultResponse.getStatus()).isEqualTo(200);
        assertThat(resultResponse.getHeader(HttpHeaders.CONTENT_TYPE))
                .contains(MediaType.APPLICATION_JSON_VALUE);

        ResponseDto<ApiInformation> responseDto =
                objectMapper.readValue(resultResponse.getContentAsString(),
                        new TypeReference<ResponseDto<ApiInformation>>() {});
        assertThat(responseDto.getData()).isEqualTo(expected);
    }
}
