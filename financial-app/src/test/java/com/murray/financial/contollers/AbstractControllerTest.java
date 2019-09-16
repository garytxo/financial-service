package com.murray.financial.contollers;

import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
import com.murray.financial.dtos.TransferDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic functionality that can be used among the differenct controllers
 */
abstract class AbstractControllerTest {


    /**
     * Defines the {@link TestRestTemplate}
     * @return
     */
    abstract TestRestTemplate restTemplate();


    /**
     * Create a test transfer using the API calls
     * @param destCurrency currency defined of the destination account
     * @param amount amount to transfer
     * @return TransferDTO
     */
    TransferDTO createTestTransfer(String destCurrency, BigDecimal amount){

        BankAccountDTO eurAccount1 = createTestAccount("EUR");
        BankAccountDTO eurAccount2 = createTestAccount(destCurrency);

        TransferDTO dto = new TransferDTO();
        dto.setSource(eurAccount1.getIbanNumber());
        dto.setDestination(eurAccount2.getIbanNumber());
        dto.setDescription("Test transfer of :"+amount.toString());
        dto.setAmount(amount);

        HttpEntity<TransferDTO> entity = getTransferEntity(dto);

        return       restTemplate().exchange(getTransferUrl(),
                HttpMethod.POST, entity, TransferDTO.class).getBody();

    }



    /**
     * Create a test account using the rest api
     * @param currency
     * @return
     */
    BankAccountDTO createTestAccount(String currency) {


        BankAccountDTO dto = new BankAccountDTO();
        dto.setCurrency(currency);
        dto.setBalance(BigDecimal.TEN);
        HttpEntity<BankAccountDTO> entity = getAccountEntity(dto);

        ResponseEntity<BankAccountDTO> responseEntity =
                restTemplate().exchange(getAccountUrl(), HttpMethod.POST, entity, BankAccountDTO.class);

        return responseEntity.getBody();
    }


    String getAccountUrl() {

        UriTemplate template = new UriTemplate("/Account");

        return restTemplate().getRestTemplate().getUriTemplateHandler().expand(template.toString()).toString();
    }

    String getTransferUrl() {

        UriTemplate template = new UriTemplate("/Transfer");

        return restTemplate().getRestTemplate().getUriTemplateHandler().expand(template.toString()).toString();
    }

    HttpEntity getEntity() {

        return new HttpEntity(httpHeaders());
    }

    String getAccountUrl(String requestParameter) {

        UriTemplate template = new UriTemplate("/Account/".concat(requestParameter));

        return restTemplate().getRestTemplate().getUriTemplateHandler().expand(template.toString()).toString();
    }




    HttpEntity<BankAccountDTO> getAccountEntity(BankAccountDTO dto) {

        return new HttpEntity<>(dto, httpHeaders());
    }

    HttpEntity<TransferDTO> getTransferEntity(TransferDTO dto) {

        return new HttpEntity<>(dto, httpHeaders());
    }


    HttpHeaders httpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes());
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    private List<MediaType> acceptableMediaTypes() {

        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        return acceptableMediaTypes;

    }

}
