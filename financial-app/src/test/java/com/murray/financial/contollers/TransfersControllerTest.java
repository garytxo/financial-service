package com.murray.financial.contollers;

import com.murray.financial.config.TestConfig;
import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
import com.murray.financial.dtos.TransferDTO;
import com.murray.financial.dtos.TransferSearchCriteriaDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
@Sql("classpath:test-data.sql")
@Transactional
public class TransfersControllerTest extends AbstractControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Override
    TestRestTemplate restTemplate() {
        return restTemplate;
    }

    @Test
    public void read_with_no_filters_return_all_transfers_200(){

        TransferDTO transferDTO = createTestTransfer("EUR",BigDecimal.TEN);
        TransferDTO transferDTO2 = createTestTransfer("EUR",BigDecimal.TEN);


        TransferSearchCriteriaDTO dto = new TransferSearchCriteriaDTO();

        HttpEntity<TransferSearchCriteriaDTO> entity = getTransferSearchEntity(dto);

        ResponseEntity<Collection<TransferDTO>> responseEntity =
                restTemplate.exchange(getTransferUrl(), HttpMethod.GET, entity,
                        new ParameterizedTypeReference<Collection<TransferDTO>>() {
                });

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));

        assertThat(responseEntity.getBody().isEmpty(), is(equalTo(false)));

    }


    @Test
    public void create_fail_when_transfer_to_same_account_return_406(){

        BankAccountDTO eurAccount2 = createTestAccount("EUR");

        TransferDTO dto = new TransferDTO();
        dto.setSource(eurAccount2.getIbanNumber());
        dto.setDestination(eurAccount2.getIbanNumber());
        dto.setDescription("Test transfer");
        dto.setAmount(BigDecimal.TEN);

        HttpEntity<TransferDTO> entity = getTransferEntity(dto);

        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getTransferUrl(), HttpMethod.POST, entity,
                        Object.class);


        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void create_fails_invalid_account_return_406(){

        BankAccountDTO eurAccount2 = createTestAccount("EUR");

        TransferDTO dto = new TransferDTO();
        dto.setSource("NOT_EXIST");
        dto.setDestination(eurAccount2.getIbanNumber());
        dto.setDescription("Test transfer");
        dto.setAmount(BigDecimal.TEN);

        HttpEntity<TransferDTO> entity = getTransferEntity(dto);

        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getTransferUrl(), HttpMethod.POST, entity,
                        Object.class);


        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void create_transfer_return_201(){

        BankAccountDTO eurAccount1 = createTestAccount("EUR");
        BankAccountDTO eurAccount2 = createTestAccount("EUR");

        TransferDTO dto = new TransferDTO();
        dto.setSource(eurAccount1.getIbanNumber());
        dto.setDestination(eurAccount2.getIbanNumber());
        dto.setDescription("Test transfer");
        dto.setAmount(BigDecimal.TEN);

        HttpEntity<TransferDTO> entity = getTransferEntity(dto);

        ResponseEntity<TransferDTO> responseEntity =
                restTemplate.exchange(getTransferUrl(), HttpMethod.POST, entity, TransferDTO.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.CREATED)));

        assertThat(responseEntity.getBody().getSource(),
                is(equalTo(eurAccount1.getIbanNumber())));

        assertThat(responseEntity.getBody().getDestination(),
                is(equalTo(eurAccount2.getIbanNumber())));

        assertThat(responseEntity.getBody().getAmount(),is(equalTo(BigDecimal.TEN)));

    }


    HttpEntity<TransferSearchCriteriaDTO> getTransferSearchEntity(TransferSearchCriteriaDTO dto) {


        return new HttpEntity<>(dto, httpHeaders());
    }

}
