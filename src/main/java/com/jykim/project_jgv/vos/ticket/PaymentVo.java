package com.jykim.project_jgv.vos.ticket;

import com.jykim.project_jgv.entities.ticket.PaymentEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentVo extends PaymentEntity {
    private int scNum;
}
