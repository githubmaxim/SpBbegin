package com.example.demo2.service.filesInfo;

import org.springframework.http.MediaType;

import javax.servlet.ServletContext;


//Этот класс для создания типа файла, который будет вставляться в тело ответа
public class MediaTypeUtils {
    // abc.zip
    // abc.pdf,..
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
