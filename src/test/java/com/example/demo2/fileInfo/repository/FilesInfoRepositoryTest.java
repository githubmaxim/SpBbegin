package com.example.demo2.fileInfo.repository;


import com.example.demo2.entity.filesInfo.FilesInfo;
import com.example.demo2.repository.filesInfo.FilesInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.demo2.fileInfo.prototype.FilesInfoPrototypeForUnitTest.aFilesInfo1;
import static com.example.demo2.fileInfo.prototype.FilesInfoPrototypeForUnitTest.aFilesInfo2;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:myTestApplication.properties") //подгружает не стандартный "application.properties",
// а необходимый "myTestApplication.properties". В "myTestApplication.properties" для тестирования без обращения к реальной
// (MySQL) БД создаем подключение к внутренней "H2" БД. Для того, чтобы это сработало необходимо раскомментировать или
// создать (если нет) в "pom.xml" блок подключающий БД "H2".
public class FilesInfoRepositoryTest {

    @Autowired
    private FilesInfoRepository filesInfoRepository;

    @Test
    void deleteByName() throws InterruptedException {
        filesInfoRepository.save(aFilesInfo1());
        Optional<FilesInfo> filesInfo1 = filesInfoRepository.findById(1);
        assertThat(filesInfo1).isNotNull(); //вначале проверяем наличие записанных данных в БД
        filesInfoRepository.deleteByName(aFilesInfo1().getName());
        Optional<FilesInfo> filesInfo = filesInfoRepository.findById(1);
        assertThat(filesInfo).isEmpty(); //теперь уверенно можно проверять удаление
    }
}
