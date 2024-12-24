package com.jykim.project_jgv.mappers.movie;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.jykim.project_jgv.entities.movie.RatingEntity;

@Mapper
public interface RaitingMapper {
    int insertMovieRaiting(RatingEntity ratingEntity);
    Integer selectRaitingIdByName(String RaitingName);
}
