package com.jykim.project_jgv.vos.theater;

import com.jykim.project_jgv.entities.movie.MovieEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieVo extends MovieEntity {
    private String raGrade;
    private String mImgUrl;
    private LocalDateTime scStartDate;
    private String thName;
    private int theaterCount;
    private String regName;
}
