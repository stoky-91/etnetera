package cz.eg.hr.service;

import cz.eg.hr.dto.JavaScriptFrameworkDto;
import cz.eg.hr.data.JavaScriptFramework;
import cz.eg.hr.exception.FrameworkAlreadyExistException;
import cz.eg.hr.exception.FrameworkNotFoundException;
import cz.eg.hr.repository.JavaScriptFrameworkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class JavaScriptFrameworkServiceTest {

    @Autowired
    private JavaScriptFrameworkService service;

    @Autowired
    private JavaScriptFrameworkRepository repository;

    private JavaScriptFrameworkDto dto;

    @BeforeEach
    public void setUp() {
        dto = new JavaScriptFrameworkDto("React", "18.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.5));
    }

    @Test
    public void testGetAllFrameworks_noData() {
        Iterable<JavaScriptFramework> frameworks = service.getAllFrameworks();
        assertTrue(frameworks.iterator().hasNext(), "List should be empty");
    }

    @Test
    public void testGetAllFrameworks_withData() {
        JavaScriptFramework framework = new JavaScriptFramework(1L, "Vue", "3.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        repository.save(framework);

        Iterable<JavaScriptFramework> frameworks = service.getAllFrameworks();
        assertTrue(frameworks.iterator().hasNext(), "List should contain frameworks");
    }

    @Test
    public void testGetAllFrameworks_multipleData() {
        JavaScriptFramework framework1 = new JavaScriptFramework(1L, "React", "18.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        JavaScriptFramework framework2 = new JavaScriptFramework(2L, "Vue", "3.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        repository.save(framework1);
        repository.save(framework2);

        Iterable<JavaScriptFramework> frameworks = service.getAllFrameworks();
        assertEquals(2, ((Iterable<?>) frameworks).spliterator().getExactSizeIfKnown(), "There should be 2 frameworks");
    }

    @Test
    public void testCreate_success() {
        repository.deleteAll();

        JavaScriptFramework framework = service.create(dto);
        assertNotNull(framework.getId(), "Framework should have an ID");
        assertEquals("React", framework.getName(), "Framework name should be React");
    }

    @Test
    public void testCreate_duplicate() {
        service.create(dto);

        FrameworkAlreadyExistException exception = assertThrows(FrameworkAlreadyExistException.class, () -> {
            service.create(dto);
        });
        assertEquals("This framework already exists.", exception.getMessage());
    }

    @Test
    public void testCreate_invalidData() {
        JavaScriptFrameworkDto invalidDto = new JavaScriptFrameworkDto("", "", null, BigDecimal.valueOf(4.0));

        assertThrows(Exception.class, () -> service.create(invalidDto));
    }

    @Test
    public void testUpdate_success() {
        JavaScriptFramework framework = new JavaScriptFramework(1L, "Angular", "11.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        repository.save(framework);

        JavaScriptFrameworkDto updatedDto = new JavaScriptFrameworkDto("Angular", "12.0", LocalDate.of(2026, 12, 31), BigDecimal.valueOf(4.3));
        JavaScriptFramework updatedFramework = service.update(framework.getId(), updatedDto);

        assertEquals("12.0", updatedFramework.getVersion(), "Version should be updated");
        assertEquals(BigDecimal.valueOf(4.3), updatedFramework.getRating(), "Rating should be updated");
    }

    @Test
    public void testUpdate_notFound() {
        JavaScriptFrameworkDto updatedDto = new JavaScriptFrameworkDto("Angular", "12.0", LocalDate.of(2026, 12, 31), BigDecimal.valueOf(4.0));

        FrameworkNotFoundException exception = assertThrows(FrameworkNotFoundException.class, () -> {
            service.update(999L, updatedDto);
        });
        assertEquals("Framework with ID 999 not found.", exception.getMessage());
    }

    @Test
    public void testUpdate_invalidData() {
        JavaScriptFramework framework = new JavaScriptFramework(1L, "Vue", "3.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        repository.save(framework);

        JavaScriptFrameworkDto invalidDto = new JavaScriptFrameworkDto("", "", null, BigDecimal.valueOf(-4.0));
        assertThrows(Exception.class, () -> service.update(framework.getId(), invalidDto));
    }

    @Test
    public void testDelete_notFound() {
        FrameworkNotFoundException exception = assertThrows(FrameworkNotFoundException.class, () -> {
            service.delete(999L);
        });
        assertEquals("Framework with ID 999 not found.", exception.getMessage());
    }

    @Test
    public void testFulltextSearch_found() {
        JavaScriptFramework framework = new JavaScriptFramework(1L, "Vue", "3.0", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(4.0));
        repository.save(framework);

        Iterable<JavaScriptFramework> results = service.fulltextSearch("Vue");
        assertTrue(results.iterator().hasNext(), "Search should return results");
    }

    @Test
    public void testFulltextSearch_notFound() {
        Iterable<JavaScriptFramework> results = service.fulltextSearch("Angular");
        assertFalse(results.iterator().hasNext(), "Search should return no results");
    }

    @Test
    public void testFulltextSearch_emptyQuery() {
        Iterable<JavaScriptFramework> results = service.fulltextSearch("");
        assertFalse(results.iterator().hasNext(), "Search should return no results for empty query");
    }
}
