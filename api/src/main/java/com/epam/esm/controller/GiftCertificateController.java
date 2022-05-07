package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public List<GiftCertificateDto> getCertificates() {
        return giftCertificateService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getCertificateById(@PathVariable long id) {
        Optional<GiftCertificateDto> optDto = giftCertificateService.find(id);
        return optDto.isPresent() ? new ResponseEntity<>(optDto.get(), HttpStatus.OK)
                : new ResponseEntity<>(Translator.toLocale("response.find.certificate"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        giftCertificateService.create(giftCertificateDto);
        return new ResponseEntity<>(Translator.toLocale("response.certificate.create"), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = "sort_criteria")
    public List<GiftCertificateDto> getSortCertificate(@RequestParam(value = "sort_direction", defaultValue = "asc") String direction,
                                                       @RequestParam("sort_criteria") String criteria) {
        return giftCertificateService.sort(direction, criteria);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<String> patchCertificate(@PathVariable long id, @RequestBody GiftCertificateDto patch) {
        GiftCertificateDto dto = giftCertificateService.find(id).get();
        giftCertificateService.updateObject(dto, patch);
        giftCertificateService.update(dto, id);
        return new ResponseEntity<>(Translator.toLocale("response.certificate.update"), HttpStatus.OK);
    }

    @GetMapping(params = "search_name")
    public List<GiftCertificateDto> getFilteredCertificates(@RequestParam(defaultValue = "certificate", value = "search_criteria") String criteria,
                                                            @RequestParam("search_name") String name) {
        return giftCertificateService.findByCriteria(criteria, name);
    }
}
