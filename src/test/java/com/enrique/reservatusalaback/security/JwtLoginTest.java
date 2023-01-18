package com.enrique.reservatusalaback.security;

import com.enrique.reservatusalaback.model.User;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class JwtLoginTest {

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
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

    @DisplayName("POST login")
    @Test
    public void whenLoginWithEmailAndPassword_ThenReturnOkAndAuthorizationToken() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        String notEncodedPassword = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setEmail("some@email.com");
        user.setPassword(encodedPassword);
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail(user.getEmail());
        credentials.setPassword(notEncodedPassword);
        doReturn(new UserDetailsImpl(user)).when(userDetailsService).loadUserByUsername(any(String.class));

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentials)))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Authorization").exists())
                .andExpect(jsonPath("$.Authorization", startsWith("Bearer ")));
    }

    @DisplayName("POST login invalid credentials")
    @Test
    public void whenLoginWithWrongEmailAndPassword_ThenReturnUnauthorized() throws Exception {
        User user = mockGenerator.nextObject(User.class);
        String notEncodedPassword = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setEmail("some@email.com");
        user.setPassword(encodedPassword);
        AuthCredentials credentials = new AuthCredentials();
        credentials.setEmail(user.getEmail());
        credentials.setPassword(notEncodedPassword);
        doThrow(new UsernameNotFoundException("User with email " + user.getEmail() + "not exists.")).when(userDetailsService).loadUserByUsername(any(String.class));

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentials)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.Authorization").doesNotExist());
    }

    @DisplayName("GET all users request with authorization token")
    @Test
    public void whenMakingRequestWithAuthorizationToken_ThenReturnUserList() throws Exception {
        List<User> users = mockGenerator.objects(User.class, 5).toList();
        String token = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJzb21lQGVtYWlsLmNvbS" +
                "IsImV4cCI6MTcwNTU3NDIzMywibmFtZSI6Ik5MV1VaTlJjQiJ9.EFqoeJd7vHC4E1" +
                "BMaj3f-mQVVssyJHx7tFLuWrYPr8JQSxemy1j5BOHtb0Y3o7Zb";
        doReturn(users).when(userService).findAll();

        this.mockMvc.perform(get("/user/all")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @DisplayName("GET all users request without authorization token")
    @Test
    public void whenMakingRequestWithNoAuthorization_ThenReturnUnauthorized() throws Exception {
        List<User> users = mockGenerator.objects(User.class, 5).toList();
        doReturn(users).when(userService).findAll();

        this.mockMvc.perform(get("/user/all"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    static String asJsonString(final AuthCredentials credentials) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email", credentials.getEmail());
        object.put("password", credentials.getPassword());

        return object.toString();
    }
}
