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
        return new ResponseEntity<>(optTag.get(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postTag(@Valid @RequestBody Tag tag) {
        tagService.create(tag);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable long id) {
        tagService.delete(id);
    }
}
