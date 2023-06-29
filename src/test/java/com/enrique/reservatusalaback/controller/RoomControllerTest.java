package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.service.RoomService;
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
import java.lang.Math;

import static java.lang.Math.abs;
import static org.hamcrest.Matchers.*;
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
public class RoomControllerTest {

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    private final String token = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJzb21lQGVtYWlsLmNvbS" +
        "IsImV4cCI6MTcwNTU3NDIzMywibmFtZSI6Ik5MV1VaTlJjQiJ9.EFqoeJd7vHC4E1" +
        "BMaj3f-mQVVssyJHx7tFLuWrYPr8JQSxemy1j5BOHtb0Y3o7Zb";

    @DisplayName("POST add room")
    @Test
    public void whenAddNewRoom_ThenReturnOkAndRoomWithId() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Room roomNoId = new Room(
                room.getBusiness(),
                room.getLocation(),
                room.getName(),
                room.getSize(),
                room.getCapacity(),
                room.getPrice()
        );
        doReturn(room).when(roomService).add(any(Room.class));

        this.mockMvc.perform(post("/room")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(roomNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(room.getId())))
                .andExpect(jsonPath("$.business.id", is(room.getBusiness().getId())))
                .andExpect(jsonPath("$.location.id", is(room.getLocation().getId())))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.size", is(room.getSize())))
                .andExpect(jsonPath("$.capacity", is(room.getCapacity())))
                .andExpect(jsonPath("$.price", is(room.getPrice())));
    }

    @DisplayName("POST add room empty location")
    @Test
    public void whenAddNewRoomWithNoLocation_ThenReturnBadRequestAndError() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        JSONObject object = new JSONObject();
        object.put("name", room.getName());
        object.put("size", room.getSize());
        object.put("capacity", room.getCapacity());
        object.put("price", room.getPrice());

        this.mockMvc.perform(post("/room")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.location", is("Location is required")));
    }

    @DisplayName("GET all rooms")
    @Test
    public void whenFindAllRooms_ThenReturnOkAndListOfRooms() throws Exception {
        List<Room> rooms = mockGenerator.objects(Room.class, 5).toList();
        doReturn(rooms).when(roomService).findAll();

        this.mockMvc.perform(get("/room/all")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(rooms.get(0).getId())))
                .andExpect(jsonPath("$[0].business.id", is(rooms.get(0).getBusiness().getId())))
                .andExpect(jsonPath("$[0].location.id", is(rooms.get(0).getLocation().getId())))
                .andExpect(jsonPath("$[0].name", is(rooms.get(0).getName())))
                .andExpect(jsonPath("$[0].size", is(rooms.get(0).getSize())))
                .andExpect(jsonPath("$[0].capacity", is(rooms.get(0).getCapacity())))
                .andExpect(jsonPath("$[0].price", is(rooms.get(0).getPrice())))
                .andExpect(jsonPath("$[4].id", is(rooms.get(4).getId())))
                .andExpect(jsonPath("$[4].business.id", is(rooms.get(4).getBusiness().getId())))
                .andExpect(jsonPath("$[4].location.id", is(rooms.get(4).getLocation().getId())))
                .andExpect(jsonPath("$[4].name", is(rooms.get(4).getName())))
                .andExpect(jsonPath("$[4].size", is(rooms.get(4).getSize())))
                .andExpect(jsonPath("$[4].capacity", is(rooms.get(4).getCapacity())))
                .andExpect(jsonPath("$[4].price", is(rooms.get(4).getPrice())));
    }

    @DisplayName("GET room by valid id")
    @Test
    public void whenFindRoomByValidId_ThenReturnOkAndRoomWithThatId() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(room).when(roomService).findById(room.getId());

        this.mockMvc.perform(get("/room")
                        .header("Authorization", token)
                        .param("id", room.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(room.getId())))
                .andExpect(jsonPath("$.location.id", is(room.getLocation().getId())))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.size", is(room.getSize())))
                .andExpect(jsonPath("$.capacity", is(room.getCapacity())))
                .andExpect(jsonPath("$.price", is(room.getPrice())));
    }

    @DisplayName("GET room by invalid id")
    @Test
    public void whenFindRoomByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(null).when(roomService).findById(room.getId());

        this.mockMvc.perform(get("/room")
                        .header("Authorization", token)
                        .param("id", room.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update room valid id")
    @Test
    public void whenUpdateRoomWithValidId_ThenReturnOkAndUpdatedRoom() throws Exception {
        Room updatedRoom = mockGenerator.nextObject(Room.class);
        doReturn(updatedRoom).when(roomService).update(any(Room.class));

        this.mockMvc.perform(put("/room")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRoom)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedRoom.getId())))
                .andExpect(jsonPath("$.location.id", is(updatedRoom.getLocation().getId())))
                .andExpect(jsonPath("$.name", is(updatedRoom.getName())))
                .andExpect(jsonPath("$.size", is(updatedRoom.getSize())))
                .andExpect(jsonPath("$.capacity", is(updatedRoom.getCapacity())))
                .andExpect(jsonPath("$.price", is(updatedRoom.getPrice())));
    }

    @DisplayName("PUT update room invalid id")
    @Test
    public void whenUpdateRoomWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Room updatedRoom = mockGenerator.nextObject(Room.class);
        doReturn(null).when(roomService).update(updatedRoom);

        this.mockMvc.perform(put("/room")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRoom)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE room by valid id")
    @Test
    public void whenDeleteRoomWithValidId_ThenReturnOk() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(0).when(roomService).deleteById(room.getId());

        this.mockMvc.perform(delete("/room")
                        .header("Authorization", token)
                        .param("id", room.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE room by invalid id")
    @Test
    public void whenDeleteRoomWithInvalidId_ThenReturnNotFound() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        doReturn(1).when(roomService).deleteById(room.getId());

        this.mockMvc.perform(delete("/room")
                        .header("Authorization", token)
                        .param("id", room.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add operation valid id")
    @Test
    public void whenAddNewOperationWithValidRoomId_ThenReturnOkAndAddedOperationWithId() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation operationNoId = new Operation(
                operation.getCustomer(),
                operation.getBusiness(),
                operation.getRoom(),
                operation.getStart(),
                operation.getEnd(),
                operation.getCost(),
                operation.getStatus()
        );
        doReturn(operation).when(roomService).addOperation(any(Long.class), any(Operation.class));

        this.mockMvc.perform(post("/room/operation")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operationNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(operation.getId())))
                .andExpect(jsonPath("$.start", is(operation.getStart().toString())))
                .andExpect(jsonPath("$.end", is(operation.getEnd().toString())))
                .andExpect(jsonPath("$.cost", equalTo(operation.getCost())))
                .andExpect(jsonPath("$.status", is(operation.getStatus().toString())));
    }

    @DisplayName("POST add operation invalid id")
    @Test
    public void whenAddNewOperationWithInvalidRoomId_ThenReturnNotFound() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        doReturn(null).when(roomService).addOperation(room.getId(), operation);

        this.mockMvc.perform(post("/room/operation")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(operation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add material valid id")
    @Test
    public void whenAddNewMaterialWithValidRoomId_ThenReturnOkAndAddedMaterialWithId() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Material material = mockGenerator.nextObject(Material.class);
        Material materialNoId = new Material(
                material.getMaterial(),
                abs(material.getQuantity())
        );
        doReturn(material).when(roomService).addMaterial(room.getId(), materialNoId);

        this.mockMvc.perform(post("/room/material")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(materialNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(material.getId())))
                .andExpect(jsonPath("$.material", is(material.getMaterial())))
                .andExpect(jsonPath("$.quantity", is(material.getQuantity())));
    }

    @DisplayName("POST add material invalid id")
    @Test
    public void whenAddNewMaterialWithInvalidRoomId_ThenReturnNotFound() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Material material = mockGenerator.nextObject(Material.class);
        material.setQuantity(abs(material.getQuantity()));
        doReturn(null).when(roomService).addMaterial(room.getId(), material);

        this.mockMvc.perform(post("/room/material")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(material)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("POST add schedule valid id")
    @Test
    public void whenAddNewScheduleWithValidRoomId_ThenReturnOkAndAddedScheduleWithId() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        Schedule scheduleNoId = new Schedule(
                schedule.getWeekDay(),
                schedule.getStart(),
                schedule.getEnd()
        );
        doReturn(schedule).when(roomService).addSchedule(room.getId(), scheduleNoId);

        this.mockMvc.perform(post("/room/schedule")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scheduleNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(schedule.getId())))
                .andExpect(jsonPath("$.weekDay", is(schedule.getWeekDay())))
                .andExpect(jsonPath("$.start", is(schedule.getStart().toString().substring(0, 17))))
                .andExpect(jsonPath("$.end", is(schedule.getEnd().toString().substring(0, 17))));
    }

    @DisplayName("POST add schedule invalid id")
    @Test
    public void whenAddNewScheduleWithInvalidRoomId_ThenReturnNotFound() throws Exception {
        Room room = mockGenerator.nextObject(Room.class);
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        doReturn(null).when(roomService).addSchedule(room.getId(), schedule);

        this.mockMvc.perform(post("/room/schedule")
                        .header("Authorization", token)
                        .param("id", room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(schedule)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final Room room) throws JSONException {
        JSONObject object = new JSONObject();
        if (room.getId() != null) {
            object.put("id", room.getId());
        }
        JSONObject business = new JSONObject();
        business.put("id", room.getBusiness().getId());
        object.put("business", business);
        object.put("location", asJson(room.getLocation()));
        object.put("name", room.getName());
        object.put("size", room.getSize());
        object.put("capacity", room.getCapacity());
        object.put("price", room.getPrice());

        return object.toString();
    }

    static String asJsonString(final Operation operation) throws JSONException {
        JSONObject object = new JSONObject();
        if (operation.getId() != null) {
            object.put("id", operation.getId());
        }
        JSONObject business = new JSONObject();
        business.put("id", operation.getBusiness().getId());
        object.put("business", business);
        JSONObject customer = new JSONObject();
        customer.put("id", operation.getCustomer().getId());
        object.put("customer", customer);
        JSONObject room = new JSONObject();
        room.put("id", operation.getRoom().getId());
        object.put("room", room);
        object.put("start", operation.getStart());
        object.put("end", operation.getEnd());
        object.put("cost", operation.getCost());
        object.put("status", operation.getStatus());

        return object.toString();
    }

    static String asJsonString(final Schedule schedule) throws JSONException {
        JSONObject object = new JSONObject();
        if (schedule.getId() != null) {
            object.put("id", schedule.getId());
        }
        object.put("weekDay", schedule.getWeekDay());
        object.put("start", schedule.getStart());
        object.put("end", schedule.getEnd());

        return object.toString();
    }

    static String asJsonString(final Material material) throws JSONException {
        JSONObject object = new JSONObject();
        if (material.getId() != null) {
            object.put("id", material.getId());
        }
        object.put("material", material.getMaterial());
        object.put("quantity", material.getQuantity());

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
}
