package cz.eg.hr.service;

import cz.eg.hr.dto.JavaScriptFrameworkDto;
import cz.eg.hr.data.JavaScriptFramework;
import cz.eg.hr.exception.FrameworkAlreadyExistException;
import cz.eg.hr.exception.FrameworkNotFoundException;
import cz.eg.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;

    @Autowired
    public JavaScriptFrameworkService(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Iterable<JavaScriptFramework> getAllFrameworks() {
        return repository.findAll();
    }

    @Transactional
    public JavaScriptFramework create(JavaScriptFrameworkDto dto) {
        if (repository.existsByNameAndVersion(dto.getName(), dto.getVersion())) {
            throw new FrameworkAlreadyExistException("This framework already exists.");
        }

        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName(dto.getName());
        framework.setVersion(dto.getVersion());
        framework.setEndOfSupportDate(dto.getEndOfSupportDate());
        framework.setRating(dto.getRating());

        return repository.save(framework);
    }

    @Transactional
    public JavaScriptFramework update(Long id, JavaScriptFrameworkDto dto) {
        return repository.findById(id)
            .map(existingFramework -> {
                existingFramework.setName(dto.getName());
                existingFramework.setVersion(dto.getVersion());
                existingFramework.setEndOfSupportDate(dto.getEndOfSupportDate());
                existingFramework.setRating(dto.getRating());
                return repository.save(existingFramework);
            })
            .orElseThrow(() -> new FrameworkNotFoundException("Framework with ID " + id + " not found."));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new FrameworkNotFoundException("Framework with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<JavaScriptFramework> fulltextSearch(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return repository.findByNameOrVersion(text, text);
    }
}
