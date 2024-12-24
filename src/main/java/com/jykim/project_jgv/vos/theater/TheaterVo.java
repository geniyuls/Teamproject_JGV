package com.jykim.project_jgv.vos.theater;

import com.jykim.project_jgv.entities.theater.TheaterEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TheaterVo extends TheaterEntity {
    private String citName;
    private LocalDateTime scStartDate;
    private int cinemaCount;
    private int seatCount;
}
