package com.example.demo2.fileInfo.controller;


import com.example.demo2.controller.filesInfo.FilesInfoController;
import com.example.demo2.service.filesInfo.FilesInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static com.example.demo2.fileInfo.prototype.FilesInfoPrototypeForUnitTest.aFilesInfoDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Тут мы делаем тестирование формируемых контроллером сетевых ответов для Клиентов

//Такое написание аннотаций дает возможность тестировать страницы с доступом для всех ролей
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(FilesInfoController.class)//@WebMvcTest вместо @SpringBootTest + говорим создать экземпляр только одного
// контроллера иначе потребует создать @MockBean-заглушки для всех сервисов и репозиториев остальных контроллеров.
// @WebMvcTest вместо @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще,
// кроме заглушки сетевого соединения, заглушку для репозитория, сервиса и т.д.)
public class FilesInfoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilesInfoService filesInfoService;

    @Test
    void uploadFileNotEmpty() throws Exception {
        when(filesInfoService.singleFileUpload(any())).thenReturn("file uploaded");

        String name = "file"; //это из "@RequestPart(value = "file", required = false)"
        String originalFileName = "chtougodno";
        String contentType = "MediaType.TEXT_PLAIN_VALUE"; //тип отправляемой на upload информации
        String content = "chtougodno"; //хотя бы один символ, чтобы было, что разбить на байты (т.е. не "file.isEmpty()")
        MockMultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content.getBytes());

        mockMvc.perform(multipart("/filesInfo/upload")
                    .file(multipartFile)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").value("file uploaded"));
    }

@Test
    void uploadFileIsEmpty() throws Exception {
        String name = "file"; //это из "@RequestPart(value = "file", required = false)"
        String originalFileName = "chtougodno";
        String contentType = "MediaType.TEXT_PLAIN_VALUE"; //тот тип информации,который будет в "body" ответа от сервера клиенту
        String content = ""; //т.е. создаем пустой контент для "file.isEmpty()"
        MockMultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content.getBytes());

        mockMvc.perform(multipart("/filesInfo/upload")
                    .file(multipartFile)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(444))
                .andExpect(jsonPath("$").value("Not file for download"));
    }



    @Test
    void downloadFileIsEmpty() throws Exception {
        when(filesInfoService.downloadFile(any())).thenReturn("Not file for download");
        mockMvc.perform(get("/filesInfo/download?param1={param}", "chtougodno"))
                .andDo(print())
                .andExpect(status().is(444))
                .andExpect(jsonPath("$").value("Not file for download"));
    }

    @Test
    void downloadFileNotEmpty() throws Exception {

        //Создаем пустой файл на диске (если такой файл уже есть тест падает), а после проверки (в самом низу) его удаляем.
        // При этом чтобы файлы, созданные в этой дирректории, каждый раз при создании не попадали под Git контроль, дирректория указана в файле ".gitignore"
        String directory = "src//test//java//com//example//demo2//fileInfo//forDownload2";
        String fileName = "forDownload.txt";
        Path path = Paths.get(directory + "/" + fileName);
        Files.createDirectories(path.getParent()); //если не создать папку(тут мы из пути "path" исключаем название файла), то при ее отсутствии тест упадет
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }

        //Создаем данные для заполнения заглушек методов
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders(); //создаем объект для дальнейшей записи в него всех необходимых заголовков (это в запросе/ответе инфа идущая после первой “стартовой” строки и до превой пустой строки, после которой идет тело сообщения)
        headers.add("Content-Disposition", String.format("attachment; filename=" + path.getFileName().toString()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        when(filesInfoService.downloadFile(any())).thenReturn(resource);
        when(filesInfoService.headerForDownloadingFile(any())).thenReturn(headers);
        when(filesInfoService.lengthForDownloadingFile(any())).thenReturn(data);

        mockMvc.perform(get("/filesInfo/download?param1={param}", "forDownload.txt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(data));

        Files.delete(path); //удаляем созданный файл
    }



    @Test
    void findAllFilesName() throws Exception {
        when(filesInfoService.findAllFilesName()).thenReturn(Collections.singletonList("Files name list"));
        mockMvc.perform(get("/filesInfo/findAllFilesName"))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList("Files name list"))))
                .andExpect(status().isOk());
    }



    @Test
    void deleteFile() throws Exception {
        doNothing().when(filesInfoService).deleteFile(aFilesInfoDto().getName()); //заглушка для void метода "deleteFile()" интерфейса filesInfoService
        mockMvc.perform(delete("/filesInfo/delete/{fileName}", aFilesInfoDto().getName()))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
