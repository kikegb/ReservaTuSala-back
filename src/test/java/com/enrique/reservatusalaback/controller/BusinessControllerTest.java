package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.service.BusinessService;
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
public class BusinessControllerTest {

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @DisplayName("POST add business")
    @Test
    public void whenAddNewBusiness_ThenReturnOkAndBusinessWithId() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        Business businessNoId = new Business(
                business.getCif(),
                business.getName(),
                business.getPhone(),
                business.getPassword(),
                business.getEmail()
        );
        doReturn(business).when(businessService).add(businessNoId);

        this.mockMvc.perform(post("/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(businessNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(business.getId())))
                .andExpect(jsonPath("$.cif", is(business.getCif())))
                .andExpect(jsonPath("$.name", is(business.getName())))
                .andExpect(jsonPath("$.phone", is(business.getPhone())))
                .andExpect(jsonPath("$.password", is(business.getPassword())))
                .andExpect(jsonPath("$.email", is(business.getEmail())))
                .andExpect(jsonPath("$.deleted", is(business.isDeleted())));
    }

    @DisplayName("POST add already existent business")
    @Test
    public void whenAddExistentBusiness_ThenReturnConflictAndAlreadyExistentUserError() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        doReturn(null).when(businessService).add(business);

        this.mockMvc.perform(post("/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(business)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code", is(2)))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description",
                        is("User conflict: There is already a user with that email or cif/nif.")));

    }

    @DisplayName("GET all businesses")
    @Test
    public void whenFindAllBusinesses_ThenReturnOkAndListOfBusinesses() throws Exception {
        List<Business> businesses = mockGenerator.objects(Business.class, 5).toList();
        doReturn(businesses).when(businessService).findAll();

        this.mockMvc.perform(get("/business/findAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(businesses.get(0).getId())))
                .andExpect(jsonPath("$[0].cif", is(businesses.get(0).getCif())))
                .andExpect(jsonPath("$[0].name", is(businesses.get(0).getName())))
                .andExpect(jsonPath("$[0].phone", is(businesses.get(0).getPhone())))
                .andExpect(jsonPath("$[0].password", is(businesses.get(0).getPassword())))
                .andExpect(jsonPath("$[0].email", is(businesses.get(0).getEmail())))
                .andExpect(jsonPath("$[0].deleted", is(businesses.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(businesses.get(4).getId())))
                .andExpect(jsonPath("$[4].cif", is(businesses.get(4).getCif())))
                .andExpect(jsonPath("$[4].name", is(businesses.get(4).getName())))
                .andExpect(jsonPath("$[4].phone", is(businesses.get(4).getPhone())))
                .andExpect(jsonPath("$[4].password", is(businesses.get(4).getPassword())))
                .andExpect(jsonPath("$[4].email", is(businesses.get(4).getEmail())))
                .andExpect(jsonPath("$[4].deleted", is(businesses.get(4).isDeleted())));
    }

    @DisplayName("GET business by valid id")
    @Test
    public void whenFindBusinessByValidId_ThenReturnOkAndBusinessWithThatId() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        doReturn(business).when(businessService).findById(business.getId());

        this.mockMvc.perform(get("/business/findById")
                        .param("id", business.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(business.getId())))
                .andExpect(jsonPath("$.cif", is(business.getCif())))
                .andExpect(jsonPath("$.name", is(business.getName())))
                .andExpect(jsonPath("$.phone", is(business.getPhone())))
                .andExpect(jsonPath("$.password", is(business.getPassword())))
                .andExpect(jsonPath("$.email", is(business.getEmail())))
                .andExpect(jsonPath("$.deleted", is(business.isDeleted())));
    }

    @DisplayName("GET business by invalid id")
    @Test
    public void whenFindBusinessByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        doReturn(null).when(businessService).findById(business.getId());

        this.mockMvc.perform(get("/business/findById")
                        .param("id", business.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update business valid id")
    @Test
    public void whenUpdateBusinessWithValidId_ThenReturnOkAndUpdatedBusiness() throws Exception {
        Business updatedBusiness = mockGenerator.nextObject(Business.class);
        doReturn(updatedBusiness).when(businessService).update(updatedBusiness);

        this.mockMvc.perform(put("/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedBusiness)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedBusiness.getId())))
                .andExpect(jsonPath("$.cif", is(updatedBusiness.getCif())))
                .andExpect(jsonPath("$.name", is(updatedBusiness.getName())))
                .andExpect(jsonPath("$.phone", is(updatedBusiness.getPhone())))
                .andExpect(jsonPath("$.password", is(updatedBusiness.getPassword())))
                .andExpect(jsonPath("$.email", is(updatedBusiness.getEmail())))
                .andExpect(jsonPath("$.deleted", is(updatedBusiness.isDeleted())));
    }

    @DisplayName("PUT update business invalid id")
    @Test
    public void whenUpdateBusinessWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Business updatedBusiness = mockGenerator.nextObject(Business.class);
        doReturn(null).when(businessService).update(updatedBusiness);

        this.mockMvc.perform(put("/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedBusiness)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE business by valid id")
    @Test
    public void whenDeleteBusinessWithValidId_ThenReturnOk() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        doReturn(0).when(businessService).deleteById(business.getId());

        this.mockMvc.perform(delete("/business")
                        .param("id", business.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE business by invalid id")
    @Test
    public void whenDeleteBusinessWithInvalidId_ThenReturnNotFound() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        doReturn(1).when(businessService).deleteById(business.getId());

        this.mockMvc.perform(delete("/business")
                        .param("id", business.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add room valid id")
    @Test
    public void whenAddNewRoomWithValidBusinessId_ThenReturnOkAndAddedRoomWithId() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        Room room = mockGenerator.nextObject(Room.class);
        Room roomNoId = new Room(
                room.getLocation(),
                room.getName(),
                room.getSize(),
                room.getPrice()
        );
        doReturn(room).when(businessService).addRoom(business.getId(), roomNoId);

        this.mockMvc.perform(post("/business/addRoom")
                        .param("id", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(roomNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(room.getId())))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.size", is(room.getSize())))
                .andExpect(jsonPath("$.price", is(room.getPrice())))
                .andExpect(jsonPath("$.deleted", is(room.isDeleted())));
    }

    @DisplayName("POST add room invalid id")
    @Test
    public void whenAddNewRoomWithInvalidBusinessId_ThenReturnNotFound() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(null).when(businessService).addRoom(business.getId(), room);

        this.mockMvc.perform(post("/business/addRoom")
                        .param("id", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(room)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add operation valid id")
    @Test
    public void whenAddNewOperationWithValidBusinessId_ThenReturnOkAndAddedOperationWithId() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(businessService).addOperation(business.getId(), operationNoId);

        this.mockMvc.perform(post("/business/addOperation")
                        .param("id", business.getId().toString())
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
    public void whenAddNewOperationWithInvalidBusinessId_ThenReturnNotFound() throws Exception {
        Business business = mockGenerator.nextObject(Business.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(businessService).addOperation(business.getId(), operation);

        this.mockMvc.perform(post("/business/addOperation")
                        .param("id", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final Business business) throws JSONException {
        JSONObject object = new JSONObject();
        if (business.getId() != null) {
            object.put("id", business.getId());
        }
        object.put("cif", business.getCif());
        object.put("name", business.getName());
        object.put("phone", business.getPhone());
        object.put("password", business.getPassword());
        object.put("email", business.getEmail());

        return object.toString();
    }

    static String asJsonString(final Room room) throws JSONException {
        JSONObject object = new JSONObject();
        if (room.getId() != null) {
            object.put("id", room.getId());
        }
        object.put("location", asJson(room.getLocation()));
        object.put("name", room.getName());
        object.put("size", room.getSize());
        object.put("price", room.getPrice());

        return object.toString();
    }

    static JSONObject asJson(final Location location) throws JSONException {
        JSONObject object = new JSONObject();
        if (location.getId() != null) {
            object.put("id", location.getId());
        }
        object.put("street", location.getStreet());
        object.put("number", location.getNumber());
        object.put("postcode", location.getPostcode());
        object.put("town", location.getTown());
        object.put("province", location.getProvince());
        object.put("country", location.getCountry());

        return object;
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
