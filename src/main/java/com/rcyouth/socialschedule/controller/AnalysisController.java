package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/person/{personId}/{year}/{month}")
    public ResponseEntity<Map<String, Object>> analyzePersonContent(@PathVariable Long personId, @PathVariable int year, @PathVariable int month) {
        Map<String, Object> analysisResult = analysisService.analyzePersonContent(personId, year, month);
        return ResponseEntity.ok(analysisResult);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<Map<String, Object>> analyzeMonth(@PathVariable int year, @PathVariable int month) {
        Map<String, Object> analysisResult = analysisService.analyzeMonth(year, month);
        return ResponseEntity.ok(analysisResult);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, Object>> analyzeYear(@PathVariable int year) {
        Map<String, Object> analysisResult = analysisService.analyzeYear(year);
        return ResponseEntity.ok(analysisResult);
    }
}