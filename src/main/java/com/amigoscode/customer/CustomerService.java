package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
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


    public void updateCustomerById(
            Integer customerId,
            CustomerUpdateRequest customerUpdateRequest
    ) {
        String newName = customerUpdateRequest.name();
        String newEmail = customerUpdateRequest.email();
        Integer newAge = customerUpdateRequest.age();

        Customer customer = findCustomerById(customerId);

        boolean changes = false;

        if (newName != null && !customer.getName().equals(newName)) {
            customer.setName(newName);
            changes = true;
        }

        if (newAge != null && customer.getAge() == (newAge)) {
            customer.setAge(newAge);
            changes = true;
        }

        if (newEmail != null && !customer.getEmail().equals(newEmail)) {
            if (customerDAO.existsPersonWithEmail(newEmail)) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(newEmail);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found.");
        }

        customerDAO.updateCustomer(customer);
    }
}
