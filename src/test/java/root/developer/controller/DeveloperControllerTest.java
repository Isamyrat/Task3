package root.developer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import root.developer.model.Developer;
import root.developer.security.model.JwtRequest;
import root.developer.security.model.JwtResponse;
import root.developer.security.model.Role;
import root.developer.security.service.AuthService;
import root.developer.service.DeveloperService;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @MockBean
    private DeveloperService developerService;

    @Autowired
    private AuthService authService;

    Developer RECORD_1 = new Developer(1L, "Nikita", "ganu@gmail.com");
    Developer RECORD_2 = new Developer(2L, "Misha", "orlov@gmail.com");
    Developer RECORD_3 = new Developer(3L, "Sasha", "volchonok@gmail.com");

    @Test
    public void getAllRecords_success() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setIssuer("GP");
        jwtRequest.setRoles(Collections.singleton(Role.ADMIN));

        JwtResponse jwtResponse = authService.login(jwtRequest);

        List<Developer> developerList = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        when(developerService.findAll()).thenReturn(developerList);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/developer/findAll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getAccessToken())
                .with(SecurityMockMvcRequestPostProcessors.user("task2").roles("ADMIN"));

        mockMvc.perform(request)
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Sasha")))
                .andExpect(jsonPath("$[0].email", is("ganu@gmail.com")));

    }
    @Test
    public void getDeveloperById_success() throws Exception {
        when(developerService.findById(RECORD_1.getId())).thenReturn(RECORD_1);
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setIssuer("GP");
        jwtRequest.setRoles(Collections.singleton(Role.ADMIN));

        JwtResponse jwtResponse = authService.login(jwtRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/developer/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getAccessToken())
                .with(SecurityMockMvcRequestPostProcessors.user("task2").roles("ADMIN"));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nikita")));
    }
    @Test
    public void updateDeveloper_success() throws Exception {
        Developer developer = Developer.builder()
                .id(1L)
                .name("Masha")
                .email("varlamova@gmail.com")
                .build();
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setIssuer("GP");
        jwtRequest.setRoles(Collections.singleton(Role.ADMIN));

        JwtResponse jwtResponse = authService.login(jwtRequest);
        Map<Validation, Developer> map = new LinkedHashMap<>();
        map.put(Validation.CREATED, developer);
        when(developerService.findById(RECORD_1.getId())).thenReturn(RECORD_1);
        when(developerService.update(developer)).thenReturn(map);

        String updateContent = objectWriter.writeValueAsString(developer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/developer/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getAccessToken())
                .with(SecurityMockMvcRequestPostProcessors.user("task2").roles("ADMIN"));

        mockMvc.perform(mockRequest.content(updateContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Masha")))
                .andExpect(jsonPath("$.email", is("varlamova@gmail.com")));
    }
    @Test
    public void createDeveloper_success() throws Exception {
        Developer developer = Developer.builder()
                .id(1L)
                .name("Andryusha")
                .email("varlamov@gmail.com")
                .build();

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setIssuer("GP");
        jwtRequest.setRoles(Collections.singleton(Role.ADMIN));

        JwtResponse jwtResponse = authService.login(jwtRequest);
        Map<Validation, Developer> map = new LinkedHashMap<>();
        map.put(Validation.CREATED, developer);

        when(developerService.save(developer)).thenReturn(map);
        System.out.println(jwtResponse.getAccessToken());
        String content = objectWriter.writeValueAsString(developer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/developer/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getAccessToken());

        mockMvc.perform(mockRequest.content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Andryusha")))
                .andExpect(jsonPath("$.email", is("varlamov@gmail.com")));
    }

}