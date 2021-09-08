package avito.internassignment.controllers;

import avito.internassignment.DTO.ChangeRequest;
import avito.internassignment.DTO.TransferRequest;
import avito.internassignment.model.User;
import avito.internassignment.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
class UserControllerIntTests {

    @MockBean
    private UserService userService;

    private final String BASE_URL = "http://localhost:8080/user";

    @Autowired
    private MockMvc mvc;

    final Long USER_ID = 1L;

    User user;

    @BeforeEach
    void setUp() throws Exception {
        user = new User(USER_ID);
        user.setAmount(BigDecimal.valueOf(500));
    }

    @Test
    void checkTest() throws Exception {
        Mockito.when(userService.getCurrentAmount(USER_ID, null)).thenReturn(user.getAmount());
        MvcResult result = mvc.perform(get(BASE_URL + "/1/check"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(result);
        assertEquals("500", result.getResponse().getContentAsString());

    }

    @Test
    public void depositTest() throws Exception {
        User testUser = user;

        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setUserId(testUser.getId());
        changeRequest.setAmount(BigDecimal.valueOf(200));
        changeRequest.setComment("love ya");

        testUser.setAmount(testUser.getAmount().add(changeRequest.getAmount()));

        Mockito.when(userService.deposit(changeRequest.getUserId(), changeRequest.getAmount(), changeRequest.getComment()))
                .thenReturn(testUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(changeRequest);

        MvcResult result = mvc.perform(post(BASE_URL + "/deposit")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        User resultUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertNotNull(result);
        assertEquals(testUser.getId(), resultUser.getId());
        assertEquals(testUser.getAmount(), resultUser.getAmount());
    }

    @Test
    public void withdrawTest() throws Exception {
        User testUser = user;

        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setUserId(testUser.getId());
        changeRequest.setAmount(BigDecimal.valueOf(200));

        Mockito.when(userService.withdraw(changeRequest.getUserId(), changeRequest.getAmount(), null))
                .thenReturn(testUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(changeRequest);

        MvcResult result = mvc.perform(post(BASE_URL + "/withdraw")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        User resultUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertNotNull(result);
        assertEquals(testUser.getId(), resultUser.getId());
        assertEquals(testUser.getAmount(), resultUser.getAmount());
    }

    @Test
    public void transferTest() throws Exception {
        User userToWithdraw = user;
        User userToDeposit = new User(2L);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setIdToWithdraw(userToWithdraw.getId());
        transferRequest.setIdToDeposit(userToDeposit.getId());
        transferRequest.setAmount(BigDecimal.valueOf(200));

        Mockito.when(userService.transfer(transferRequest.getIdToWithdraw(), transferRequest.getIdToDeposit(),
                transferRequest.getAmount(), null)).thenReturn(Arrays.asList(userToWithdraw, userToDeposit));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(transferRequest);

        MvcResult result = mvc.perform(post(BASE_URL + "/transfer")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<User> resultUsers = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>(){});

        assertNotNull(resultUsers);
        assertEquals(userToWithdraw.getId(), resultUsers.get(0).getId());
        assertEquals(userToWithdraw.getAmount(), resultUsers.get(0).getAmount());
        assertEquals(userToDeposit.getId(), resultUsers.get(1).getId());
        assertEquals(userToDeposit.getAmount(), resultUsers.get(1).getAmount());
    }

}
