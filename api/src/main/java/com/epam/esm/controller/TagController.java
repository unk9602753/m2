package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> getTags() {
        return tagService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable long id) {
        return tagService.find(id).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.no.el") + id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Tag> postTag(@Valid @RequestBody Tag tag) {
        return tagService.create(tag).map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.tag.exist")));
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteTag(@PathVariable long id) {
        return tagService.delete(id).map(result -> new ResponseEntity<>("Tag was deleted successfully", HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new NoSuchElementException(Translator.toLocale("ex.no.el") + id));
    }
}
