package com.example.demo2.dto.filesInfo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * File DTO for entity "FilesInfo".
 *
 * @author Maxim
 * @version 1.0
 */
@Data //тут эту аннотацию (которая содержит в себе 4 аннотации) можно применять т.к. нет поля “id”, которое бы автоматически назначала БД.
      //Иначе ломается логика, встроенной в эту аннотацию, внутренней аннотации @EqualsAndHashCode.
@Builder
public class FilesInfoDto {
    private Integer id;
    private String name;
    private Long size;
    private String myKey;
    private LocalDate uploadDate;
}
