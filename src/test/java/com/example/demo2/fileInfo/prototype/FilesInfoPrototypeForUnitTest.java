package com.example.demo2.fileInfo.prototype;

import com.example.demo2.dto.filesInfo.FilesInfoDto;
import com.example.demo2.entity.filesInfo.FilesInfo;

import java.time.LocalDate;

public class FilesInfoPrototypeForUnitTest {

    //при Unit-тестировании при создании объекта задавать нужно все поля включая "id"

    public static FilesInfo aFilesInfo1() {
        return FilesInfo.builder()
                .id(1)
                .name("forDownload1.txt")
                .size(1L)
                .myKey("asdf")
                .uploadDate(LocalDate.now())
                .build();
    }

    public static FilesInfo aFilesInfo2() {
        return FilesInfo.builder()
                .id(2)
                .name("forDownload2.txt")
                .size(2L)
                .myKey("zxcv")
                .uploadDate(LocalDate.now())
                .build();
    }

    public static FilesInfoDto aFilesInfoDto() {
        return FilesInfoDto.builder()
                .id(1)
                .name("forDownload1.txt")
                .size(1L)
                .myKey("asdf")
                .uploadDate(LocalDate.now())
                .build();
    }

}
