package com.jykim.project_jgv.mappers.user;

import com.jykim.project_jgv.DTO.ReviewDTO;
import com.jykim.project_jgv.entities.user.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int insertReview(ReviewEntity review);

    int selectArticleCountByMovieId(@Param("id") int id);

    ReviewDTO[] selectArticleByMovieId(@Param("id") int id, @Param("limitCount") int limitCount, // 선택 개수(제한 개수)
                                     @Param("offsetCount") int offsetCount); // 거를 개수(건너뛸 개수));

    int incrementLikeCount(@Param("reviewId") int reviewId);

    int countUserReviewsForMovie(@Param("movieId") int movieId, @Param("userId") int userId);

    ReviewEntity getReviewsById(@Param("reviewId") int reviewId);

    int updateReview(ReviewEntity review);
}
