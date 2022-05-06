package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
    public ResponseEntity<GiftCertificateDto> getCertificateById(@PathVariable long id) {
        return giftCertificateService.find(id).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.no.el") + id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> postCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.create(giftCertificateDto)
                .map(result -> new ResponseEntity<>("Certificate successfully created", HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("ex.create.object")));
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCertificate(@PathVariable long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity<>("Certificate was deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<GiftCertificateDto> patchCertificate(@PathVariable long id, @RequestBody GiftCertificateDto patch) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.find(id)
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
        giftCertificateService.update(giftCertificateDto, id);
        Optional<GiftCertificateDto> optDto = giftCertificateService.find(id);
        return new ResponseEntity<>(optDto.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "", params = {"search", "tag"})
    public List<GiftCertificateDto> getFilteredCertificates(@RequestParam String search, @RequestParam boolean tag) {
        if (tag) {
            return giftCertificateService.findByTagName(search);
        }
        return giftCertificateService.findByPartOfName(search);
    }

//    @RequestMapping(value = "", params = "sort")
//    public List<GiftCertificateDto> getSortCertificate(@RequestParam String sort) {
//        if (sort.equals("date_asc")) {
//            return giftCertificateService.ascSortByDate();
//        } else if (sort.equals("date_desc")) {
//            return giftCertificateService.descSortByDate();
//        } else {
//            return giftCertificateService.ascSortByDate();
//        }
//    }
}
