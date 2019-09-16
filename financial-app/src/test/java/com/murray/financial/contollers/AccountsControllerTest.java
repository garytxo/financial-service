package com.murray.financial.contollers;

import com.murray.financial.config.TestConfig;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
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
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
@Sql("classpath:test-data.sql")
@Transactional
public class AccountsControllerTest extends AbstractControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Override
    TestRestTemplate restTemplate() {
        return restTemplate;
    }

    @Test
    public void read_find_four_accounts_with_when_no_parameter_passed() throws Exception {

        BankAccountDTO eurAccount1 = createTestAccount("EUR");
        BankAccountDTO eurAccount2 = createTestAccount("EUR");
        BankAccountDTO gbpAccount1 = createTestAccount("GBP");
        BankAccountDTO gbpAccount2 = createTestAccount("GBP");


        BankAccountSearchCriteriaDTO searchCriteriaDTO = new BankAccountSearchCriteriaDTO();

        HttpEntity<BankAccountSearchCriteriaDTO> entity = getSearchEntity(searchCriteriaDTO);


        ResponseEntity<Collection<BankAccountDTO>> responseEntity =
                restTemplate.exchange(getAccountUrl(), HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<BankAccountDTO>>() {
                });


        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));


        assertThat(responseEntity.getBody().size(), is(equalTo(4)));


    }


    @Test
    public void delete_invalid_account_return_404() {


        HttpEntity<BankAccountDTO> entity = getEntity();

        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl("NO121212121"),
                        HttpMethod.DELETE, entity, Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_FOUND)));

    }

    @Test
    public void delete_valid_account_return_200() {

        BankAccountDTO newAccount = createTestAccount("EUR");

        HttpEntity<BankAccountDTO> entity = getEntity();

        ResponseEntity<BankAccountDTO> responseEntity =
                restTemplate.exchange(getAccountUrl(newAccount.getIbanNumber()), HttpMethod.DELETE, entity, BankAccountDTO.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));

    }

    @Test
    public void put_updates_status_currency_returns_200() {

        BankAccountDTO newAccount = createTestAccount("EUR");

        newAccount.setCurrency(AccountCurrency.USD.name());
        newAccount.setStatus(AccountStatus.DISABLED.name());

        HttpEntity<BankAccountDTO> entity = getAccountEntity(newAccount);


        ResponseEntity<BankAccountDTO> responseEntity =
                restTemplate.exchange(
                        getAccountUrl(newAccount.getIbanNumber()), HttpMethod.PUT,
                        entity, BankAccountDTO.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(responseEntity.getBody().getCurrency(), is(equalTo(AccountCurrency.USD.name())));
        assertThat(responseEntity.getBody().getBalance().longValue(), is(equalTo(newAccount.getBalance().longValue())));
        assertThat(responseEntity.getBody().getIbanNumber(), is(equalTo(newAccount.getIbanNumber())));
        assertThat(responseEntity.getBody().getStatus(), is(equalTo(AccountStatus.DISABLED.name())));
    }

    @Test
    public void put_updates_currency_invalid_return_406() {

        BankAccountDTO newAccount = createTestAccount("EUR");

        newAccount.setCurrency("AUD");

        HttpEntity<BankAccountDTO> entity = getAccountEntity(newAccount);


        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl(newAccount.getIbanNumber()), HttpMethod.PUT,
                        entity, Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void put_updates_fails_in_valid_ibanNumber_return_406() {

        BankAccountDTO newAccount = new BankAccountDTO();


        HttpEntity<BankAccountDTO> entity = getAccountEntity(newAccount);


        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl("UN12129102912012"), HttpMethod.PUT, entity,
                        Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void put_updates_status_invalid_return_406() {

        BankAccountDTO newAccount = createTestAccount("EUR");

        newAccount.setStatus("UNKNOWN");

        HttpEntity<BankAccountDTO> entity = getAccountEntity(newAccount);


        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl(newAccount.getIbanNumber()), HttpMethod.PUT, entity,
                        Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }


    @Test
    public void create_new_account_with_balance_returns_200() {

        BankAccountDTO dto = new BankAccountDTO();

        dto.setCurrency("EUR");
        dto.setBalance(BigDecimal.TEN);

        HttpEntity<BankAccountDTO> entity = getAccountEntity(dto);

        ResponseEntity<BankAccountDTO> responseEntity =
                restTemplate.exchange(getAccountUrl(), HttpMethod.POST, entity, BankAccountDTO.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(responseEntity.getBody().getCurrency(), is(equalTo(AccountCurrency.EUR.name())));
        assertThat(responseEntity.getBody().getBalance().longValue(), is(equalTo(BigDecimal.TEN.longValue())));
        assertThat(responseEntity.getBody().getIbanNumber(), is(notNullValue()));
        assertThat(responseEntity.getBody().getStatus(), is(equalTo(AccountStatus.ACTIVE.name())));

    }

    @Test
    public void create_new_account_returns_200() {


        BankAccountDTO dto = new BankAccountDTO();

        dto.setCurrency("GBP");

        HttpEntity<BankAccountDTO> entity = getAccountEntity(dto);

        ResponseEntity<BankAccountDTO> responseEntity =
                restTemplate.exchange(getAccountUrl(), HttpMethod.POST, entity, BankAccountDTO.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(responseEntity.getBody().getCurrency(), is(equalTo(AccountCurrency.GBP.name())));
        assertThat(responseEntity.getBody().getBalance().longValue(), is(equalTo(BigDecimal.ZERO.longValue())));
        assertThat(responseEntity.getBody().getIbanNumber(), is(notNullValue()));
        assertThat(responseEntity.getBody().getStatus(), is(equalTo(AccountStatus.ACTIVE.name())));

    }

    @Test
    public void create_return_406_error_when_no_currency_passed() {

        HttpEntity<BankAccountDTO> entity = getAccountEntity(new BankAccountDTO());

        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl(), HttpMethod.POST, entity,
                        Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    @Test
    public void create_return_406_error_when_invalid_currency() {

        BankAccountDTO dto = new BankAccountDTO();

        dto.setCurrency("AUD");

        HttpEntity<BankAccountDTO> entity = getAccountEntity(dto);

        ResponseEntity<?> responseEntity =
                restTemplate.exchange(getAccountUrl(), HttpMethod.POST,
                        entity, Object.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.NOT_ACCEPTABLE)));

    }

    HttpEntity<BankAccountSearchCriteriaDTO> getSearchEntity(BankAccountSearchCriteriaDTO dto) {


        return new HttpEntity<>(dto, httpHeaders());
    }


}
