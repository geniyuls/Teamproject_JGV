package com.jykim.project_jgv.controlles.ticket;

import com.jykim.project_jgv.entities.theater.ScreenEntity;
import com.jykim.project_jgv.entities.theater.TheaterEntity;
import com.jykim.project_jgv.entities.user.UserEntity;
import com.jykim.project_jgv.results.Result;
import com.jykim.project_jgv.services.theater.TheaterService;
import com.jykim.project_jgv.services.ticket.TicketService;
import com.jykim.project_jgv.vos.theater.MovieVo;
import com.jykim.project_jgv.vos.theater.RegionVo;
import com.jykim.project_jgv.vos.theater.ScreenVo;
import com.jykim.project_jgv.vos.ticket.CinemaTypeVo;
import com.jykim.project_jgv.vos.ticket.SeatVo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(value = "/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TheaterService theaterService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(@RequestParam(value = "region", required = false) String region,
                                 @RequestParam(value = "moTitle", required = false) String moTitle,
                                 @RequestParam(value = "thName", required = false) String thName,
                                 @RequestParam(value = "scStartDate", required = false) String scStartDate,
                                 HttpSession session, UserEntity user) {
        ModelAndView modelAndView = new ModelAndView();
        MovieVo[] movies = this.ticketService.selectAllMoviesByRating();
        RegionVo[] regions = this.ticketService.selectRegionAndTheaterCount();
        TheaterEntity[] theaters = this.theaterService.getTheatersByRegion(region);
        Map<String, String> maps = this.ticketService.getWeekdays();
        if (moTitle != null && thName == null && scStartDate == null) {
            MovieVo[] movieVos = this.ticketService.selectAllMovies(moTitle);
            List<Object[]> vos = new ArrayList<>();
            Map<String, String> moMaps = this.ticketService.getWeekdaysByMoTitle(moTitle);
            Set<String> keys = new LinkedHashSet<>();
            SortedSet<String> thKeys = new TreeSet<>();
            for (MovieVo vo : movieVos) {
                keys.add(vo.getMoTitle());
                keys.add(vo.getMImgUrl());
                keys.add(vo.getRaGrade());
                keys.add(String.valueOf(vo.getTheaterCount()));
                keys.add(vo.getRegName());
                thKeys.add(vo.getThName());
            }
            vos.add(new Object[]{keys, thKeys, moMaps});
            modelAndView.addObject("movieVos", vos);
        }
        if (moTitle == null && thName != null && scStartDate == null) {
            MovieVo[] movieVos = this.ticketService.selectAllMoviesByThName(thName);
            List<Object[]> vos = new ArrayList<>();
            Map<String, String> moMaps = this.ticketService.getWeekdaysByThName(thName);
            Map<String, Boolean> map = new HashMap<>();

            for (MovieVo vo : movieVos) {
                switch (vo.getRaGrade()) {
                    case "청소년관람불가" -> vo.setRaGrade("nineteen");
                    case "15세이상관람가" -> vo.setRaGrade("fifteen");
                    case "12세이상관람가" -> vo.setRaGrade("twelve");
                    case "전체관람가" -> vo.setRaGrade("all");
                    case "미정" -> vo.setRaGrade("none");
                }
                String key = vo.getMoTitle() + "&&" + vo.getRaGrade();
                map.put(key, true);
            }
            vos.add(new Object[]{map, moMaps});
            modelAndView.addObject("theaterVos", vos);
        }
        if (moTitle == null && thName == null && scStartDate != null) {
            MovieVo[] movieVos = this.ticketService.selectAllMoviesByScStartDate(scStartDate);
            List<Object[]> vos = new ArrayList<>();
            SortedSet<String> thKeys = new TreeSet<>();
            Map<String, Boolean> map = new HashMap<>();
            for (MovieVo vo : movieVos) {
                switch (vo.getRaGrade()) {
                    case "청소년관람불가" -> vo.setRaGrade("nineteen");
                    case "15세이상관람가" -> vo.setRaGrade("fifteen");
                    case "12세이상관람가" -> vo.setRaGrade("twelve");
                    case "전체관람가" -> vo.setRaGrade("all");
                    case "미정" -> vo.setRaGrade("none");
                }
                String key = vo.getMoTitle() + "&&" + vo.getRaGrade();
                map.put(key, true);
                thKeys.add(vo.getThName());
            }
            vos.add(new Object[]{map, thKeys});
            modelAndView.addObject("dateVos", vos);
        }
        if (moTitle != null && thName != null && scStartDate == null) {
            List<Object[]> vos = new ArrayList<>();
            Map<String, String> moMaps = this.ticketService.getWeekdaysByMoTitleAndThName(moTitle, thName);
            vos.add(new Object[]{moMaps});
            modelAndView.addObject("moThVos", vos);
        }
        if (moTitle != null && thName == null && scStartDate != null) {
            MovieVo[] movieVos = this.ticketService.selectAllMoviesByMoTitleAndScStartDate(moTitle, scStartDate);
            List<Object[]> vos = new ArrayList<>();
            SortedSet<String> thKeys = new TreeSet<>();
            for (MovieVo vo : movieVos) {
                thKeys.add(vo.getThName());
            }
            vos.add(new Object[]{thKeys});
            modelAndView.addObject("moScVos", vos);
        }
        if (moTitle == null && thName != null && scStartDate != null) {
            MovieVo[] movieVos = this.ticketService.selectAllMoviesByThNameAndScStartDate(thName, scStartDate);
            List<Object[]> vos = new ArrayList<>();
            Set<String> keys = new LinkedHashSet<>();
            Map<String, Boolean> map = new HashMap<>();
            for (MovieVo vo : movieVos) {
                switch (vo.getRaGrade()) {
                    case "청소년관람불가" -> vo.setRaGrade("nineteen");
                    case "15세이상관람가" -> vo.setRaGrade("fifteen");
                    case "12세이상관람가" -> vo.setRaGrade("twelve");
                    case "전체관람가" -> vo.setRaGrade("all");
                    case "미정" -> vo.setRaGrade("none");
                }
                String key = vo.getMoTitle() + "&&" + vo.getRaGrade();
                map.put(key, true);
            }
            vos.add(new Object[]{keys, map});
            modelAndView.addObject("thScVos", vos);
        }
        if (moTitle != null && thName != null && scStartDate != null) {
            MovieVo[] movieVos = this.ticketService.selectAllMovies(moTitle);
            ScreenVo[] screens = this.ticketService.selectScreenDatesByMovieAndTheaterAndDate(moTitle, thName, scStartDate);
            Map<List<String>, List<List<String>>> times = new HashMap<>();
            List<Object[]> vos = new ArrayList<>();
            Set<String> MoKeys = new LinkedHashSet<>();
            for (MovieVo vo : movieVos) {
                MoKeys.add(vo.getMoTitle());
                MoKeys.add(vo.getMImgUrl());
                MoKeys.add(vo.getRaGrade());
                MoKeys.add(String.valueOf(vo.getTheaterCount()));
                MoKeys.add(vo.getRegName());
            }
            vos.add(new Object[]{MoKeys});
            for (ScreenVo screen : screens) {
                List<String> keys = new ArrayList<>();
                List<String> contents = new ArrayList<>();
                keys.add(screen.getCitName());
                keys.add(screen.getCiName());
                keys.add(String.valueOf(screen.getSeatCount()));
                contents.add(String.valueOf(screen.getScStartDate()));
                contents.add(String.valueOf(screen.getSeatCount()));
                times.computeIfAbsent(keys, k -> new ArrayList<>()).add(contents);
            }
            modelAndView.addObject("times", times);
            modelAndView.addObject("Vos", vos);
        }
        modelAndView.addObject("movies", movies);
        modelAndView.addObject("regions", regions);
        modelAndView.addObject("theaters", theaters);
        modelAndView.addObject("maps", maps);
        modelAndView.addObject("session", session);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("ticket/index");
        return modelAndView;
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMovies() {
        ModelAndView modelAndView = new ModelAndView();
        MovieVo[] movies = this.ticketService.selectAllMoviesByKorea();
        modelAndView.addObject("movies", movies);
        modelAndView.setViewName("ticket/index");
        return modelAndView;
    }

    @RequestMapping(value = "/crawling", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView Crawling(ScreenEntity screen) {
        ModelAndView modelAndView = new ModelAndView();
        this.ticketService.Crawl(screen);
        modelAndView.setViewName("ticket/crawling");
        return modelAndView;
    }

    // --------------------------------------

    @RequestMapping(value = "/seat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSeat(
            @RequestParam(value = "ciName", required = false) String ciName,
            @RequestParam(value = "thName", required = false) String thName,
            @RequestParam(value = "moTitle", required = false) String moTitle,
            @RequestParam(value = "scStartDate", required = false) LocalDateTime scStartDate
    ) {
        JSONObject response = new JSONObject();
        SeatVo[] seatNum = this.ticketService.selectSeatByReservationNum(ciName, thName, moTitle, scStartDate);
        CinemaTypeVo[] citPrice = this.ticketService.selectSeatByCitPrice(ciName, thName, moTitle, scStartDate);
        response.put(Result.NAME, seatNum);
        response.put(Result.NAMES, citPrice);
        return response.toString();
    }

    @RequestMapping(value = "/ReservationRefundRegulations", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReservationRefundRegulations(
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ticket/ReservationRefundRegulations");
        return modelAndView;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(@RequestParam(value = "meName", required = false) String meName,
                            @RequestParam(value = "paPrice", required = false) int paPrice,
                            @RequestParam(value = "usNum", required = false) int usNum,
                            @RequestParam(value = "seName", required = false) String[] seNames,  // 여러 좌석 정보 배열로 받기
                            @RequestParam(value = "moTitle", required = false) String moTitle,
                            @RequestParam(value = "ciName", required = false) String ciName,
                            @RequestParam(value = "thName", required = false) String thName,
                            @RequestParam(value = "scStartDate", required = false) LocalDateTime scStartDate) {

        // seNames 배열이 비어 있는지 체크
        if (seNames == null || seNames.length == 0) {
            throw new IllegalArgumentException("좌석 정보가 전달되지 않았습니다.");
        }

        // 결제 정보 저장
        Result result = this.ticketService.insertReservationAndPayment(moTitle, ciName, thName, scStartDate, meName, usNum, seNames, paPrice);

        int results = this.ticketService.selectPaymentNum(moTitle, ciName, thName, scStartDate, paPrice, usNum);

        // 응답 데이터 생성
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        response.put(Result.NAMES, results);


        return response.toString();
    }


    @RequestMapping(value = "/reservation", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getReservation(
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ticket/reservation");
        return modelAndView;
    }
}