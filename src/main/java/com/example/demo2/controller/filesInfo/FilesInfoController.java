package com.example.demo2.controller.filesInfo;

import com.example.demo2.service.filesInfo.FilesInfoService;
import com.example.demo2.service.filesInfo.MediaTypeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * The Controller file that serves the functions of the "workingWithFiles.js" file used on the "workingWithFiles.html" page.
 *
 * @author Maxim
 * @version 1.0
 */
@RestController
@RequestMapping("/filesInfo")
@AllArgsConstructor
@Slf4j
public class FilesInfoController {

    private final FilesInfoService filesInfoService;

    @Autowired
    private ServletContext servletContext;

    //!!!Исключения в методах и тем более проброс через "throws" я не применяю, т.к. работаю через механизм проверки полей через if-ы и пишу код сам для себя!!!

    /**
     * This method is used to run the "singleFileUpload()" class "DefaultFilesInfoService"(interface "FilesInfoService") method.
     * @param file file uploading to the server
     * @return status "200"|"444" and "file uploaded"or"Error"|"Not file for download"
     */
    @PostMapping("/upload")
    public ResponseEntity<?> singleFileUpload(@RequestPart(value = "file", required = false) MultipartFile file) {
        log.info("+++message by FilesInfoController, method singleFileUpload+++ file = " + file);
        if (file.isEmpty()) {
            return ResponseEntity.status(444).body("Not file for download");
        } else {
            return ResponseEntity.status(200).body(filesInfoService.singleFileUpload(file));
        }
    }


    //Тут два варианта написания для получения параметра: через @RequestParam или @PathVariable

    /**
     * This method is used to run the "downloadFile()" class "DefaultFilesInfoService"(interface "FilesInfoService") method.
     * @param fileName name of the file downloading from the server
     * @return status "200"|"444" and  downloading file|"Not file for download"
     */
    @GetMapping("/download")
//    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@RequestParam(value = "param1", required = false, defaultValue = "forDownload.doc") String fileName) {
//    public ResponseEntity<?> downloadFile(@PathVariable Optional<String> fileName) {
        log.info("+++message by FilesInfoController, method download+++");
        log.info("FilesInfoController: Handling find by fileName: " + fileName);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName); // создается типа файла, который будет вставляться в тело ответа
        if (filesInfoService.downloadFile(fileName).equals("Not file for download")) {
            return ResponseEntity.status(444).body("Not file for download");
        } else {
            return ResponseEntity
                    .ok()
                    .headers(filesInfoService.headerForDownloadingFile(fileName))
                    .contentType(mediaType)
                    .contentLength(filesInfoService.lengthForDownloadingFile(fileName).length)
                    .body(filesInfoService.downloadFile(fileName));
        }
    }

    /**
     * This method is used to run the "findAllFilesName()" class "DefaultFilesInfoService"(interface "FilesInfoService") method.
     * @return all data from entity "FilesInfo"
     */
    @GetMapping("/findAllFilesName")
    public List<String> findAllFilesName() {
        log.info("+++message by FilesInfoController, method findAllFilesName+++");
        log.info("UserController: Handling find all users request");
        return filesInfoService.findAllFilesName();
    }

    /**
     * This method is used to run the "deleteFile()" class "DefaultFilesInfoService"(interface "FilesInfoService") method.
     * @param fileName name of the file to be deleted on the server
     * @return status "200"
     */
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        log.info("+++message by UserController, method deleteUsers+++");
        log.info("UserController: Handling delete user request: " + fileName);
        filesInfoService.deleteFile(fileName);
        return ResponseEntity.status(200).build();
    }

}
