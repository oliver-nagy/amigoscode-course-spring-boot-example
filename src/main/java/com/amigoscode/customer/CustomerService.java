package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> findAllCustomers() {
        return customerDAO.selectAllCustomers();
    };

    public Customer findCustomerById(Integer id) {
        return customerDAO.selectCustomerById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer with id: [%s] not found".formatted(id))
        );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();

        if (customerDAO.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("Email is already taken.");
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );

        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDAO.existsPersonWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }
        customerDAO.deleteCustomerById(customerId);
    }


}
