package com.amigoscode.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerListDataAccessServiceTest {

    private CustomerListDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerListDataAccessService();
    }

    @Test
    public void testUpdateCustomerShouldUpdateExistingCustomer() {
        Customer customer = new Customer(
                1,
                "Bober",
                "test@test.com",
                22
        );

        underTest.insertCustomer(customer);

        int expectedAge = 23;

        Customer updatedCustomer = new Customer(
                1,
                "Bober",
                "test@test.com",
                23
        );

        underTest.updateCustomer(updatedCustomer);

        assertEquals(1, underTest.selectAllCustomers().size());
        assertEquals(updatedCustomer, underTest.selectCustomerById(1).orElse(null));

    }
}