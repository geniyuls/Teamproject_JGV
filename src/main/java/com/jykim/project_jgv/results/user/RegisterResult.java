package com.jykim.project_jgv.results.user;

import com.jykim.project_jgv.results.Result;


public enum RegisterResult implements Result {

    FAILURE_DUPLICATE_ID,
    FAILURE_DUPLICATE_CONTACT,
    FAILURE_DUPLICATE_EMAIL,
    FAILURE_DUPLICATE_NICKNAME,
    FAILURE_INVALID_ID,
    FAILURE_INVALID_PASSWORD,
}
