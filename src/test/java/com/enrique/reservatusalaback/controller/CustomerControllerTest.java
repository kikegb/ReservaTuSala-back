package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Customer;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.service.CustomerService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @DisplayName("POST add customer")
    @Test
    public void whenAddNewCustomer_ThenReturnOkAndCustomerWithId() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Customer customerNoId = new Customer(
                customer.getCnif(),
                customer.getName(),
                customer.getPhone(),
                customer.getPassword(),
                customer.getEmail()
        );
        doReturn(customer).when(customerService).add(customerNoId);

        this.mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(customer.getId())))
                .andExpect(jsonPath("$.cnif", is(customer.getCnif())))
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.phone", is(customer.getPhone())))
                .andExpect(jsonPath("$.password", is(customer.getPassword())))
                .andExpect(jsonPath("$.email", is(customer.getEmail())))
                .andExpect(jsonPath("$.deleted", is(customer.isDeleted())));
    }

    @DisplayName("POST add already existent customer")
    @Test
    public void whenAddExistentCustomer_ThenReturnConflictAndAlreadyExistentUserError() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        doReturn(null).when(customerService).add(customer);

        this.mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code", is(2)))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description",
                        is("User conflict: There is already a user with that email or cif/nif.")));

    }

    @DisplayName("POST add customer empty CIF/NIF")
    @Test
    public void whenAddNewCustomerWithNoCifOrNif_ThenReturnBadRequestAndError() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        JSONObject object = new JSONObject();
        object.put("name", customer.getName());
        object.put("phone", customer.getPhone());
        object.put("password", customer.getPassword());
        object.put("email", customer.getEmail());

        this.mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.cnif", is("CIF or NIF is required")));
    }

    @DisplayName("POST add business short phone")
    @Test
    public void whenAddNewCustomerWithWrongPhone_ThenReturnBadRequestAndError() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        JSONObject object = new JSONObject();
        object.put("cnif", customer.getCnif());
        object.put("name", customer.getName());
        object.put("phone", "1234567");
        object.put("password", customer.getPassword());
        object.put("email", customer.getEmail());

        this.mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.phone", is("Phone must be 9 characters long")));
    }

    @DisplayName("GET all customers")
    @Test
    public void whenFindAllCustomers_ThenReturnOkAndListOfCustomers() throws Exception {
        List<Customer> customers = mockGenerator.objects(Customer.class, 5).toList();
        doReturn(customers).when(customerService).findAll();

        this.mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(customers.get(0).getId())))
                .andExpect(jsonPath("$[0].cnif", is(customers.get(0).getCnif())))
                .andExpect(jsonPath("$[0].name", is(customers.get(0).getName())))
                .andExpect(jsonPath("$[0].phone", is(customers.get(0).getPhone())))
                .andExpect(jsonPath("$[0].password", is(customers.get(0).getPassword())))
                .andExpect(jsonPath("$[0].email", is(customers.get(0).getEmail())))
                .andExpect(jsonPath("$[0].deleted", is(customers.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(customers.get(4).getId())))
                .andExpect(jsonPath("$[4].cnif", is(customers.get(4).getCnif())))
                .andExpect(jsonPath("$[4].name", is(customers.get(4).getName())))
                .andExpect(jsonPath("$[4].phone", is(customers.get(4).getPhone())))
                .andExpect(jsonPath("$[4].password", is(customers.get(4).getPassword())))
                .andExpect(jsonPath("$[4].email", is(customers.get(4).getEmail())))
                .andExpect(jsonPath("$[4].deleted", is(customers.get(4).isDeleted())));
    }

    @DisplayName("GET customer by valid id")
    @Test
    public void whenFindCustomerByValidId_ThenReturnOkAndCustomerWithThatId() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        doReturn(customer).when(customerService).findById(customer.getId());

        this.mockMvc.perform(get("/customer/byId")
                        .param("id", customer.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId())))
                .andExpect(jsonPath("$.cnif", is(customer.getCnif())))
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.phone", is(customer.getPhone())))
                .andExpect(jsonPath("$.password", is(customer.getPassword())))
                .andExpect(jsonPath("$.email", is(customer.getEmail())))
                .andExpect(jsonPath("$.deleted", is(customer.isDeleted())));
    }

    @DisplayName("GET customer by invalid id")
    @Test
    public void whenFindCustomerByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        doReturn(null).when(customerService).findById(customer.getId());

        this.mockMvc.perform(get("/customer/byId")
                        .param("id", customer.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update customer valid id")
    @Test
    public void whenUpdateCustomerWithValidId_ThenReturnOkAndUpdatedCustomer() throws Exception {
        Customer updatedCustomer = mockGenerator.nextObject(Customer.class);
        doReturn(updatedCustomer).when(customerService).update(any(Customer.class));

        this.mockMvc.perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCustomer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedCustomer.getId())))
                .andExpect(jsonPath("$.cnif", is(updatedCustomer.getCnif())))
                .andExpect(jsonPath("$.name", is(updatedCustomer.getName())))
                .andExpect(jsonPath("$.phone", is(updatedCustomer.getPhone())))
                .andExpect(jsonPath("$.password", is(updatedCustomer.getPassword())))
                .andExpect(jsonPath("$.email", is(updatedCustomer.getEmail())))
                .andExpect(jsonPath("$.deleted", is(updatedCustomer.isDeleted())));
    }

    @DisplayName("PUT update customer invalid id")
    @Test
    public void whenUpdateCustomerWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Customer updatedCustomer = mockGenerator.nextObject(Customer.class);
        doReturn(null).when(customerService).update(updatedCustomer);

        this.mockMvc.perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCustomer)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE customer by valid id")
    @Test
    public void whenDeleteCustomerWithValidId_ThenReturnOk() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        doReturn(0).when(customerService).deleteById(customer.getId());

        this.mockMvc.perform(delete("/customer")
                        .param("id", customer.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE customer by invalid id")
    @Test
    public void whenDeleteCustomerWithInvalidId_ThenReturnNotFound() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        doReturn(1).when(customerService).deleteById(customer.getId());

        this.mockMvc.perform(delete("/customer")
                        .param("id", customer.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add operation valid id")
    @Test
    public void whenAddNewOperationWithValidCustomerId_ThenReturnOkAndAddedOperationWithId() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(customerService).addOperation(customer.getId(), operationNoId);

        this.mockMvc.perform(post("/customer/operation")
                        .param("id", customer.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operationNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(operation.getId())))
                .andExpect(jsonPath("$.start", is(operation.getStart().toString())))
                .andExpect(jsonPath("$.end", is(operation.getEnd().toString())))
                .andExpect(jsonPath("$.cost", is(operation.getCost())))
                .andExpect(jsonPath("$.status", is(operation.getStatus().toString())));
    }

    @DisplayName("POST add operation invalid id")
    @Test
    public void whenAddNewOperationWithInvalidCustomerId_ThenReturnNotFound() throws Exception {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(customerService).addOperation(customer.getId(), operation);

        this.mockMvc.perform(post("/customer/operation")
                        .param("id", customer.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final Customer customer) throws JSONException {
        JSONObject object = new JSONObject();
        if (customer.getId() != null) {
            object.put("id", customer.getId());
        }
        object.put("cnif", customer.getCnif());
        object.put("name", customer.getName());
        object.put("phone", customer.getPhone());
        object.put("password", customer.getPassword());
        object.put("email", customer.getEmail());

        return object.toString();
    }

    static String asJsonString(final Operation operation) throws JSONException {
        JSONObject object = new JSONObject();
        if (operation.getId() != null) {
            object.put("id", operation.getId());
        }
        object.put("start", operation.getStart());
        object.put("end", operation.getEnd());
        object.put("cost", operation.getCost());
        object.put("status", operation.getStatus());

        return object.toString();
    }
}
