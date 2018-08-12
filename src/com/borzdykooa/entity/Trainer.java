package com.borzdykooa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/*
Класс, соответствующий таблице trainer в базе данных
 */
@Data
@Builder
@AllArgsConstructor
public class Trainer implements Serializable {

    private Long id;
    private String name;
    private String language;
    private Integer experience;

    public Trainer(String name, String language, Integer experience) {
        this.name = name;
        this.language = language;
        this.experience = experience;
    }

    public Trainer(Long id, Integer experience) {
        this.id = id;
        this.experience = experience;
    }
}
