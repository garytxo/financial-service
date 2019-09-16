package com.murray.financial.domain.repository.query;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.querybuilder.Operator;
import com.murray.financial.querybuilder.QueryCondition;
import com.murray.financial.querybuilder.QueryField;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.murray.financial.domain.repository.query.BankAccountQueryField.NATIVE_QUERY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class BankAccountSearchTest {


    private BankAccountSearch bankAccountSearch;

    private QueryCondition ibanNumberCondition = new QueryCondition(BankAccountQueryField.IBAN_NUMBER,
            Operator.EQUALS, "DK5750510001322123");

    private QueryCondition ibanNumberConditionTwo = new QueryCondition(BankAccountQueryField.IBAN_NUMBER,
            Operator.EQUALS, "DK5750510001322145");

    private QueryCondition balanceCondition = new QueryCondition(BankAccountQueryField.BALANCE,
            Operator.EQUALS, BigDecimal.ONE);

    @Before
    public void setup() {

        bankAccountSearch = new BankAccountSearch();
    }

    @Test
    public void return_native_query_with_no_where_condition() {

        assertThat(bankAccountSearch.toNativeQueryWithConditions(), is(notNullValue()));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), startsWith(bankAccountSearch.nativeQuery()));

    }

    @Test
    public void return_native_query_with_two_where_conditions() {

        assertThat(bankAccountSearch.toNativeQueryWithConditions(), is(notNullValue()));
        bankAccountSearch.addCondition(ibanNumberCondition);
        bankAccountSearch.addCondition(ibanNumberConditionTwo);
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), startsWith(bankAccountSearch.nativeQuery()));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("WHERE"));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("iban_number=:iban_number"));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("AND"));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("iban_number=:iban_number AND iban_number=:iban_number"));

    }

    @Test
    public void return_native_query_with_one_where_condition() {

        assertThat(bankAccountSearch.toNativeQueryWithConditions(), is(notNullValue()));
        bankAccountSearch.addCondition(ibanNumberCondition);
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), startsWith(bankAccountSearch.nativeQuery()));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("WHERE"));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("iban_number=:iban_number"));

    }

    @Test
    public void return_native_query_with_one_have_condition() {

        assertThat(bankAccountSearch.toNativeQueryWithConditions(), is(notNullValue()));
        bankAccountSearch.addCondition(balanceCondition);
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), startsWith(bankAccountSearch.nativeQuery()));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("HAVING"));
        assertThat(bankAccountSearch.toNativeQueryWithConditions(), containsString("balance=:balance"));
        System.out.println(bankAccountSearch.toNativeQueryWithConditions());

    }

    @Test(expected = IllegalArgumentException.class)
    public void throw_error_if_query_condition_field_not_valid_field() {

        QueryCondition fakeQueryCondition =
                new QueryCondition(TestQueryField.TEST_FIELD, Operator.EQUALS, "DK5750510001322123");

        bankAccountSearch.addCondition(fakeQueryCondition);

    }

    @Test
    public void return_one_query_conditions_when_add_valid_condition() {


        bankAccountSearch.addCondition(ibanNumberCondition);

        assertThat(bankAccountSearch.queryConditions().size(), is(1));

    }

    @Test
    public void return_one_query_conditions_when_add_valid_condition_twice_with_same_value() {

        bankAccountSearch.addCondition(ibanNumberCondition);
        bankAccountSearch.addCondition(ibanNumberCondition);

        assertThat(bankAccountSearch.queryConditions().size(), is(1));

    }


    @Test
    public void return_the_bank_account_entity_class() {


        assertThat(bankAccountSearch.getEntityClass(), is(equalTo(BankAccount.class)));

    }

    @Test
    public void return_the_bank_account_result_class() {


        assertThat(bankAccountSearch.getSearchResult(), is(equalTo(BankAccountResult.class)));

    }

    @Test
    public void return_native_query() {

        assertThat(bankAccountSearch.nativeQuery(), is(equalTo(NATIVE_QUERY)));

    }


    static class TestQueryField<T extends Comparable> extends QueryField {


        public static TestQueryField<String> TEST_FIELD =
                new TestQueryField<>("foo -> bar", "test_field",
                        "test_field","b.id");

        public TestQueryField(String name, String sqlColumnName, String conditionClause, String groupByColumnName) {
            super(name, sqlColumnName, conditionClause, groupByColumnName);
        }
    }


}
