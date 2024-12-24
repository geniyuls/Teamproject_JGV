package com.jykim.project_jgv.mappers.ticket;

import com.jykim.project_jgv.entities.movie.MovieEntity;
import com.jykim.project_jgv.entities.theater.CinemaEntity;
import com.jykim.project_jgv.entities.theater.CinemaTypeEntity;
import com.jykim.project_jgv.entities.theater.ScreenEntity;
import com.jykim.project_jgv.entities.theater.TheaterEntity;
import com.jykim.project_jgv.entities.ticket.ReservationEntity;
import com.jykim.project_jgv.entities.ticket.SeatEntity;
import com.jykim.project_jgv.vos.theater.MovieVo;
import com.jykim.project_jgv.vos.theater.RegionVo;
import com.jykim.project_jgv.vos.theater.ScreenVo;
import com.jykim.project_jgv.vos.ticket.CinemaTypeVo;
import com.jykim.project_jgv.vos.ticket.SeatVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface TicketMapper {
    int insertScreen(ScreenEntity screen);

    TheaterEntity selectTheater(@Param("thName") String thName);

    ScreenVo[] selectScreenDatesByMovieAndTheaterAndDate(@Param("movie") int moNum,
                                                         @Param("theater") int thNum,
                                                         @Param("date") String scStartDate);

    ScreenEntity[] selectAllScreenDates();

    RegionVo[] selectRegionAndTheaterCount();

    MovieVo[] selectAllMoviesByMoTitle(@Param(value = "moTitle") String moTitle);

    MovieVo[] selectAllMoviesByThName(@Param(value = "thName") String thName);

    MovieVo[] selectAllMoviesByscStartDate(@Param(value = "scStartDate") String scStartDate);

    MovieVo[] selectAllMoviesByMoTitleAndThName(@Param(value = "moTitle") String moTitle,
                                                @Param(value = "thName") String thName);

    MovieVo[] selectAllMoviesByMoTitleAndScStartDate(@Param(value = "moTitle") String moTitle,
                                                     @Param(value = "scStartDate") String scStartDate);

    MovieVo[] selectAllMoviesByThNameAndScStartDate(@Param(value = "thName") String thName,
                                                    @Param(value = "scStartDate") String scStartDate);

    MovieVo[] selectAllMoviesByRating();

    MovieVo[] selectAllMoviesByKorea();

    MovieEntity selectMovieNumByMovieTitle(@Param(value = "moTitle") String moTitle);

    CinemaEntity selectCinemaNumByCinemaTitle(@Param(value = "ciName") String ciName,
                                              @Param(value = "thName") String thName);

    CinemaEntity selectCinemaNumByCinemaType(@Param(value = "citName") String citName,
                                             @Param(value = "thName") String thName);

    ScreenEntity[] selectDuplicateScreen(@Param(value = "startDate") LocalDateTime startDate,
                                         @Param(value = "moNum") int moNum,
                                         @Param(value = "ciNum") int ciNum);

//    ---------------------------------------

    SeatVo[] selectSeatByReservationSeNum(
            @Param("ciName") String ciName,
            @Param("thName") String thName,
            @Param("moTitle") String moTitle,
            @Param("scStartDate") LocalDateTime scStartDate);

    CinemaTypeVo[] selectSeatByCitPrice(
            @Param("ciName") String ciName,
            @Param("thName") String thName,
            @Param("moTitle") String moTitle,
            @Param("scStartDate") LocalDateTime scStartDate);

    Integer selectScNumByScName(@Param("moTitle") String moTitle,
                                @Param("ciName") String ciName,
                                @Param("thName") String thName,
                                @Param("scStartDate") LocalDateTime scStartDate);

}
