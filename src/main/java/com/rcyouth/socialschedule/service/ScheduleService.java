package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.model.Schedule;
import com.rcyouth.socialschedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getSchedulesByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return scheduleRepository.findByScheduledDateBetween(startDate, endDate);
    }

    public List<Schedule> getSchedulesByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = startDate.plusYears(1).minusDays(1);
        return scheduleRepository.findByScheduledDateBetween(startDate, endDate);
    }

    public List<Schedule> getPersonContentWithType(long personId, LocalDate startDate, LocalDate endDate, String type) {
        List<Schedule> schedules = scheduleRepository.findByPersonIdAndContentTypeNameIgnoreCaseAndScheduledDateBetween(personId, type, startDate, endDate);
        return schedules.stream()
                .sorted((o1, o2) -> o1.getScheduledDate().compareTo(o2.getScheduledDate()))
                .collect(Collectors.toList());
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}