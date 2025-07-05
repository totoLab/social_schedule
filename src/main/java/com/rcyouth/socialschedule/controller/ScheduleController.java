package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.model.Schedule;
import com.rcyouth.socialschedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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