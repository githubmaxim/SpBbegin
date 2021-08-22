package com.example.demo2.entity.filesInfo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

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

    private String name;
    private Long size;
    private String myKey;
    private LocalDate uploadDate;
}
