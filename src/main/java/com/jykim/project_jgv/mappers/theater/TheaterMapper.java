package com.jykim.project_jgv.mappers.theater;

import com.jykim.project_jgv.entities.theater.RegionEntity;
import com.jykim.project_jgv.entities.theater.TheaterEntity;
import com.jykim.project_jgv.vos.theater.ScreenVo;
import com.jykim.project_jgv.vos.theater.TheaterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TheaterMapper {
    RegionEntity[] getRegionAll();

    TheaterEntity[] getTheatersByRegion(@Param(value = "region") String region);

    TheaterVo[] selectAllTheaters(@Param(value = "theater") String theater);

    TheaterVo[] selectAllTheatersByRegion(@Param(value = "region") String region,
                                          @Param(value = "movie") String movie);

    ScreenVo[] selectAllScreens(@Param(value = "date") String date,
                                @Param(value = "theater") String theater);

    ScreenVo[] selectAllScreensByRegion(@Param(value = "date") String date,
                                        @Param(value = "region") String region,
                                        @Param(value = "movie") String movie);
}
