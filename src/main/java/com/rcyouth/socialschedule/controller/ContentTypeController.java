package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.model.ContentType;
import com.rcyouth.socialschedule.service.ContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content-types")
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    @Autowired
    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ContentType>> getAllContentTypes() {
        List<ContentType> contentTypes = contentTypeService.getAllContentTypes();
        return ResponseEntity.ok(contentTypes);
    }

    @PostMapping
    public ResponseEntity<ContentType> saveContentType(@RequestBody ContentType contentType) {
        ContentType savedContentType = contentTypeService.saveContentType(contentType);
        return ResponseEntity.ok(savedContentType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentType(@PathVariable Long id) {
        contentTypeService.deleteContentType(id);
        return ResponseEntity.noContent().build();
    }
}
