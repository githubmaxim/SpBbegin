package com.example.demo2.controller.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class ForForm {

    @NotBlank(message = "number can't empty")
    Integer number;

    @NotBlank(message = "word can't empty")
    String word;
}
