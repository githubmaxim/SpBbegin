package com.example.demo2.service.filesInfo;

import com.example.demo2.dto.filesInfo.FilesInfoDto;
import com.example.demo2.entity.filesInfo.FilesInfo;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Builder
public class FilesInfoConverter {
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
