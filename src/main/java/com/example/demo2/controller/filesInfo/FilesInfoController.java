package com.example.demo2.controller.filesInfo;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.service.filesInfo.FilesInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/filesInfo")
@AllArgsConstructor
@Slf4j
public class FilesInfoController {

    private final FilesInfoService filesInfoService;

    @PostMapping("/upload")
    public ResponseEntity<?> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("+++message by FilesInfoController, method singleFileUpload+++ file = " + file);
        if (file.isEmpty()) {
            return ResponseEntity.status(444).body("Not file for download");
        } else {
            return ResponseEntity.status(200).body(filesInfoService.singleFileUpload(file));
        }
    }

    //Тут два варианта написания для получения параметра: через @RequestParam или @PathVariable
    @GetMapping("/download")
//    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@RequestParam(value = "param1", required = false, defaultValue = "forDownload.doc") String fileName) throws IOException {
//    public ResponseEntity<?> downloadFile(@PathVariable Optional<String> fileName) throws IOException {
        log.info("+++message by FilesInfoController, method download+++");
        log.info("FilesInfoController: Handling find by fileName: " + fileName);
        if (fileName.equals("empty")) {
//        if (fileName.get().equals("empty")) {
            return filesInfoService.downloadFile("forDownload.doc");
        } else {
            return filesInfoService.downloadFile(fileName);
//            return usersService.downloadFile(fileName.get());
        }
    }

    @GetMapping("/findAllFilesName")
    public List<String> findAllFilesName() {
        log.info("+++message by FilesInfoController, method findAllFilesName+++");
        log.info("UserController: Handling find all users request");
        return filesInfoService.findAllFilesName();
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        log.info("+++message by UserController, method deleteUsers+++");
        log.info("UserController: Handling delete user request: " + fileName);
        filesInfoService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

}
