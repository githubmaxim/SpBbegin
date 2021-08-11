package com.example.demo2.service.filesInfo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesInfoService {

    String singleFileUpload(MultipartFile file) throws IOException;

    ResponseEntity<?> downloadFile(String fileName) throws IOException;

    List<String> findAllFilesName();

    void deleteFile(String fileName);
}
