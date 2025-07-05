package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Month;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/schedules/{scheduleName}/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Request DTO for image generation
    static class GenerateImageRequest {
        public int year;
        public int month;
    }

    @PostMapping("/calendar")
    public ResponseEntity<String> generateCalendarImage(@PathVariable String scheduleName, @RequestBody GenerateImageRequest request) throws IOException {
        String imagePath = imageService.generateCalendarImage(scheduleName, request.year, request.month);
        return ResponseEntity.ok("{\"imagePath\": \"" + imagePath + "\"}");
    }

    @GetMapping("/calendar/{year}/{month}")
    public ResponseEntity<Resource> getCalendarImage(@PathVariable String scheduleName, @PathVariable int year, @PathVariable int month) throws IOException {
        // Assuming the image path is predictable based on scheduleName, year, and month
        // This needs to match the filename generated in ImageService
        String filename = String.format("schedule_images/%d.%s_%d_calendar.png",
                month,
                Month.of(month).toString().toLowerCase(),
                year);

        Path filePath = Paths.get(filename).toAbsolutePath().normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            String contentType = "image/png"; // Assuming PNG for now
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
