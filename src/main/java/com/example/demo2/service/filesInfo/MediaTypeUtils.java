package com.example.demo2.service.filesInfo;

import org.springframework.http.MediaType;

import javax.servlet.ServletContext;


//Этот класс для создания типа файла, который будет вставляться в тело ответа при работе клиента с отправкой/получением файлов
/**
 * A class for creating a file type that will be inserted by the server into the response body when the client is working with sending / receiving files.
 *
 * @author Maxim
 * @version 1.0
 */
public class MediaTypeUtils {
    // abc.zip
    // abc.pdf,..

    /**
     * A method for creating a file type that will be inserted by the server into the response body when the client is working with sending / receiving files.
     * @param servletContext service information
     * @param fileName name of the file to be sent by the server
     * @return file type to be sent by the server
     */
    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
