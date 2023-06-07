package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.service.ScheduleService;
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
public class ScheduleControllerTest {

    @MockBean
    private ScheduleService scheduleService;

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

    @DisplayName("POST add schedule")
    @Test
    public void whenAddNewSchedule_ThenReturnOkAndScheduleWithId() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        Schedule scheduleNoId = new Schedule(
                schedule.getWeekDay(),
                schedule.getStart(),
                schedule.getEnd()
        );
        doReturn(schedule).when(scheduleService).add(scheduleNoId);

        this.mockMvc.perform(post("/schedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scheduleNoId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(schedule.getId())))
                .andExpect(jsonPath("$.weekDay", is(schedule.getWeekDay())))
                .andExpect(jsonPath("$.start", is(schedule.getStart().toString())))
                .andExpect(jsonPath("$.end", is(schedule.getEnd().toString())));
    }

    @DisplayName("POST add schedule empty start")
    @Test
    public void whenAddNewScheduleWithNoStart_ThenReturnBadRequestAndError() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        JSONObject object = new JSONObject();
        object.put("weekDay", schedule.getWeekDay());
        object.put("end", schedule.getEnd());

        this.mockMvc.perform(post("/schedule")
                        .header("Authorization", token)
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

    @DisplayName("GET all schedules")
    @Test
    public void whenFindAllSchedules_ThenReturnOkAndListOfSchedules() throws Exception {
        List<Schedule> schedules = mockGenerator.objects(Schedule.class, 5).toList();
        doReturn(schedules).when(scheduleService).findAll();

        this.mockMvc.perform(get("/schedule/all")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(schedules.get(0).getId())))
                .andExpect(jsonPath("$[0].weekDay", is(schedules.get(0).getWeekDay())))
                .andExpect(jsonPath("$[0].start", is(schedules.get(0).getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(schedules.get(0).getEnd().toString())))
                .andExpect(jsonPath("$[4].id", is(schedules.get(4).getId())))
                .andExpect(jsonPath("$[4].weekDay", is(schedules.get(4).getWeekDay())))
                .andExpect(jsonPath("$[4].start", is(schedules.get(4).getStart().toString())))
                .andExpect(jsonPath("$[4].end", is(schedules.get(4).getEnd().toString())));
    }

    @DisplayName("GET schedule by valid id")
    @Test
    public void whenFindScheduleByValidId_ThenReturnOkAndScheduleWithThatId() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        doReturn(schedule).when(scheduleService).findById(schedule.getId());

        this.mockMvc.perform(get("/schedule")
                        .header("Authorization", token)
                        .param("id", schedule.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(schedule.getId())))
                .andExpect(jsonPath("$.weekDay", is(schedule.getWeekDay())))
                .andExpect(jsonPath("$.start", is(schedule.getStart().toString())))
                .andExpect(jsonPath("$.end", is(schedule.getEnd().toString())));
    }

    @DisplayName("GET schedule by invalid id")
    @Test
    public void whenFindScheduleByInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        doReturn(null).when(scheduleService).findById(schedule.getId());

        this.mockMvc.perform(get("/schedule")
                        .header("Authorization", token)
                        .param("id", schedule.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("PUT update schedule valid id")
    @Test
    public void whenUpdateScheduleWithValidId_ThenReturnOkAndUpdatedSchedule() throws Exception {
        Schedule updatedSchedule = mockGenerator.nextObject(Schedule.class);
        doReturn(updatedSchedule).when(scheduleService).update(any(Schedule.class));

        this.mockMvc.perform(put("/schedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSchedule)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(updatedSchedule.getId())))
                .andExpect(jsonPath("$.weekDay", is(updatedSchedule.getWeekDay())))
                .andExpect(jsonPath("$.start", is(updatedSchedule.getStart().toString())))
                .andExpect(jsonPath("$.end", is(updatedSchedule.getEnd().toString())));
    }

    @DisplayName("PUT update schedule invalid id")
    @Test
    public void whenUpdateScheduleWithInvalidId_ThenReturnNotFoundAndNotFoundIdError() throws Exception {
        Schedule updatedSchedule = mockGenerator.nextObject(Schedule.class);
        doReturn(null).when(scheduleService).update(updatedSchedule);

        this.mockMvc.perform(put("/schedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSchedule)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
    }

    @DisplayName("DELETE schedule by valid id")
    @Test
    public void whenDeleteScheduleWithValidId_ThenReturnOk() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        doReturn(0).when(scheduleService).deleteById(schedule.getId());

        this.mockMvc.perform(delete("/schedule")
                        .header("Authorization", token)
                        .param("id", schedule.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.description", is("OK")));
    }

    @DisplayName("DELETE schedule by invalid id")
    @Test
    public void whenDeleteScheduleWithInvalidId_ThenReturnNotFound() throws Exception {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        doReturn(1).when(scheduleService).deleteById(schedule.getId());

        this.mockMvc.perform(delete("/schedule")
                        .header("Authorization", token)
                        .param("id", schedule.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.description",
                        is("Invalid ID: ID not found in database.")));
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
}
