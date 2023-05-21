package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.service.UserService;
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

import java.math.BigDecimal;
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
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
                    .stringLengthRange(9,9)
    );

    private final String token = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJzb21lQGVtYWlsLmNvbS" +
        "IsImV4cCI6MTcwNTU3NDIzMywibmFtZSI6Ik5MV1VaTlJjQiJ9.EFqoeJd7vHC4E1" +
        "BMaj3f-mQVVssyJHx7tFLuWrYPr8JQSxemy1j5BOHtb0Y3o7Zb";

    @DisplayName("POST add user")
    @Test
    public void whenAddNewUser_ThenReturnOkAndUserWithId() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        user.setEmail("some@email.com");
        User userNoId = new User(
                user.getCnif(),
                user.getName(),
                user.getPhone(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
        doReturn(user).when(userService).add(userNoId);

        this.mockMvc.perform(post("/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.cnif", is(user.getCnif())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.phone", is(user.getPhone())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.role", is(user.getRole().toString())))
                .andExpect(jsonPath("$.deleted", is(user.isDeleted())));
    }

    @DisplayName("POST add already existent user")
    @Test
    public void whenAddExistentUser_ThenReturnConflictAndAlreadyExistentUserError() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        user.setEmail("some@email.com");
        doReturn(null).when(userService).add(user);

        this.mockMvc.perform(post("/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.code", is("2")))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description",
                        is("User conflict: There is already a user with that email or cif/nif.")));

    }

    @DisplayName("POST add user empty CIF")
    @Test
    public void whenAddNewUserWithNoCifOrNif_ThenReturnBadRequestAndError() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        JSONObject object = new JSONObject();
        object.put("name", user.getName());
        object.put("phone", user.getPhone());
        object.put("password", user.getPassword());
        object.put("email", user.getEmail());

        this.mockMvc.perform(post("/user")
                        .header("Authorization", token)
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

    @DisplayName("POST add user short phone")
    @Test
    public void whenAddNewUserWithWrongPhone_ThenReturnBadRequestAndError() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        JSONObject object = new JSONObject();
        object.put("cif", user.getCnif());
        object.put("name", user.getName());
        object.put("phone", "1234567");
        object.put("password", user.getPassword());
        object.put("email", user.getEmail());

        this.mockMvc.perform(post("/user")
                        .header("Authorization", token)
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

    @DisplayName("GET all users")
    @Test
    public void whenFindAllUsers_ThenReturnOkAndListOfUsers() throws Exception {
        List<User> users = mockGenerator.objects(User.class, 5).toList();
        doReturn(users).when(userService).findAll();

        this.mockMvc.perform(get("/user/all")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId())))
                .andExpect(jsonPath("$[0].cnif", is(users.get(0).getCnif())))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].phone", is(users.get(0).getPhone())))
                .andExpect(jsonPath("$[0].password", is(users.get(0).getPassword())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())))
                .andExpect(jsonPath("$[0].role", is(users.get(0).getRole().toString())))
                .andExpect(jsonPath("$[0].deleted", is(users.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(users.get(4).getId())))
                .andExpect(jsonPath("$[4].cnif", is(users.get(4).getCnif())))
                .andExpect(jsonPath("$[4].name", is(users.get(4).getName())))
                .andExpect(jsonPath("$[4].phone", is(users.get(4).getPhone())))
                .andExpect(jsonPath("$[4].password", is(users.get(4).getPassword())))
                .andExpect(jsonPath("$[4].email", is(users.get(4).getEmail())))
                .andExpect(jsonPath("$[4].role", is(users.get(4).getRole().toString())))
                .andExpect(jsonPath("$[4].deleted", is(users.get(4).isDeleted())));
    }

    @DisplayName("GET user by valid id")
    @Test
    public void whenFindUserByValidId_ThenReturnOkAndUserWithThatId() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        doReturn(user).when(userService).findById(user.getId());

        this.mockMvc.perform(get("/user")
                        .header("Authorization", token)
                        .param("id", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.cnif", is(user.getCnif())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.phone", is(user.getPhone())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.role", is(user.getRole().toString())))
                .andExpect(jsonPath("$.deleted", is(user.isDeleted())));
    }

    @DisplayName("GET user by invalid id")
    @Test
    public void whenFindUserByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        doReturn(null).when(userService).findById(user.getId());

        this.mockMvc.perform(get("/user")
                        .header("Authorization", token)
                        .param("id", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update user valid id")
    @Test
    public void whenUpdateUserWithValidId_ThenReturnOkAndUpdatedUser() throws Exception {
        User updatedUser = mockGenerator.nextObject(User.class);
        updatedUser.setEmail("some@email.com");
        doReturn(updatedUser).when(userService).update(any(User.class));

        this.mockMvc.perform(put("/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedUser.getId())))
                .andExpect(jsonPath("$.cnif", is(updatedUser.getCnif())))
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.phone", is(updatedUser.getPhone())))
                .andExpect(jsonPath("$.password", is(updatedUser.getPassword())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())))
                .andExpect(jsonPath("$.role", is(updatedUser.getRole().toString())))
                .andExpect(jsonPath("$.deleted", is(updatedUser.isDeleted())));
    }

    @DisplayName("PUT update user invalid id")
    @Test
    public void whenUpdateUserWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        User updatedUser = mockGenerator.nextObject(User.class);
        updatedUser.setEmail("some@email.com");
        doReturn(null).when(userService).update(updatedUser);

        this.mockMvc.perform(put("/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUser)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE user by valid id")
    @Test
    public void whenDeleteUserWithValidId_ThenReturnOk() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        doReturn(0).when(userService).deleteById(user.getId());

        this.mockMvc.perform(delete("/user")
                        .header("Authorization", token)
                        .param("id", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE user by invalid id")
    @Test
    public void whenDeleteUserWithInvalidId_ThenReturnNotFound() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        doReturn(1).when(userService).deleteById(user.getId());

        this.mockMvc.perform(delete("/user")
                        .header("Authorization", token)
                        .param("id", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add room valid id")
    @Test
    public void whenAddNewRoomWithValidUserId_ThenReturnOkAndAddedRoomWithId() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Room room = mockGenerator.nextObject(Room.class);
        Room roomNoId = new Room(
                room.getLocation(),
                room.getName(),
                room.getSize(),
                room.getCapacity(),
                room.getPrice()
        );
        doReturn(room).when(userService).addRoom(user.getId(), roomNoId);

        this.mockMvc.perform(post("/user/business/room")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(roomNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(room.getId())))
                .andExpect(jsonPath("$.location.id", is(room.getLocation().getId())))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.size", is(room.getSize())))
                .andExpect(jsonPath("$.price", is(BigDecimal.valueOf(room.getPrice()))))
                .andExpect(jsonPath("$.deleted", is(room.isDeleted())));
    }

    @DisplayName("POST add room invalid id")
    @Test
    public void whenAddNewRoomWithInvalidUserId_ThenReturnNotFound() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(null).when(userService).addRoom(user.getId(), room);

        this.mockMvc.perform(post("/user/business/room")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(room)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add business operation valid id")
    @Test
    public void whenAddNewBusinessOperationWithValidUserId_ThenReturnOkAndAddedOperationWithId() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(userService).addBusinessOperation(user.getId(), operationNoId);

        this.mockMvc.perform(post("/user/business/operation")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
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

    @DisplayName("POST add business operation invalid id")
    @Test
    public void whenAddNewBusinessOperationWithInvalidUserId_ThenReturnNotFound() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(userService).addBusinessOperation(user.getId(), operation);

        this.mockMvc.perform(post("/user/business/operation")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add customer operation valid id")
    @Test
    public void whenAddNewCustomerOperationWithValidUserId_ThenReturnOkAndAddedOperationWithId() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(userService).addCustomerOperation(user.getId(), operationNoId);

        this.mockMvc.perform(post("/user/customer/operation")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
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

    @DisplayName("POST add customer operation invalid id")
    @Test
    public void whenAddNewCustomerOperationWithInvalidUserId_ThenReturnNotFound() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(userService).addCustomerOperation(user.getId(), operation);

        this.mockMvc.perform(post("/user/customer/operation")
                        .header("Authorization", token)
                        .param("id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final User user) throws JSONException {
        JSONObject object = new JSONObject();
        if (user.getId() != null) {
            object.put("id", user.getId());
        }
        object.put("cnif", user.getCnif());
        object.put("name", user.getName());
        object.put("phone", user.getPhone());
        object.put("password", user.getPassword());
        object.put("email", user.getEmail());
        object.put("role", user.getRole());

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
        object.put("capacity", room.getCapacity());
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
