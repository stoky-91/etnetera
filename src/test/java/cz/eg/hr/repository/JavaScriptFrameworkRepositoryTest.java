package cz.eg.hr.repository;

import cz.eg.hr.data.JavaScriptFramework;
import cz.eg.hr.repository.JavaScriptFrameworkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class JavaScriptFrameworkRepositoryTest {

    private final JavaScriptFrameworkRepository repository;

    @Autowired
    public JavaScriptFrameworkRepositoryTest(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @Test
    void testSaveAndRetrieveFramework() {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("React");
        framework.setVersion("18.0.0");
        framework.setEndOfSupportDate(LocalDate.of(2025, 12, 31));
        framework.setRating(new BigDecimal("4.8"));

        JavaScriptFramework saved = repository.save(framework);

        Optional<JavaScriptFramework> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("React");
        assertThat(found.get().getVersion()).isEqualTo("18.0.0");
    }
}
