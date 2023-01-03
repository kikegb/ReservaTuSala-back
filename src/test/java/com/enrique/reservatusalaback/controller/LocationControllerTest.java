package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.service.LocationService;
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
public class LocationControllerTest {

    @MockBean
    private LocationService locationService;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @DisplayName("POST add location")
    @Test
    public void whenAddNewLocation_ThenReturnOkAndLocationWithId() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        Location locationNoId = new Location(
                location.getStreet(),
                location.getNumber(),
                location.getPostcode(),
                location.getTown(),
                location.getProvince(),
                location.getCountry()
        );
        doReturn(location).when(locationService).add(locationNoId);

        this.mockMvc.perform(post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(locationNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(location.getId())))
                .andExpect(jsonPath("$.street", is(location.getStreet())))
                .andExpect(jsonPath("$.number", is(location.getNumber())))
                .andExpect(jsonPath("$.postcode", is(location.getPostcode())))
                .andExpect(jsonPath("$.town", is(location.getTown())))
                .andExpect(jsonPath("$.province", is(location.getProvince())))
                .andExpect(jsonPath("$.country", is(location.getCountry())))
                .andExpect(jsonPath("$.deleted", is(location.isDeleted())));
    }

    @DisplayName("POST add location empty street")
    @Test
    public void whenAddNewLocationWithNoStreet_ThenReturnBadRequestAndError() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        JSONObject object = new JSONObject();
        object.put("number", location.getNumber());
        object.put("postcode", location.getPostcode());
        object.put("town", location.getTown());
        object.put("province", location.getProvince());
        object.put("country", location.getCountry());

        this.mockMvc.perform(post("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("3")))
                .andExpect(jsonPath("$.description",
                        is("Bad request: Request body has empty or wrong formatted data.")))
                .andExpect(jsonPath("$.street", is("Street is required")));
    }

    @DisplayName("GET all locations")
    @Test
    public void whenFindAllLocations_ThenReturnOkAndListOfLocations() throws Exception {
        List<Location> locations = mockGenerator.objects(Location.class, 5).toList();
        doReturn(locations).when(locationService).findAll();

        this.mockMvc.perform(get("/location"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(locations.get(0).getId())))
                .andExpect(jsonPath("$[0].street", is(locations.get(0).getStreet())))
                .andExpect(jsonPath("$[0].number", is(locations.get(0).getNumber())))
                .andExpect(jsonPath("$[0].postcode", is(locations.get(0).getPostcode())))
                .andExpect(jsonPath("$[0].town", is(locations.get(0).getTown())))
                .andExpect(jsonPath("$[0].province", is(locations.get(0).getProvince())))
                .andExpect(jsonPath("$[0].country", is(locations.get(0).getCountry())))
                .andExpect(jsonPath("$[0].deleted", is(locations.get(0).isDeleted())))
                .andExpect(jsonPath("$[4].id", is(locations.get(4).getId())))
                .andExpect(jsonPath("$[4].street", is(locations.get(4).getStreet())))
                .andExpect(jsonPath("$[4].number", is(locations.get(4).getNumber())))
                .andExpect(jsonPath("$[4].postcode", is(locations.get(4).getPostcode())))
                .andExpect(jsonPath("$[4].town", is(locations.get(4).getTown())))
                .andExpect(jsonPath("$[4].province", is(locations.get(4).getProvince())))
                .andExpect(jsonPath("$[4].country", is(locations.get(4).getCountry())))
                .andExpect(jsonPath("$[4].deleted", is(locations.get(4).isDeleted())));
    }

    @DisplayName("GET location by valid id")
    @Test
    public void whenFindLocationByValidId_ThenReturnOkAndLocationWithThatId() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        doReturn(location).when(locationService).findById(location.getId());

        this.mockMvc.perform(get("/location/byId")
                        .param("id", location.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(location.getId())))
                .andExpect(jsonPath("$.street", is(location.getStreet())))
                .andExpect(jsonPath("$.number", is(location.getNumber())))
                .andExpect(jsonPath("$.postcode", is(location.getPostcode())))
                .andExpect(jsonPath("$.town", is(location.getTown())))
                .andExpect(jsonPath("$.province", is(location.getProvince())))
                .andExpect(jsonPath("$.country", is(location.getCountry())))
                .andExpect(jsonPath("$.deleted", is(location.isDeleted())));
    }

    @DisplayName("GET location by invalid id")
    @Test
    public void whenFindLocationByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        doReturn(null).when(locationService).findById(location.getId());

        this.mockMvc.perform(get("/location/byId")
                        .param("id", location.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update location valid id")
    @Test
    public void whenUpdateLocationWithValidId_ThenReturnOkAndUpdatedLocation() throws Exception {
        Location updatedLocation = mockGenerator.nextObject(Location.class);
        doReturn(updatedLocation).when(locationService).update(any(Location.class));

        this.mockMvc.perform(put("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedLocation)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedLocation.getId())))
                .andExpect(jsonPath("$.street", is(updatedLocation.getStreet())))
                .andExpect(jsonPath("$.number", is(updatedLocation.getNumber())))
                .andExpect(jsonPath("$.postcode", is(updatedLocation.getPostcode())))
                .andExpect(jsonPath("$.town", is(updatedLocation.getTown())))
                .andExpect(jsonPath("$.province", is(updatedLocation.getProvince())))
                .andExpect(jsonPath("$.country", is(updatedLocation.getCountry())))
                .andExpect(jsonPath("$.deleted", is(updatedLocation.isDeleted())));
    }

    @DisplayName("PUT update location invalid id")
    @Test
    public void whenUpdateLocationWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Location updatedLocation = mockGenerator.nextObject(Location.class);
        doReturn(null).when(locationService).update(updatedLocation);

        this.mockMvc.perform(put("/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedLocation)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE location by valid id")
    @Test
    public void whenDeleteLocationWithValidId_ThenReturnOk() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        doReturn(0).when(locationService).deleteById(location.getId());

        this.mockMvc.perform(delete("/location")
                        .param("id", location.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE location by invalid id")
    @Test
    public void whenDeleteLocationWithInvalidId_ThenReturnNotFound() throws Exception {
        Location location = mockGenerator.nextObject(Location.class);
        doReturn(1).when(locationService).deleteById(location.getId());

        this.mockMvc.perform(delete("/location")
                        .param("id", location.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    static String asJsonString(final Location location) throws JSONException {
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

        return object.toString();
    }
}
