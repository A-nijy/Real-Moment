package rm.backend.domain.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.backend.domain.entity.Announcement;
import rm.backend.domain.repository.custom.AnnouncementRepositoryCustom;

public interface AnnouncementRepositoryRepository extends JpaRepository<Announcement, Long>, AnnouncementRepositoryCustom {

    @Modifying
    @Transactional
    @Query("UPDATE Announcement a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void viewCount(@Param("id") Long id);
}
