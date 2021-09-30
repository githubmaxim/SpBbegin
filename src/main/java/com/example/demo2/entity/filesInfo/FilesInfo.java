package com.example.demo2.entity.filesInfo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Entity for storing information about files loaded from the sheet "workingWithFiles.html".
 *
 * @author Maxim
 * @version 1.0
 */
@Entity
@Table(name = "fileInfo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder //создает все конструкторы, а геттеры/сеттеры создает аннотация @Data

public class FilesInfo {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * file name
     */
    private String name;

    /**
     * file size
     */
    private Long size;

    /**
     * generated file identifier
     */
    private String myKey;

    /**
     * date the entry was created
     */
    private LocalDate uploadDate;
}
