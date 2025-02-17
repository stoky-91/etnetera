package cz.eg.hr.controller;

import cz.eg.hr.dto.JavaScriptFrameworkDto;
import cz.eg.hr.data.JavaScriptFramework;
import cz.eg.hr.service.JavaScriptFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/frameworks")
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkService service;

    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkService service) {
        this.service = service;
    }

    @GetMapping()
    public Iterable<JavaScriptFramework> frameworks() {
        return service.getAllFrameworks();
    }

    @GetMapping("/search")
    public Iterable<JavaScriptFramework> fulltextSearch(@RequestParam String text) {
        return service.fulltextSearch(text);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public JavaScriptFramework create(@RequestBody JavaScriptFrameworkDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public JavaScriptFramework update(@PathVariable("id") Long id, @RequestBody JavaScriptFrameworkDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.delete(id);
    }
}
