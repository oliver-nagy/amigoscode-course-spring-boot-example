package com.amigoscode.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/v1/customers")
    public List<Customer> getCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/api/v1/customers/{customerId}")
    public Customer getCustomerById(@PathVariable Integer customerId) {
        return customerService.findCustomerById(customerId);
    }
}
