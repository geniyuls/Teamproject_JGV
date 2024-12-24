package com.jykim.project_jgv.mappers.user;

import com.jykim.project_jgv.entities.user.EmailTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailTokenMapper {
    int insertEmailToken(EmailTokenEntity emailToken);

    EmailTokenEntity selectEmailTokenByUserEmailAndKey(
            @Param("emEmail") String userEmail,
            @Param("emKey") String key
    );

    int updateEmailToken(EmailTokenEntity emailToken);

}
