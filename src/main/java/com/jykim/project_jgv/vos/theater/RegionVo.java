package com.jykim.project_jgv.vos.theater;

import com.jykim.project_jgv.entities.theater.RegionEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionVo extends RegionEntity {
    private int theaterCount;
}
