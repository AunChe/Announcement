package group.int221project.repositories;

import group.int221project.entities.Announce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announce, Integer> {
    List<Announce> findAllByOrderByAnnouncementIdDesc();
    List<Announce> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNull(String announcementDisplay);
    List<Announce> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNullAndPublishDateBefore(String announcementDisplay, String publishDate);
    List<Announce> findAllByAnnouncementDisplayAndPublishDateIsNullAndCloseDateIsNotNullAndCloseDateAfter(String announcementDisplay, String closeDate);
    List<Announce> findAllByAnnouncementDisplayAndPublishDateIsNotNullAndCloseDateIsNotNullAndPublishDateBeforeAndCloseDateAfter(String announcementDisplay, String publishDate, String closeDate);
    List<Announce> findAllByAnnouncementDisplayAndCloseDateIsNotNullAndCloseDateBeforeOrderByAnnouncementIdDesc(String announcementDisplay, String closeDate);
    List<Announce> findAllByCategoryId(Integer categoryId);
    Page<Announce> findAllByCategoryId(Integer categoryId, Pageable pageable);

}
