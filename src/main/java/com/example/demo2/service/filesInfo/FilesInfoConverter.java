package com.example.demo2.service.filesInfo;

import com.example.demo2.dto.filesInfo.FilesInfoDto;
import com.example.demo2.entity.filesInfo.FilesInfo;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * File for converting the entity class “FilesInfo” to the class “FilesInfoDto” and vice versa.
 *
 * @author Maxim
 * @version 1.0
 */
@Component
@NoArgsConstructor
@Builder
public class FilesInfoConverter {

    /**
     * A method that converts the "FilesInfoDto" class into the "FilesInfo" entity class
     * @param filesInfoDto object of the class "FilesInfoDto"
     * @return object of the entity class "FilesInfo"
     */
    public FilesInfo fromFilesInfoDtoToFilesInfo(FilesInfoDto filesInfoDto) { //обычное заполнение, не через сеттеры
       return FilesInfo.builder()
       .id(filesInfoDto.getId())
               .name(filesInfoDto.getName())
               .size(filesInfoDto.getSize())
               .myKey(filesInfoDto.getMyKey())
               .uploadDate(filesInfoDto.getUploadDate())
               .build();

    //Так создавать если конструкторы класса FileInfo написаны вручную
//        FilesInfo filesInfo = new FilesInfo();
//        filesInfo.setId(filesInfoDto.getId());
//        filesInfo.setName(filesInfoDto.getName());
//        filesInfo.setSize(filesInfoDto.getSize());
//        filesInfo.setMyKey(filesInfoDto.getMyKey());
//        filesInfo.setUploadDate(filesInfoDto.getUploadDate());
//        return filesInfo;
    }

    /**
     * A method that converts the "FilesInfo" entity class into the "FilesInfoDto" class
     * @param filesInfo object of the entity class "FilesInfo"
     * @return object of the class "FilesInfoDto"
     */
    public FilesInfoDto fromFilesInfoToFilesInfoDto(FilesInfo filesInfo) { //заполнение через Builder
        return FilesInfoDto.builder()
                .id(filesInfo.getId())
                .name(filesInfo.getName())
                .size(filesInfo.getSize())
                .myKey(filesInfo.getMyKey())
                .uploadDate(filesInfo.getUploadDate())
                .build();
    }
}
