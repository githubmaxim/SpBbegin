package com.example.demo2.service.filesInfo;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Interface with method templates for the “DefaultFilesInfoService” class.
 *
 * @author Maxim
 * @version 1.0
 */
public interface FilesInfoService {

    String singleFileUpload(MultipartFile file);

    Object downloadFile(String fileName);
    HttpHeaders headerForDownloadingFile(String fileName);
    byte[] lengthForDownloadingFile(String fileName);

    List<String> findAllFilesName();

    void deleteFile(String fileName);
}
