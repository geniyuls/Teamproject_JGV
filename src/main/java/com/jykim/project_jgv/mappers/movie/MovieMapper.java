package com.jykim.project_jgv.mappers.movie;

import com.jykim.project_jgv.DTO.CharacterDTO;
import com.jykim.project_jgv.DTO.Movie_ImageDTO;
import com.jykim.project_jgv.DTO.Movie_InfoDTO;
import com.jykim.project_jgv.entities.movie.MovieEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MovieMapper {
    // 영화 데이터 insert
    int insertMovie(@Param("movie") MovieEntity movie, @Param("raiting") int rating);


    //영화 데이터 중복 방지
    Integer selectMovieByUniqueFields(@Param("movieTitle") String movieTitle, @Param("movieDate")String movieDate);

    // 영화 리스트 모두 표시하기
    List<Movie_ImageDTO> selectAllMovies();

    //영화 중복 데이터 삭제
    boolean deleteDupleMovies();

    // 영화 제목으로 id를 들고오기 (업데이트를 위해)
    Integer selectMovieIdByTitle(@Param("movieTitle") String movieTitle);

    // 영화 아이디로 관람 등급 들고오기
    Integer selectMovieRaitingByMovieId(@Param("moNum") int moNum);

    // 관람 등급 업데이트
    void updateMovieRaiting(@Param("moNum") int moNum, @Param("raId") int raId);

    // 영화 데이터 업데이트
    void updateMovie(MovieEntity movie);

    //영화 기본정보와 상세정보 조회
    Movie_InfoDTO getMovieInfoById(@Param("id") Integer id);

    //감독 이름으로 영화 찾기
    List<Map<String, Object>> getRelatedMoviesByDirector(String director);

    //search 키워드로 영화, 인물 찾기
    List<Movie_ImageDTO> findMovieByKeyword(@Param("keyword") String keyword, int offsetCount, int countPerPage);

    List<CharacterDTO> findCharacterByKeyword(@Param("keyword") String keyword);

    List<Movie_ImageDTO> searchMoviesByPersonKeyword(@Param("keyword") String keyword);

    // 필모그래피를 위해
    List<Map<String, Object>> findMoviesByActorName(@Param("actorName")String actorName);

    //인물 id로 영화 찾기(필모그래피)
    List<Map<String, Object>> findMoviesByCharacterId(@Param("charId") Integer charId);

    //페이징을 위한 영화 카운트
    int getMovieCountByKeyword(String keyword);

}