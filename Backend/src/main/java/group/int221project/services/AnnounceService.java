package group.int221project.services;

import group.int221project.entities.Announce;
import group.int221project.entities.Category;
import group.int221project.exceptions.AnnounceNotFound;
import group.int221project.repositories.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnounceService {
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private CategoryService categoryService;
    private final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private void formatDateTime(DateTimeFormatter formatter, Announce announce) {
        if (announce.getPublishDate() != null) {
            LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
            ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
            Instant instantPublishDate = zonePublishDate.toInstant();
            announce.setPublishDate(instantPublishDate.toString());
        }
        if (announce.getCloseDate() != null) {
            LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
            ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
            Instant instantCloseDate = zoneCloseDate.toInstant();
            announce.setCloseDate(instantCloseDate.toString());
        }
    }

    public List<Announce> getAllAnnouncements(String mode) {
        List<Announce> announces = new ArrayList<>();

        if (mode == null || mode.equals("admin")) {
            announces = announceRepository.findAllByOrderByAnnouncementIdDesc();
        } else if (mode.equals("active")) {
            List<Announce> activeAnnounces = new ArrayList<>();

            activeAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull("Y"));

            activeAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore("Y", Instant.now().toString()));

            activeAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter("Y", Instant.now().toString(), Instant.now().toString()));

            activeAnnounces.addAll(announceRepository.findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter("Y", Instant.now().toString()));

            announces.addAll(activeAnnounces.stream().distinct().collect(Collectors.toList()));
        } else if (mode.equals("close")) {
            announces = announceRepository.findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBeforeOrderByAnnouncementIdDesc("Y", Instant.now().toString());
        }

        announces.sort(Comparator.comparing(Announce::getAnnouncementId).reversed());

        announces.forEach(announce -> formatDateTime(formatter, announce));
        return announces;
    }

    public Announce getAnnounceById(Integer announceId, Boolean count) {
        Announce announce = announceRepository.findById(announceId)
                .orElseThrow(() -> new AnnounceNotFound(announceId));
        if (count != null && count) {
            incrementAnnounceView(announce);
        }

        formatDateTime(formatter, announce);

        return announce;
    }
    private void incrementAnnounceView(Announce announce) {
        announce.setView(announce.getView() + 1);
        announceRepository.saveAndFlush(announce);
    }


    public Announce addNewAnnounce(Announce newAnnounce) {
        if (newAnnounce.getPublishDate() != null && newAnnounce.getPublishDate().isEmpty()) {
            newAnnounce.setPublishDate(null);
        }

        if (newAnnounce.getCloseDate() != null && newAnnounce.getCloseDate().isEmpty()) {
            newAnnounce.setCloseDate(null);
        }

        newAnnounce.setCategory(categoryService.getCategoryById(newAnnounce.getCategoryId()));

        DateTimeFormatter testFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatPublishAndClose(newAnnounce, testFormatter, utcFormatter, localFormatter);

        Announce announce = announceRepository.save(newAnnounce);
        formatDateTime(localFormatter, announce);
        return announce;
    }

    private void formatPublishAndClose(Announce announce, DateTimeFormatter testFormatter, DateTimeFormatter utcFormatter, DateTimeFormatter localFormatter) {
        if (announce.getPublishDate() != null) {
            if (announce.getPublishDate().length() == 24) {
                LocalDateTime utcDateTime = LocalDateTime.parse(announce.getPublishDate(), testFormatter);
                announce.setPublishDate(utcDateTime.format(utcFormatter));
            } else {
                LocalDateTime localDateTime = LocalDateTime.parse(announce.getPublishDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
                announce.setPublishDate(localDateTime.format(localFormatter));
            }
        }

        if (announce.getCloseDate() != null) {
            if (announce.getCloseDate().length() == 24) {
                LocalDateTime utcDateTime = LocalDateTime.parse(announce.getCloseDate(), testFormatter);
                announce.setCloseDate(utcDateTime.format(utcFormatter));
            } else {
                LocalDateTime localDateTime = LocalDateTime.parse(announce.getCloseDate(), utcFormatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
                announce.setCloseDate(localDateTime.format(localFormatter));
            }
        }
    }

    public void removeAnnounce(Integer announcementId) {
        Announce announce = announceRepository.findById(announcementId)
                .orElseThrow(() -> new AnnounceNotFound(announcementId));
        announceRepository.delete(announce);
    }

    public Announce updateAnnounce(Integer announcementId, Announce newAnnounce) {
        Announce announce = announceRepository.findById(announcementId)
                .orElseThrow(() -> new AnnounceNotFound(announcementId));

        Category category = categoryService.getCategoryById(newAnnounce.getCategoryId());
        newAnnounce.setCategory(category);

        DateTimeFormatter testFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        formatPublishAndClose(newAnnounce, testFormatter, utcFormatter, localFormatter);

        announce.setAnnouncementTitle(newAnnounce.getAnnouncementTitle());
        announce.setAnnouncementDescription(newAnnounce.getAnnouncementDescription());
        announce.setAnnouncementDisplay(newAnnounce.getAnnouncementDisplay());
        announce.setPublishDate(newAnnounce.getPublishDate());
        announce.setCloseDate(newAnnounce.getCloseDate());
        announce.setCategory(newAnnounce.getCategory());

        Announce announceSave = announceRepository.saveAndFlush(announce);
        formatDateTime(localFormatter, announceSave);
        return announceSave;
    }

    private boolean isActiveAnnouncement(Announce announce) {
        if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
            if (announce.getPublishDate() != null && announce.getCloseDate() == null) {
                LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                Instant instantPublishDate = zonePublishDate.toInstant();
                return Instant.now().compareTo(instantPublishDate) >= 0;
            } else if (announce.getPublishDate() != null && announce.getCloseDate() != null) {
                LocalDateTime localPublishDate = LocalDateTime.parse(announce.getPublishDate(), formatter);
                ZonedDateTime zonePublishDate = localPublishDate.atZone(ZoneId.of("UTC"));
                Instant instantPublishDate = zonePublishDate.toInstant();
                LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                Instant instantCloseDate = zoneCloseDate.toInstant();
                return Instant.now().compareTo(instantPublishDate) >= 0 &&
                        Instant.now().compareTo(instantCloseDate) < 0;
            } else if (announce.getPublishDate() == null && announce.getCloseDate() != null) {
                LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                Instant instantCloseDate = zoneCloseDate.toInstant();
                return Instant.now().compareTo(instantCloseDate) < 0;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isClosedAnnouncement(Announce announce) {
        if (String.valueOf(announce.getAnnouncementDisplay()).equals("Y")) {
            if (announce.getCloseDate() != null) {
                LocalDateTime localCloseDate = LocalDateTime.parse(announce.getCloseDate(), formatter);
                ZonedDateTime zoneCloseDate = localCloseDate.atZone(ZoneId.of("UTC"));
                Instant instantCloseDate = zoneCloseDate.toInstant();
                return Instant.now().compareTo(instantCloseDate) >= 0;
            }
        }
        return false;
    }

    void formatAndPrint(List<Announce> announces) {
        announces.forEach(announce -> formatDateTime(formatter, announce));
    }

    public Page<Announce> getAnnouncePage(String mode, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Announce> announces;

        switch (mode) {
            case "admin":
                size = 9;
                pageable = PageRequest.of(page, size, sort);
                announces = announceRepository.findAll(pageable).getContent();
                break;
            case "active":
                announces = announceRepository.findAll()
                        .stream()
                        .filter(announce -> isActiveAnnouncement(announce))
                        .sorted(Comparator.comparing(Announce::getAnnouncementId, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                break;
            case "close":
                announces = announceRepository.findAll()
                        .stream()
                        .filter(announce -> isClosedAnnouncement(announce))
                        .sorted(Comparator.comparing(Announce::getAnnouncementId, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                break;
            default:
                return new PageImpl<>(Collections.emptyList());
        }

        formatAndPrint(announces);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), announces.size());
        return new PageImpl<>(announces.subList(start, end), pageable, announces.size());
    }

    public Page<Announce> getAnnounceByCategoryId(String mode, Integer page, Integer size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "announcementId");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Announce> announces;

        if (mode == null || mode.equals("admin")) {
            size = 9;
            pageable = PageRequest.of(page, size, sort);
            announces = announceRepository.findAllByCategoryId(categoryId, pageable).getContent();
        } else if (mode.equals("active")) {
            announces = announceRepository.findAllByCategoryId(categoryId)
                    .stream()
                    .filter(announce -> String.valueOf(announce.getAnnouncementDisplay()).equals("Y"))
                    .filter(announce -> isActiveAnnouncement(announce))
                    .sorted(Comparator.comparing(Announce::getAnnouncementId, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else if (mode.equals("close")) {
            announces = announceRepository.findAllByCategoryId(categoryId)
                    .stream()
                    .filter(announce -> String.valueOf(announce.getAnnouncementDisplay()).equals("Y"))
                    .filter(announce -> isClosedAnnouncement(announce))
                    .sorted(Comparator.comparing(Announce::getAnnouncementId, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            return new PageImpl<>(Collections.emptyList());
        }

        formatAndPrint(announces);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), announces.size());
        return new PageImpl<>(announces.subList(start, end), pageable, announces.size());
    }
}