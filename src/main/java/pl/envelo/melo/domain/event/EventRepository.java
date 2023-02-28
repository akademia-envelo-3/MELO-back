package pl.envelo.melo.domain.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByStartTimeAfterAndType(LocalDateTime localDateTime, EventType eventType);

    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    List<Event> findAll(Specification<Event> spec, Sort sort);

    List<Event> findByPeriodicTypeNotAndNextEventIsNullAndUnitIsNotNull(PeriodicType none);
}
