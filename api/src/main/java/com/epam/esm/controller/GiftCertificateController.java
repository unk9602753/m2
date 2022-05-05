package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateDtoService giftCertificateDtoService;

    @GetMapping
    public List<GiftCertificateDto> getCertificates() {
        return giftCertificateDtoService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<GiftCertificateDto> getCertificateById(@PathVariable long id) {
        return giftCertificateDtoService.find(id).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.no.el") + id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> postCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateDtoService.create(giftCertificateDto)
                .map(result -> new ResponseEntity<>("Certificate successfully created", HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.create.object")));
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCertificate(@PathVariable long id) {
        return giftCertificateDtoService.delete(id).map(result -> new ResponseEntity<>("Certificate was deleted successfully", HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new NoSuchElementException(Translator.toLocale("ex.no.el") + id));
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<GiftCertificateDto> patchCertificate(@PathVariable long id, @RequestBody GiftCertificateDto patch) {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoService.find(id)
                .orElseThrow(() -> new NoSuchElementException(Translator.toLocale("ex.no.el") + id));
        if (patch.getName() != null) {
            giftCertificateDto.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            giftCertificateDto.setDescription(patch.getDescription());
        }
        if (patch.getTags() != null) {
            giftCertificateDto.setTags(patch.getTags());
        }
        if (patch.getPrice() != 0) {
            giftCertificateDto.setPrice(patch.getPrice());
        }
        if (patch.getDuration() != 0) {
            giftCertificateDto.setDuration(patch.getDuration());
        }
        giftCertificateDtoService.update(giftCertificateDto, id);
        Optional<GiftCertificateDto> optDto = giftCertificateDtoService.find(id);
        return new ResponseEntity<>(optDto.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "", params = {"search", "tag"})
    public List<GiftCertificateDto> getFilteredCertificates(@RequestParam String search, @RequestParam boolean tag) {
        if (tag) {
            return giftCertificateDtoService.findByTagName(search);
        }
        return giftCertificateDtoService.findByPartOfName(search);
    }

    @RequestMapping(value = "", params = "sort")
    public List<GiftCertificateDto> getSortCertificate(@RequestParam String sort) {
        if (sort.equals("date_asc")) {
            return giftCertificateDtoService.ascSortByDate();
        } else if (sort.equals("date_desc")) {
            return giftCertificateDtoService.descSortByDate();
        } else {
            return giftCertificateDtoService.ascSortByDate();
        }
    }
}
