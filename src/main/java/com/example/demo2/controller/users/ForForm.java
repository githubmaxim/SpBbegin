package com.example.demo2.controller.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Class for accepting information from the client using the form method
 *
 * @author Maxim
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class ForForm {

    /**
     * just some numeral
     */
    @NotBlank(message = "number can't empty")
    Integer number;

    /**
     * just some text
     */
    @NotBlank(message = "word can't empty")
    String word;
}
