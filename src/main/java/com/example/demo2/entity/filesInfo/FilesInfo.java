package com.example.demo2.entity.filesInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

public class FilesInfo {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private Long size;
    private String myKey;
    private LocalDate uploadDate;
}
