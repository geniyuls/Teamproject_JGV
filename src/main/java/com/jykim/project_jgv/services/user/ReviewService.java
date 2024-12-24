package com.jykim.project_jgv.services.user;

import com.jykim.project_jgv.DTO.ReviewDTO;
import org.apache.commons.lang3.tuple.Pair;
import com.jykim.project_jgv.entities.user.ReviewEntity;
import com.jykim.project_jgv.mappers.user.ReviewMapper;
import com.jykim.project_jgv.results.CommonResult;
import com.jykim.project_jgv.vos.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public CommonResult postReview(ReviewEntity review) {
        if(review == null || review.getReContent() == null) {
            return CommonResult.FAILURE;
        }
        review.setReCreatedAt(LocalDateTime.now());
        review.setReUpdatedAt(null);
        review.setReDeletedAt(null);
        review.setReLiked(0);
        return this.reviewMapper.insertReview(review) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Pair<PageVo, ReviewDTO[]> getReviewById(int id, int page) {
        page = Math.max(1, page);
        int totalCount = this.reviewMapper.selectArticleCountByMovieId(id);
        PageVo pageVo = new PageVo(page, totalCount);

        ReviewDTO[] reviewDTO = this.reviewMapper.selectArticleByMovieId(id, pageVo.countPerPage, pageVo.offsetCount);
        return Pair.of(pageVo, reviewDTO);
    }
}
