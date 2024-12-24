package com.jykim.project_jgv.vos.ticket;


import com.jykim.project_jgv.entities.theater.CinemaTypeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaTypeVo extends CinemaTypeEntity {
    private int moTime;
    private String mImgUrl;

}
