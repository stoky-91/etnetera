package cz.eg.hr.repository;

import cz.eg.hr.data.JavaScriptFramework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JavaScriptFrameworkRepository extends CrudRepository<JavaScriptFramework, Long> {
    boolean existsByNameAndVersion(String name, String version);

    Iterable<JavaScriptFramework> findByNameOrVersion(String name, String version);
}
