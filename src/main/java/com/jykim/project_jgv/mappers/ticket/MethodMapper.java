package com.jykim.project_jgv.mappers.ticket;

import com.jykim.project_jgv.entities.ticket.MethodEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MethodMapper {

    MethodEntity selectPaymentMeNum(@Param("meName") String meName);
}
