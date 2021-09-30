package com.example.demo2.service.filesInfo;

import com.example.demo2.dto.filesInfo.FilesInfoDto;
import com.example.demo2.entity.filesInfo.FilesInfo;
import com.example.demo2.repository.filesInfo.FilesInfoRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.digest.DigestUtils;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Service file that serves the functions of the "FilesInfoController" class.
 *
 * @author Maxim
 * @version 1.0
 */
@Service
@Builder
@Slf4j
public class DefaultFilesInfoService implements FilesInfoService {

    private static final String MY_DIRECTORY = "src//main//java//com//example//demo2//forDownload//"; //директория в которую/из которой будут соответственно писаться/читаться файлы
    private final FilesInfoRepository filesInfoRepository;
    private final FilesInfoConverter filesInfoConverter;


    /**
     * Method uploads a client file to the server and information about it into the “FilesInfo” entity.
     * @param file file uploading to the server
     * @return text
     */
    @Override
    public String singleFileUpload(MultipartFile file) {
        log.info("!!!message by DefaultUserService, method singleFileUpload!!!");
        try {

//            Проверяем наличие папки, при отсутствии создаем
            if (Files.notExists(Paths.get(MY_DIRECTORY))) {
                Files.createDirectory(Paths.get(MY_DIRECTORY));
            }

            // Получаем файл и сохраняем его на диске в папке
            byte[] bytes = file.getBytes(); //разбили файл на последовательность байт
            Path path = Paths.get(MY_DIRECTORY + file.getOriginalFilename()); //где getOriginalFilename() это метод интерфейса MultipartFile, возвращающий исходное имя файла в файловой системе клиента
            log.info("!!!message by DefaultUserService, method singleFileUpload!!! - path =" + path);
            Files.write(path, bytes);

            //Сохраняем в БД информацию, о файле записаном на диске
            LocalDate localDate = LocalDate.now();
            String key = generateKey(file.getOriginalFilename());
            String name = file.getOriginalFilename();
            Long size = file.getSize();

            FilesInfo filesInfo = FilesInfo.builder()
                    .name(name)
                    .size(size)
                    .myKey(key)
                    .uploadDate(localDate)
                    .build();

            filesInfoRepository.save(filesInfo);

            //Возвращаем клиенту ответ
            return "file uploaded";
        } catch (IOException e) {
            return "No \"" + file.getOriginalFilename() + "\" file loaded => " + e.getMessage();
        }
    }

    // Метод генерации ключей для метода "singleFileUpload"
    /**
     * Method for generating keys for the "singleFileUpload" method
     * @param name file name
     * @return generated key
     */
    private String generateKey(String name) {
        return DigestUtils.md2Hex(name + LocalDate.now().toString());
    }


    //Следующие три метода для формирования ответа клиенту на запрос о загрузке файла

    /**
     * Method sends the file requested by the client
     * @param fileName file name
     * @return file requested by the client
     */
    @Override
    public Object downloadFile(String fileName) {
//   В следующем блоке кода используется механизмы "NIO 2" + "IO". Тут файл перед передачей разрывается на байты.
        if (Files.notExists(Paths.get(MY_DIRECTORY + "/" + fileName))) {
            log.error("!message by DefaultUserService, method downloadFile!" + "- Not file for download");
            String resource = "Not file for download";
            return resource;
        } else {

            Path path = Paths.get(MY_DIRECTORY + "/" + fileName);
            byte[] data = new byte[0];
            try {
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                log.error("!message by DefaultUserService, method downloadFile!", e);
            }
            ByteArrayResource resource = new ByteArrayResource(data);
            return resource;
        }
    }

    /**
     * Method generates “Header”
     * @param fileName file name
     * @return generated "Header"
     */
    @Override
    public HttpHeaders headerForDownloadingFile(String fileName) {
        Path path = Paths.get(MY_DIRECTORY + "/" + fileName);
        HttpHeaders headers = new HttpHeaders(); //создаем объект для дальнейшей записи в него всех необходимых заголовков (это в запросе/ответе инфа идущая после первой “стартовой” строки и до превой пустой строки, после которой идет тело сообщения)
        headers.add("Content-Disposition", String.format("attachment; filename=" + path.getFileName().toString()));
        //следующие три строки устанавливают запрет кеширования броузерами данной отправляемой информации
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }

    /**
     * Method sends the file length requested by the client
     * @param fileName file name
     * @return file length in bytes
     */
    @Override
    public byte[] lengthForDownloadingFile(String fileName) {
            Path path = Paths.get(MY_DIRECTORY + "/" + fileName);
            byte[] data = new byte[0];
            try {
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                log.error("!message by DefaultUserService, method lengthForDownloadingFile!", e);
            }
            return data;
    }



    //   !!! В следующем блоке кода используется механизм чистого "IO". При этом написано так, что механизм формирования "ResponseEntity" происходит прямо тут, а не в контроллере. !!!
//
//        File file = new File("D:/Java" + "/" + fileName);
//
//       // ” InputStreamResource”, используется только для открытых файлов, лучше не использовать вообще. Для чтения файла лучше использовать FileSystemResource использующий
//       //  (начиная с Spring Framework 5.0) NIO.2 или ByteArrayResource(для загрузки содержимого из любого заданного массива байтов и потом возможности многоразового чтения потока).
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        HttpHeaders headers = new HttpHeaders(); //создаем объект для дальнейшей записи в него всех необходимых заголовков (это в запросе/ответе инфа идущая после первой “стартовой” строки и до превой пустой строки, после которой идет тело сообщения)
//        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//
//        ResponseEntity<Object> responseEntity = ResponseEntity
//                .ok() //создаем в ответе в стартовой строке типа “HTTP 1.* 200 OK”
//                .headers(headers) //заполняем ответ необходимыми заголовками
//                .contentLength(file.length()) // это число байт данных в теле запроса или ответа. При ответе пользователь тогда сможет увидеть процент скаченных данных
//            //  .contentType(MediaType.parseMediaType("application/txt")) //еще один вариант описание данных, содержащихся в теле запроса или ответа. Данная информация помогает браузеру узнать с помощью какого приложения можно открыть данное содержание, и подсказывает пользователю открыть с помощью существующего приложения на их компьютере при завершении скачивания.
//                .contentType(mediaType)
//                .body(resource); //заполняем ответ телом
//
//        return responseEntity;

    /**
     * Method, from the "FilesInfo" entity, displays all information about the files on the disk
     * @return all data from entity "FilesInfo"
     */
    @Override
    public List<String> findAllFilesName() {

        log.info("!!!message by DefaultFilesInfoService, method findAllFilesName!!!");

        List<String> filesInfoDtoList = new ArrayList<>();
        List<FilesInfo> fiesInfoList = filesInfoRepository.findAll();
        for (FilesInfo fileInfo : fiesInfoList) {
            FilesInfoDto filesInfoDto = filesInfoConverter.fromFilesInfoToFilesInfoDto(fileInfo);
            String fileName = filesInfoDto.getName();
            filesInfoDtoList.add(fileName);
        }
        return filesInfoDtoList;
    }

    /**
     * Method removes a file from disk and information about it from the “FilesInfo” entity
     * @param fileName name of the file to be deleted on the server
     */
    @Override
    public void deleteFile(String fileName) {
        log.info("!!!message by DefaultFilesInfoService, method deleteFile!!!");

        //удаляю сведения о файле из БД
        filesInfoRepository.deleteByName(fileName);

        //удаляю с диска сам файл
        Path path = Paths.get(MY_DIRECTORY + "/" + fileName);
        //само удаление обязательно оборачивать в "try/catch"
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
