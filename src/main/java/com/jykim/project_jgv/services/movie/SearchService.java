package com.jykim.project_jgv.services.movie;

import com.jykim.project_jgv.DTO.CharacterDTO;
import com.jykim.project_jgv.DTO.Movie_ImageDTO;
import com.jykim.project_jgv.DTO.Movie_InfoDTO;
import com.jykim.project_jgv.mappers.movie.MovieMapper;
import com.jykim.project_jgv.results.CommonResult;
import com.jykim.project_jgv.vos.PageVo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    private final MovieMapper movieMapper;

    public SearchService(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    public Pair<PageVo, List<Movie_ImageDTO>> searchMoviesByKeyword (String keyword, int requestPage) {
        int totalCount = movieMapper.getMovieCountByKeyword(keyword);

        PageVo pageVo = new PageVo(requestPage, totalCount);

        List<Movie_ImageDTO> movies = movieMapper.findMovieByKeyword(keyword, pageVo.offsetCount, pageVo.countPerPage);

        return Pair.of(pageVo, movies);
    }

    public List<CharacterDTO> searchPeopleByKeyword (String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        return this.movieMapper.findCharacterByKeyword(keyword);
    }

    public List<Movie_ImageDTO> searchMoviesByPersonKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        return this.movieMapper.searchMoviesByPersonKeyword(keyword);
    }

    public List<Map<String, Object>> searchMoviesByActor(String actorName) {
        return this.movieMapper.findMoviesByActorName(actorName);
    }

}
