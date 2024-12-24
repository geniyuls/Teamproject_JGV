package com.jykim.project_jgv.mappers.ticket;

import com.jykim.project_jgv.entities.theater.ScreenEntity;
import com.jykim.project_jgv.entities.ticket.PaymentEntity;
import com.jykim.project_jgv.entities.ticket.ReservationEntity;
import com.jykim.project_jgv.entities.ticket.SeatEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface ReservationMapper {

    ScreenEntity[] selectReservationByScNum(@Param("moTitle") String moTitle,
                                            @Param("ciName") String ciName,
                                            @Param("thName") String thName,
                                            @Param("scStartDate") LocalDateTime scStartDate);

    PaymentEntity[] selectPaymentByPaNum(
            @Param("meName") String meName,
            @Param("usNum") int usNum);

    SeatEntity[] selectSeatBySeNum(@Param("seName") String seName,
                                   @Param("ciName") String ciName,
                                   @Param("thName") String thName);

    int insertReservation(ReservationEntity reservation);

    Integer isSeatAlreadyReserved(@Param("seName") String seName,
                                  @Param("ciName") String ciName,
                                  @Param("thName") String thName,
                                  @Param("scStartDate") LocalDateTime scStartDate);
}
