package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.service.OperationService;
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
public class OperationControllerTest {

    @MockBean
    private OperationService operationService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @DisplayName("POST add operation")
    @Test
    public void whenAddNewOperation_ThenReturnOkAndOperationWithId() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(operationService).add(operationNoId);

        this.mockMvc.perform(post("/operation")
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
                .andExpect(jsonPath("$.status", is(operation.getStatus().toString())))
                .andExpect(jsonPath("$.deleted", is(operation.isDeleted())));
    }

    @DisplayName("POST add operation empty start")
    @Test
    public void whenAddNewOperationWithNoStart_ThenReturnBadRequestAndError() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        JSONObject object = new JSONObject();
        object.put("end", operation.getEnd());
        object.put("cost", operation.getCost());
        object.put("status", operation.getStatus());

        this.mockMvc.perform(post("/operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.start", is("Start is required")));
    }

    @DisplayName("GET all operations")
    @Test
    public void whenFindAllOperations_ThenReturnOkAndListOfOperations() throws Exception {
        List<Operation> operations = mockGenerator.objects(Operation.class, 5).toList();
        doReturn(operations).when(operationService).findAll();

        this.mockMvc.perform(get("/operation/findAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(operations.get(0).getId())))
                .andExpect(jsonPath("$[0].start", is(operations.get(0).getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(operations.get(0).getEnd().toString())))
                .andExpect(jsonPath("$[0].cost", is(operations.get(0).getCost())))
                .andExpect(jsonPath("$[0].status", is(operations.get(0).getStatus().toString())))
                .andExpect(jsonPath("$[0].deleted", is(operations.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(operations.get(4).getId())))
                .andExpect(jsonPath("$[4].start", is(operations.get(4).getStart().toString())))
                .andExpect(jsonPath("$[4].end", is(operations.get(4).getEnd().toString())))
                .andExpect(jsonPath("$[4].cost", is(operations.get(4).getCost())))
                .andExpect(jsonPath("$[4].status", is(operations.get(4).getStatus().toString())))
                .andExpect(jsonPath("$[4].deleted", is(operations.get(4).isDeleted())));
    }

    @DisplayName("GET operation by valid id")
    @Test
    public void whenFindOperationByValidId_ThenReturnOkAndOperationWithThatId() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(operation).when(operationService).findById(operation.getId());

        this.mockMvc.perform(get("/operation/findById")
                        .param("id", operation.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(operation.getId())))
                .andExpect(jsonPath("$.start", is(operation.getStart().toString())))
                .andExpect(jsonPath("$.end", is(operation.getEnd().toString())))
                .andExpect(jsonPath("$.cost", is(operation.getCost())))
                .andExpect(jsonPath("$.status", is(operation.getStatus().toString())))
                .andExpect(jsonPath("$.deleted", is(operation.isDeleted())));
    }

    @DisplayName("GET operation by invalid id")
    @Test
    public void whenFindOperationByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(operationService).findById(operation.getId());

        this.mockMvc.perform(get("/operation/findById")
                        .param("id", operation.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update operation valid id")
    @Test
    public void whenUpdateOperationWithValidId_ThenReturnOkAndUpdatedOperation() throws Exception {
        Operation updatedOperation = mockGenerator.nextObject(Operation.class);
        doReturn(updatedOperation).when(operationService).update(any(Operation.class));

        this.mockMvc.perform(put("/operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedOperation)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedOperation.getId())))
                .andExpect(jsonPath("$.start", is(updatedOperation.getStart().toString())))
                .andExpect(jsonPath("$.end", is(updatedOperation.getEnd().toString())))
                .andExpect(jsonPath("$.cost", is(updatedOperation.getCost())))
                .andExpect(jsonPath("$.status", is(updatedOperation.getStatus().toString())))
                .andExpect(jsonPath("$.deleted", is(updatedOperation.isDeleted())));
    }

    @DisplayName("PUT update operation invalid id")
    @Test
    public void whenUpdateOperationWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Operation updatedOperation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(operationService).update(updatedOperation);

        this.mockMvc.perform(put("/operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedOperation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE operation by valid id")
    @Test
    public void whenDeleteOperationWithValidId_ThenReturnOk() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(0).when(operationService).deleteById(operation.getId());

        this.mockMvc.perform(delete("/operation")
                        .param("id", operation.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE operation by invalid id")
    @Test
    public void whenDeleteOperationWithInvalidId_ThenReturnNotFound() throws Exception {
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(1).when(operationService).deleteById(operation.getId());

        this.mockMvc.perform(delete("/operation")
                        .param("id", operation.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
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
