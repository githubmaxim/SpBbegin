package com.example.demo2.fileInfo.service;

import com.example.demo2.repository.filesInfo.FilesInfoRepository;
import com.example.demo2.service.filesInfo.DefaultFilesInfoService;
import com.example.demo2.service.filesInfo.FilesInfoConverter;
import com.example.demo2.service.filesInfo.FilesInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.example.demo2.fileInfo.prototype.FilesInfoPrototypeForUnitTest.aFilesInfo1;
import static com.example.demo2.fileInfo.prototype.FilesInfoPrototypeForUnitTest.aFilesInfo2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultFilesInfoServiceTest {

    private FilesInfoRepository filesInfoRepository;
    private FilesInfoConverter filesInfoConverter;
    private FilesInfoService filesInfoService;

    @BeforeEach
    void setUp() {
        filesInfoRepository = mock(FilesInfoRepository.class);
        filesInfoConverter = new FilesInfoConverter();
        filesInfoService = DefaultFilesInfoService.builder().filesInfoRepository(filesInfoRepository).filesInfoConverter(filesInfoConverter).build();
    }

    @Test
    void writeAndReadAndDeleteFileInWorkDirectoryTest() throws Exception {
        String directory = "src//main//java//com//example//demo2//forDownload//";
        String fileName = "forDownload1.txt";
        Path path = Paths.get(directory + "/" + fileName);
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        String content = ""; //создаем пустой контент, т.к. и файл выше ("forDownload1.txt") тоже создан пустым
        MockMultipartFile multipartFile = new MockMultipartFile("forDownload2", "forDownload2.txt", "txt", content.getBytes()); //создаем заглушку файла которая просто должна вернуть размер такой же как и у файла созданного выше (для последней из проверок)

        assertThat(Files.exists(path)).isTrue();
        assertThat(Files.isRegularFile(path)).isTrue();
        assertThat(Files.size(path)).isEqualTo(multipartFile.getSize());

        assertThat(Files.isReadable(path)).isTrue();

        Files.delete(path);
        assertThat(Files.notExists(path)).isTrue();
    }

    @Test
    void findAllFilesName() {
        when(filesInfoRepository.findAll()).thenReturn(Arrays.asList(aFilesInfo1(), aFilesInfo2()));
        List<String> fileInfoDtoList = filesInfoService.findAllFilesName(); //возвращает только наименования файлов

        assertThat(fileInfoDtoList).isNotNull();
        assertThat(fileInfoDtoList.get(0)).isEqualTo("forDownload1.txt");
        assertThat(fileInfoDtoList.get(1)).isEqualTo("forDownload2.txt");
    }
}