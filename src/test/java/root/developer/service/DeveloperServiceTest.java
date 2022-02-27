package root.developer.service;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import root.developer.model.Developer;
import root.developer.repository.DeveloperRepository;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DeveloperServiceTest {

    @MockBean
    private DeveloperRepository developerRepository;

    @Autowired
    private  DeveloperService developerService;

    private final Developer developer = Developer.builder()
            .id(10L)
            .name("Wasya")
            .email("varlamov@gmail.com")
            .build();

    private final Developer developer2 = Developer.builder()
            .id(15L)
            .name("Lesya")
            .email("lesya@gmail.com")
            .build();

    private final Developer developer3 = Developer.builder()
            .id(20L)
            .name("Pasha")
            .email("pasha@gmail.com")
            .build();

    private final Developer mockDeveloper = Mockito.mock(Developer.class);

    private final List<Developer> developerList = List.of(developer,developer2, developer3);

    @Test
    void save() {

        Mockito.when(mockDeveloper.getId()).thenReturn(1L);
        Mockito.when(mockDeveloper.getName()).thenReturn("Solomon");
        Mockito.when(mockDeveloper.getEmail()).thenReturn("ImBatman@gmail.com");
        Mockito.when(developerRepository.existsByEmail(mockDeveloper.getEmail())).thenReturn(false);


        Mockito.when(developerRepository.save(mockDeveloper)).thenReturn(mockDeveloper);

    }

    @Test
    void checkUserName() {
        String name = "!Solomon";
        assertEquals(true,developerService.checkUserName(developer.getName()));
        assertFalse(developerService.checkUserName(name));
    }

    @Test
    void existsByEmail() {
        Mockito.when(developerRepository.existsByEmail("ImBatman@gmail.com")).thenReturn(true);
        assertTrue(developerService.existsByEmail("ImBatman@gmail.com"));
    }

    @Test
    void update() {
        Mockito.when(mockDeveloper.getId()).thenReturn(1L);
        Mockito.when(developerRepository.findById(mockDeveloper.getId())).thenReturn(Optional.of(mockDeveloper));
        Mockito.when(mockDeveloper.getName()).thenReturn("Solomon");
        Mockito.when(mockDeveloper.getEmail()).thenReturn("ImBatman@gmail.com");
        Mockito.when(developerRepository.existsByEmail(mockDeveloper.getEmail())).thenReturn(false);
        Mockito.when(developerRepository.save(mockDeveloper)).thenReturn(mockDeveloper);
        developerService.update(mockDeveloper);
    }

    @Test
    void findById() {
        Mockito.when(developerRepository.findById(10L)).thenReturn(Optional.ofNullable(developer));
        Assertions.assertThat(developerService.findById(10L)).isEqualTo(developer);
    }

    @Test
    void findAll() {
        Mockito.when(developerRepository.findAll()).thenReturn(developerList);
        assertThat(developerService.findAll(), hasSize(3));
        assertThat(developerService.findAll(), is(developerList));
    }
}