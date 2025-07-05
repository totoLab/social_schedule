package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.schedule_manager.Schedule;
import com.rcyouth.socialschedule.service.ScheduleService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // Request DTO for schedule generation
    static class GenerateScheduleRequest {
        public int year;
        public int startMonth;
        public int endMonth;
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> generateSchedule(@RequestParam GenerateScheduleRequest generateScheduleRequest) {
        List<Schedule> schedules = new ArrayList<>(); // placeholder
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/{scheduleName}/{year}/{month}/generate")
    public ResponseEntity<Schedule> generateSchedule(@PathVariable String scheduleName, @RequestBody GenerateScheduleRequest request) {
        Schedule generatedSchedule = scheduleService.generateSchedule(scheduleName, request.year, request.startMonth, request.endMonth);
        return ResponseEntity.ok(generatedSchedule);
    }

    @PostMapping("/{scheduleName}/{year}/{month}/save")
    public ResponseEntity<Schedule> saveSchedule(@PathVariable String scheduleName, @RequestBody Schedule scheduleToSave) {
        Schedule savedSchedule = scheduleService.saveSchedule(scheduleName, scheduleToSave);
        return ResponseEntity.ok(savedSchedule);
    }

    @GetMapping("/{scheduleName}/{year}/{month}")
    public ResponseEntity<Schedule> getScheduleByMonth(@PathVariable String scheduleName, @PathVariable int year, @PathVariable int month) {
        Schedule schedule = scheduleService.getScheduleByMonth(scheduleName, year, month);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{scheduleName}/{year}")
    public ResponseEntity<Schedule> getScheduleByYear(@PathVariable String scheduleName, @PathVariable int year) {
        Schedule schedule = scheduleService.getScheduleByYear(scheduleName, year);
        return ResponseEntity.ok(schedule);
    }

}
