package com.jykim.project_jgv.DTO;

import com.jykim.project_jgv.entities.movie.CharactorEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterDTO extends CharactorEntity {
    private Integer CImgNum;
    private String CImgUrl;
}
