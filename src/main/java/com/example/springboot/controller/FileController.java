package com.example.springboot.controller;

import com.example.springboot.service.Services;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("v1/files/")
public class FileController {
    private final Services services;

    @Autowired
    public FileController(Services services) {
        this.services = services;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
            @RequestParam("destination") String dest)
            throws Exception {
        services.store(file, dest);
        return ResponseEntity.ok("Successfully uploaded");
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> delete(@RequestParam("path") Path file) throws IOException {
        services.delete(file);
        return ResponseEntity.ok("Successfully deleted");
    }

    @GetMapping("/get")
    public ResponseEntity<FileSystemResource> downloadSingleFile(@RequestParam("file") Path file) {
        FileSystemResource res;
        try {
            res = services.downloadSingleFile(file);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-folder")
    public ResponseEntity<String> createFolder(@RequestParam("folder") Path path) {
        try {
            services.createFolder(path);
            return ResponseEntity.ok("Folder successfully created");
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dir-content")
    public ResponseEntity<List<Path>> getDirContent(@RequestParam("path") Path path) {
        try {
            List<Path> res = services.getDirContent(path);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }
}
