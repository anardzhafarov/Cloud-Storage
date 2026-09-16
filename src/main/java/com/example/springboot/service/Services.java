package com.example.springboot.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@Service
public class Services {
    private static final Path basePath = Path.of("C:/Users/SellIT/Desktop/Cloud Memory");

    public static Path getBasePath() {
        return basePath;
    }


    public void store(MultipartFile file, String dest) throws Exception {
        Path destination = basePath.resolve(Path.of(dest)).normalize();
        if(!destination.startsWith(basePath))
            throw new IllegalArgumentException("Destination unavailabe");

        String name = file.getOriginalFilename();
        Path path = destination.resolve(name);
        byte[] bytes = file.getBytes();

        Files.createDirectories(destination);
        Files.write(path, bytes);
    }

    public void delete(Path file) throws IOException {
        file = basePath.resolve(file).normalize();
        if(!file.startsWith(basePath)) {
            throw new IllegalArgumentException("Destination unavailable");
        }

        Files.delete(file);
    }

    public FileSystemResource downloadSingleFile(Path file){
        file = basePath.resolve(file).normalize();
        if(!file.startsWith(basePath)) {
            throw new IllegalArgumentException("Destination unavailable");
        }

        if(Files.isRegularFile(file))
            return new FileSystemResource(file);
        else
            throw new IllegalArgumentException("Object is no File");
    }

    public void createFolder(Path folder) throws IOException {
        folder = basePath.resolve(folder).normalize();
        if(!folder.startsWith(basePath)){
            throw new IllegalArgumentException("Destination unavailable");
        }

        Files.createDirectories(folder);
    }

    public List<Path> getDirContent(Path path) throws IOException {
        path = basePath.resolve(path).normalize();
        if(!path.startsWith(basePath))
            throw new IllegalArgumentException("Destination unavailable");

        try (Stream<Path> stream = Files.list(path)) {
            List<Path> list = stream.toList();
            List<Path> res = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                Path curr = list.get(i);
                curr = curr.getName(curr.getNameCount() - 1);
                res.add(curr);
            }
            return res;
        }
    }

    @PutMapping({"/rename", "/move"})
    public void move(){

    }
}