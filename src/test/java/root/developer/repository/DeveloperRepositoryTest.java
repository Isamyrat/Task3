package root.developer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import root.developer.model.Developer;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
class DeveloperRepositoryTest {

    @Autowired
    private DeveloperRepository developerRepository;

    @Test()
    public void whenValidEmail_thenDeveloperShouldBeFound() {
        Developer developer1 = new Developer(1L,"Nikita", "ganu@gmail.com");
        Developer developer  = developerRepository.save(developer1);
        String email = "ganu@gmail.com";
        Developer developer2 = developerRepository.findByEmail(developer1.getEmail());

        assertNotNull(developer);
        assertThat(developer2.getEmail())
                .isEqualTo(email);
        assertNotNull(developer2);

        assertThat(developer2.getEmail()).isEqualTo(developer.getEmail());
    }
}