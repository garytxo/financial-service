package com.murray.financial.contollers;

import com.murray.financial.config.TestConfig;
import com.murray.financial.dtos.TransferDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
@Sql("classpath:test-data.sql")
@Transactional
public class ExecuteTransferControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Override
    TestRestTemplate restTemplate() {
        return restTemplate;
    }

    @Test
    public void return_error_code_when_not_find_transfer() {


        HttpEntity<TransferDTO>  entity = new HttpEntity<>( httpHeaders());;

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(getExecuteTransferUrl("111111"), HttpMethod.PUT, entity, String.class);


        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void return_transfer_after_exceuting(){

        TransferDTO transferDTO = createTestTransfer("EUR",BigDecimal.TEN);
        assertThat(transferDTO.getExecutionTime(),is(nullValue()));


        HttpEntity<TransferDTO>  entity = new HttpEntity<>( httpHeaders());;
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(getExecuteTransferUrl(transferDTO.getId().toString()),
                        HttpMethod.PUT, entity, String.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));


    }

    String getExecuteTransferUrl(String transferId) {

        UriTemplate template = new UriTemplate("/Execute-Transfer/" + transferId);

        return restTemplate().getRestTemplate().getUriTemplateHandler().expand(template.toString()).toString();
    }

    String getExecuteTransferUrl() {

        UriTemplate template = new UriTemplate("/Execute-Transfer");

        return restTemplate().getRestTemplate().getUriTemplateHandler().expand(template.toString()).toString();
    }
}
