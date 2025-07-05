package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedules/{scheduleName}/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/person/{personName}")
    public ResponseEntity<Map<String, Object>> analyzePersonContent(@PathVariable String scheduleName, @PathVariable String personName) {
        Map<String, Object> analysisResult = analysisService.analyzePersonContent(scheduleName, personName);
        return ResponseEntity.ok(analysisResult);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<Map<String, Object>> analyzeMonth(@PathVariable String scheduleName, @PathVariable int year, @PathVariable int month) {
        Map<String, Object> analysisResult = analysisService.analyzeMonth(scheduleName, year, month);
        return ResponseEntity.ok(analysisResult);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, Object>> analyzeYear(@PathVariable String scheduleName, @PathVariable int year) {
        Map<String, Object> analysisResult = analysisService.analyzeYear(scheduleName, year);
        return ResponseEntity.ok(analysisResult);
    }
}
