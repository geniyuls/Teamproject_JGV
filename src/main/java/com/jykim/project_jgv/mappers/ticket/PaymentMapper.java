package com.jykim.project_jgv.mappers.ticket;

import com.jykim.project_jgv.entities.ticket.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface PaymentMapper {

    int insertPayment(PaymentEntity payment);

    int updatePaymentState(@Param("paNum") int paNum,
                           @Param("paState") boolean paState);

    int deletePayment(@Param("paNum") int paNum);

    int selectPaymentNum(@Param("moTitle") String moTitle,
                         @Param("ciName") String ciName,
                         @Param("thName") String thName,
                         @Param("scStartDate") LocalDateTime scStartDate,
                         @Param("paPrice") int paPrice,
                         @Param("usNum") int usNum);
}
