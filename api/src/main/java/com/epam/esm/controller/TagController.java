package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Object> getTagById(@PathVariable long id) {
        Optional<Tag> optTag = tagService.find(id);
        return optTag.isPresent() ? new ResponseEntity<>(optTag.get(), HttpStatus.OK)
                : new ResponseEntity<>(Translator.toLocale("response.find.tag"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> postTag(@Valid @RequestBody Tag tag) {
        tagService.create(tag);
        return new ResponseEntity<>(Translator.toLocale("response.tag.create"), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteTag(@PathVariable long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
