package group.int221project.controllers;

import group.int221project.dtos.*;
import group.int221project.entities.Announce;
import group.int221project.services.AnnounceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins= {"http://localhost:5173","http://intproj22.sit.kmutt.ac.th"})
@RequestMapping("/api/announcements")
public class AnnounceController {
    @Autowired
    private AnnounceService announceService;

    @GetMapping("")
    public List<AnnounceDto> getAllAnnouncements(@RequestParam(value = "mode", required = false) String mode) {
        List<Announce> announces = announceService.getAllAnnouncements(mode);
        List<AnnounceDto> announceDtos = new ArrayList<>();

        for (Announce announce : announces) {
            AnnounceDto announceDto = new AnnounceDto();
            announceDto.setId(announce.getAnnouncementId());
            announceDto.setAnnouncementTitle(announce.getAnnouncementTitle());
            announceDto.setPublishDate(announce.getPublishDate());
            announceDto.setCloseDate(announce.getCloseDate());
            announceDto.setAnnouncementDisplay(announce.getAnnouncementDisplay());

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(announce.getCategory().getCategoryName());
            announceDto.setCategory(categoryDto);

            announceDtos.add(announceDto);
        }

        return announceDtos;
    }

    @GetMapping("/{announceId}")
    public DetailDto getAnnounceById(@PathVariable("announceId") Integer announceId,
                                     @RequestParam(value = "count", required = false) Boolean count) {
        Announce announce = announceService.getAnnounceById(announceId, count);

        DetailDto detailDto = new DetailDto();
        detailDto.setId(announce.getAnnouncementId());
        detailDto.setAnnouncementTitle(announce.getAnnouncementTitle());
        detailDto.setAnnouncementDescription(announce.getAnnouncementDescription());
        detailDto.setPublishDate(announce.getPublishDate());
        detailDto.setCloseDate(announce.getCloseDate());
        detailDto.setAnnouncementDisplay(announce.getAnnouncementDisplay());
        detailDto.setCategory(new CategoryDto());
        detailDto.setAnnouncementCategory(announce.getCategory().getCategoryName());
        detailDto.setView(announce.getView());

        return detailDto;
    }

    @DeleteMapping("{announceId}")
    public void removeAnnounce(@PathVariable Integer announceId) {
        announceService.removeAnnounce((announceId));
    }

    @PostMapping("")
    public ResponseEntity<DetailDto> addNewAnnounce(@RequestBody Announce newAnnounce) {
        Announce announce = announceService.addNewAnnounce(newAnnounce);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(announce.getCategory().getCategoryId());

        DetailDto detailDto = new DetailDto();
        detailDto.setId(announce.getAnnouncementId());
        detailDto.setAnnouncementTitle(announce.getAnnouncementTitle());
        detailDto.setAnnouncementDescription(announce.getAnnouncementDescription());
        detailDto.setPublishDate(announce.getPublishDate());
        detailDto.setCloseDate(announce.getCloseDate());
        detailDto.setCategory(categoryDto);
        detailDto.setAnnouncementDisplay(announce.getAnnouncementDisplay());
        detailDto.setView(announce.getView());

        return ResponseEntity.ok(detailDto);
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<DetailDto> updateAnnounce(
            @PathVariable Integer announcementId,
            @RequestBody Announce newAnnounce) {
        Announce announce = announceService.updateAnnounce(announcementId, newAnnounce);

        CategoryDto categoryDto = new CategoryDto();
        if (announce.getCategory() != null) {
            categoryDto.setCategoryId(announce.getCategory().getCategoryId());
        }

        DetailDto detailDto = new DetailDto();
        detailDto.setId(announce.getAnnouncementId());
        detailDto.setAnnouncementTitle(announce.getAnnouncementTitle());
        detailDto.setAnnouncementDescription(announce.getAnnouncementDescription());
        detailDto.setPublishDate(announce.getPublishDate());
        detailDto.setCloseDate(announce.getCloseDate());
        detailDto.setAnnouncementDisplay(announce.getAnnouncementDisplay());
        detailDto.setView(announce.getView());
        detailDto.setCategory(categoryDto);

        return ResponseEntity.ok(detailDto);
    }
    @GetMapping("/pages")
    public PageDto<AnnounceDto> getAnnouncePage(@RequestParam(defaultValue = "admin") String mode,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "5") Integer size,
                                                @RequestParam(defaultValue = "0") Integer category) {
        Page<Announce> announcesPage;

        if (category == null || category == 0) {
            announcesPage = announceService.getAnnouncePage(mode, page, size);
        } else {
            announcesPage = announceService.getAnnounceByCategoryId(mode, page, size, category);
        }

        List<AnnounceDto> announceDtos = new ArrayList<>();
        for (Announce announce : announcesPage.getContent()) {
            AnnounceDto announceDto = new AnnounceDto();
            announceDto.setId(announce.getAnnouncementId());
            announceDto.setAnnouncementTitle(announce.getAnnouncementTitle());
            announceDto.setPublishDate(announce.getPublishDate());
            announceDto.setCloseDate(announce.getCloseDate());
            announceDto.setAnnouncementDisplay(announce.getAnnouncementDisplay());
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(announce.getCategory().getCategoryName());
            announceDto.setCategory(categoryDto);
            announceDtos.add(announceDto);
        }

        PageDto<AnnounceDto> pageDTO = new PageDto<>();
        pageDTO.setContent(announceDtos);
        pageDTO.setLast(announcesPage.isLast());
        pageDTO.setFirst(announcesPage.isFirst());
        pageDTO.setTotalPages(announcesPage.getTotalPages());
        pageDTO.setTotalElements((int) announcesPage.getTotalElements());
        pageDTO.setSize(announcesPage.getSize());
        pageDTO.setPage(page);
        pageDTO.setNumber(announcesPage.getNumber());

        return pageDTO;
    }
}

