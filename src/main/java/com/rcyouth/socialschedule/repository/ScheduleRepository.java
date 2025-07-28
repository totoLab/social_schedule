package com.rcyouth.socialschedule.repository;

import com.rcyouth.socialschedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByScheduledDateBetween(LocalDate startDate, LocalDate endDate);

    List<Schedule> findByPersonIdAndScheduledDateBetween(Long personId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByPersonIdAndContentTypeNameIgnoreCaseAndScheduledDateBetween(Long personId, String contentType, LocalDate startDate, LocalDate endDate);
}
