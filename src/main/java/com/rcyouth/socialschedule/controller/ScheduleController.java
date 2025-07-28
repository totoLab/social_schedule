package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.model.Schedule;
import com.rcyouth.socialschedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<Schedule>> getSchedulesByMonth(@PathVariable int year, @PathVariable int month) {
        List<Schedule> schedules = scheduleService.getSchedulesByMonth(year, month);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{year}")
    public ResponseEntity<List<Schedule>> getSchedulesByYear(@PathVariable int year) {
        List<Schedule> schedules = scheduleService.getSchedulesByYear(year);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/person/{personId}/{year}/{month}/content-type/{type}")
    public List<Schedule> getPersonMonthlyContentWithType(@PathVariable long personId, @PathVariable int year, @PathVariable int month, @PathVariable String type) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return scheduleService.getPersonContentWithType(personId, startDate, endDate, type);
    }

    @GetMapping("/person/{personId}/{year}/content-type/{type}")
    public List<Schedule> getPersonMonthlyContentWithType(@PathVariable long personId, @PathVariable int year, @PathVariable String type) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return scheduleService.getPersonContentWithType(personId, startDate, endDate, type);
    }

    @PostMapping
    public ResponseEntity<Schedule> saveSchedule(@RequestBody Schedule schedule) {
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        return ResponseEntity.ok(savedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}