package com.example.springboot.client;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Client {
    private static final String LOGIN = "http://localhost:8080/v1";
    private static final String UPLOAD_FILE = "http://localhost:8080/v1/files/upload";
    private static final String DELETE_FILE = "http://localhost:8080/v1/files/delete";
    private static final String DOWNLOAD_FILE = "http://localhost:8080/v1/files/get";
    private static final String CREATE_FOLDER = "http://localhost:8080/v1/files/create-folder";
    private static final String GET_FOLDER_CONTENT = "http://localhost:8080/v1/files/dir-content";

    private RestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();

    public Client() {
        restTemplate = new RestTemplate();
    }

    public String uploadFile(Path path, Path destination) {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("destination", destination.toString());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(UPLOAD_FILE, request, String.class);

        return response.getBody();
    }

    public String deleteFile(Path file) {
        ResponseEntity<String> response = restTemplate.exchange(
                DELETE_FILE + String.format("?path=%s", file.toString()),
                HttpMethod.DELETE,
                null,
                String.class);

        return response.getBody();
    }

    public void downloadSingleFile(Path path, Path dest) throws IOException {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(DOWNLOAD_FILE +
                String.format("?file=%s", path.toString()), byte[].class);

        Files.createDirectories(dest);
        Files.write(dest.resolve(path.getFileName()), response.getBody());
    }

    public String createFolder(Path path) {
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(CREATE_FOLDER + "?folder=%s", path),
                null, String.class);

        return response.getBody();
    }

    public List<String> getDirContent(Path path) {
        ResponseEntity<String[]> response = restTemplate
                .getForEntity(GET_FOLDER_CONTENT + String.format("?path=%s", path.toString()), String[].class);
        return Arrays.asList(response.getBody());
    }

}
