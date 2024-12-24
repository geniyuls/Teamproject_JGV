package com.jykim.project_jgv.services.ticket;

import com.jykim.project_jgv.entities.movie.MovieEntity;
import com.jykim.project_jgv.entities.theater.CinemaEntity;
import com.jykim.project_jgv.entities.theater.CinemaTypeEntity;
import com.jykim.project_jgv.entities.theater.ScreenEntity;
import com.jykim.project_jgv.entities.theater.TheaterEntity;
import com.jykim.project_jgv.entities.ticket.MethodEntity;
import com.jykim.project_jgv.entities.ticket.PaymentEntity;
import com.jykim.project_jgv.entities.ticket.ReservationEntity;
import com.jykim.project_jgv.entities.ticket.SeatEntity;
import com.jykim.project_jgv.exceptions.TransactionalException;
import com.jykim.project_jgv.mappers.ticket.MethodMapper;
import com.jykim.project_jgv.mappers.ticket.PaymentMapper;
import com.jykim.project_jgv.mappers.ticket.ReservationMapper;
import com.jykim.project_jgv.mappers.ticket.TicketMapper;
import com.jykim.project_jgv.results.CommonResult;
import com.jykim.project_jgv.results.Result;
import com.jykim.project_jgv.vos.theater.MovieVo;
import com.jykim.project_jgv.vos.theater.RegionVo;
import com.jykim.project_jgv.vos.theater.ScreenVo;
import com.jykim.project_jgv.vos.ticket.CinemaTypeVo;
import com.jykim.project_jgv.vos.ticket.SeatVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketMapper ticketMapper;
    private final MethodMapper methodMapper;
    private final PaymentMapper paymentMapper;
    private final ReservationMapper reservationMapper;

    public ScreenVo[] selectScreenDatesByMovieAndTheaterAndDate(String moTitle, String thName, String scStartDate) {
        if (moTitle == null || moTitle.isEmpty() || moTitle.length() > 100 ||
                thName == null || thName.isEmpty() || thName.length() > 30 ||
                scStartDate == null || scStartDate.isEmpty() || scStartDate.length() > 10) {
            return null;
        }
        TheaterEntity theater = this.ticketMapper.selectTheater(thName);
        MovieEntity movie = this.ticketMapper.selectMovieNumByMovieTitle(moTitle);
        return this.ticketMapper.selectScreenDatesByMovieAndTheaterAndDate(movie.getMoNum(), theater.getThNum(), scStartDate);
    }

    public MovieVo[] selectAllMovies(String moTitle) {
        if (moTitle == null || moTitle.isEmpty()) {
            return null;
        }
        return this.ticketMapper.selectAllMoviesByMoTitle(moTitle);
    }

    public MovieVo[] selectAllMoviesByThName(String thName) {
        if (thName == null || thName.isEmpty()) {
            return null;
        }
        return this.ticketMapper.selectAllMoviesByThName(thName);
    }

    public MovieVo[] selectAllMoviesByScStartDate(String scStartDate) {
        if (scStartDate == null || scStartDate.isEmpty()) {
            return null;
        }
        return this.ticketMapper.selectAllMoviesByscStartDate(scStartDate);
    }

    public MovieVo[] selectAllMoviesByMoTitleAndScStartDate(String moTitle, String scStartDate) {
        if (moTitle == null || moTitle.isEmpty() ||
                scStartDate == null || scStartDate.isEmpty()) {
            return null;
        }
        return this.ticketMapper.selectAllMoviesByMoTitleAndScStartDate(moTitle, scStartDate);
    }

    public MovieVo[] selectAllMoviesByThNameAndScStartDate(String thName, String scStartDate) {
        if (thName == null || thName.isEmpty() ||
                scStartDate == null || scStartDate.isEmpty()) {
            return null;
        }
        return this.ticketMapper.selectAllMoviesByThNameAndScStartDate(thName, scStartDate);
    }

    public MovieVo[] selectAllMoviesByRating() {
        MovieVo[] movies = this.ticketMapper.selectAllMoviesByRating();
        for (MovieVo movie : movies) {
            switch (movie.getRaGrade()) {
                case "청소년관람불가" -> movie.setRaGrade("nineteen");
                case "15세이상관람가" -> movie.setRaGrade("fifteen");
                case "12세이상관람가" -> movie.setRaGrade("twelve");
                case "전체관람가" -> movie.setRaGrade("all");
                case "미정" -> movie.setRaGrade("none");
            }
        }
        return movies;
    }

    public MovieVo[] selectAllMoviesByKorea() {
        MovieVo[] movies = this.ticketMapper.selectAllMoviesByKorea();
        for (MovieVo movie : movies) {
            switch (movie.getRaGrade()) {
                case "청소년관람불가" -> movie.setRaGrade("nineteen");
                case "15세이상관람가" -> movie.setRaGrade("fifteen");
                case "12세이상관람가" -> movie.setRaGrade("twelve");
                case "전체관람가" -> movie.setRaGrade("all");
                case "미정" -> movie.setRaGrade("none");
            }
        }
        return movies;
    }

    public RegionVo[] selectRegionAndTheaterCount() {
        return this.ticketMapper.selectRegionAndTheaterCount();
    }


    public Map<String, String> getWeekdaysByMoTitleAndThName(String moTitle, String thName) {
        // 화면의 시작 날짜들을 가져옴
        MovieVo[] screens = this.ticketMapper.selectAllMoviesByMoTitleAndThName(moTitle, thName);

        // 고유 날짜를 저장할 Set
        SortedSet<String> sortedSet = new TreeSet<>();

        // 날짜 리스트를 돌면서 고유 날짜만 저장
        for (MovieVo screen : screens) {
            sortedSet.add(screen.getScStartDate().toString().split("T")[0]);
        }

        SortedSet<String> sortSet = new TreeSet<>();
        for (String sort : sortedSet) {
            sortSet.add(sort.substring(0, 7));
        }

        Map<String, String> map = new TreeMap<>();
        for (String title : sortSet) {
            List<String> list = new ArrayList<>();
            for (String day : sortedSet) {
                if (day.contains(title)) {
                    LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    list.add(day.split("-")[2] + "-" + localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).split("요일")[0]);
                }
            }
            map.put(title, list.toString().replace('[', ' ').replace(']', ' '));
        }
        return map;
    }


    public Map<String, String> getWeekdaysByMoTitle(String moTitle) {
        MovieVo[] screens = this.ticketMapper.selectAllMoviesByMoTitle(moTitle);
        SortedSet<String> sortedSet = new TreeSet<>();
        for (MovieVo screen : screens) {
            sortedSet.add(screen.getScStartDate().toString().split("T")[0]);
        }

        SortedSet<String> sortSet = new TreeSet<>();
        for (String sort : sortedSet) {
            sortSet.add(sort.substring(0, 7));
        }

        Map<String, String> map = new TreeMap<>();
        for (String title : sortSet) {
            List<String> list = new ArrayList<>();
            for (String day : sortedSet) {
                if (day.contains(title)) {
                    LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    list.add(day.split("-")[2] + "-" + localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).split("요일")[0]);
                }
            }
            map.put(title, list.toString().replace('[', ' ').replace(']', ' '));
        }
        return map;
    }

    public Map<String, String> getWeekdaysByThName(String thName) {
        MovieVo[] screens = this.ticketMapper.selectAllMoviesByThName(thName);
        SortedSet<String> sortedSet = new TreeSet<>();
        for (MovieVo screen : screens) {
            sortedSet.add(screen.getScStartDate().toString().split("T")[0]);
        }

        SortedSet<String> sortSet = new TreeSet<>();
        for (String sort : sortedSet) {
            sortSet.add(sort.substring(0, 7));
        }

        Map<String, String> map = new TreeMap<>();
        for (String title : sortSet) {
            List<String> list = new ArrayList<>();
            for (String day : sortedSet) {
                if (day.contains(title)) {
                    LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    list.add(day.split("-")[2] + "-" + localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).split("요일")[0]);
                }
            }
            map.put(title, list.toString().replace('[', ' ').replace(']', ' '));
        }
        return map;
    }

    public Map<String, String> getWeekdays() {
        ScreenEntity[] screens = this.ticketMapper.selectAllScreenDates();
        SortedSet<String> sortedSet = new TreeSet<>();
        for (ScreenEntity screen : screens) {
            sortedSet.add(screen.getScStartDate().toString().split("T")[0]);
        }

        SortedSet<String> sortSet = new TreeSet<>();
        for (String sort : sortedSet) {
            sortSet.add(sort.substring(0, 7));
        }

        Map<String, String> map = new TreeMap<>();
        for (String title : sortSet) {
            List<String> list = new ArrayList<>();
            for (String day : sortedSet) {
                if (day.contains(title)) {
                    LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    list.add(day.split("-")[2] + "-" + localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN).split("요일")[0]);
                }
            }
            map.put(title, list.toString().replace('[', ' ').replace(']', ' '));
        }
        return map;
    }

    // region 크롤링을 위한 영화관 열거형
    @Getter
    public enum TheaterCode {
        DAEGU("CGV대구", "0345"),
        SUSEONG("CGV대구수성", "0157"),
        STADIUM("CGV대구스타디움", "0108"),
        ACADEMY("CGV대구아카데미", "0185"),
        YEONGGYEONG("CGV대구연경", "0343"),
        WOLSEONG("CGV대구월성", "0216"),
        JUKJEON("CGV대구죽전", "0256"),
        HANIL("CGV대구한일", "0147"),
        HYUNDAI("CGV대구현대", "0109");

        private final String cgvName;
        private final String cgvCode;

        TheaterCode(String cgvName, String cgvCode) {
            this.cgvName = cgvName;
            this.cgvCode = cgvCode;
        }
    }
    // endregion

    // region 크롤링을 위한 영화관 타입 열거형
    @Getter
    public enum CinemaCode {
        NORMAL("2D"),
        IMAX("IMAX"),
        FOURDX("4DX"),
        SCREENX("SCREENX"),
        RECLINER("리클라이너"),
        CINE("CINE&FORET");

        private final String citName;

        CinemaCode(String citName) {
            this.citName = citName;
        }
    }
    // endregion

    // region 크롤링
    @Transactional
    public void Crawl(ScreenEntity screen) throws TransactionalException {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver();

        try {
            for (TheaterCode theater : TheaterCode.values()) {
                int ciNum = 0;
                String dateUrl = "http://www.cgv.co.kr/theaters/?areacode=11&theaterCode=" + theater.cgvCode;
                driver.get(dateUrl);

                WebElement iframe = driver.findElement(By.id("ifrm_movie_time_table"));
                driver.switchTo().frame(iframe);

                List<WebElement> dateElements = driver.findElements(By.cssSelector("#slider > .item-wrap.on > .item > li"));
                List<String> dates = new ArrayList<>();
                for (WebElement day : dateElements) {
                    String movie = day.findElement(By.cssSelector("a")).getAttribute("href");
                    if (movie.isEmpty()) {
                        continue;
                    }

                    URL url = new URL(movie);
                    String query = url.getQuery();
                    if (query == null || query.isEmpty()) {
                        continue;
                    }

                    Map<String, String> queryParams = new HashMap<>();
                    String[] pairs = query.split("&");

                    for (String pair : pairs) {
                        String[] keyValue = pair.split("=");
                        if (keyValue.length == 2) {
                            queryParams.put(keyValue[0], keyValue[1]);
                        }
                    }

                    String date = queryParams.get("date");
                    if (date != null) {
                        dates.add(date);
                    }
                }
                System.out.println(dates);
                System.out.println(theater.cgvName);

                for (int i = 0; i < dates.toArray().length; i++) {
                    String date = dates.toArray()[i].toString(); // YYYYMMDD 형식의 날짜
                    System.out.println("상영일: " + date);

                    String url = "http://www.cgv.co.kr/theaters/?areacode=11&theaterCode=" + theater.cgvCode + "&date=" + date;
                    driver.get(url);

                    WebElement iframes = driver.findElement(By.id("ifrm_movie_time_table"));
                    driver.switchTo().frame(iframes);

                    List<WebElement> movieElements = driver.findElements(By.cssSelector(".col-times"));

                    for (WebElement movieElement : movieElements) {
                        String movieTitle = movieElement.findElement(By.cssSelector(".info-movie > a > strong")).getText().trim();
                        MovieEntity movieNum = this.ticketMapper.selectMovieNumByMovieTitle(movieTitle);
                        if (movieNum == null) {
                            break;
                        }
                        screen.setMoNum(movieNum.getMoNum());

                        List<WebElement> timeTables = movieElement.findElements(By.cssSelector(".type-hall"));
                        StringBuilder timeTable = new StringBuilder();
                        for (WebElement table : timeTables) {
                            List<WebElement> cinemas = table.findElements(By.cssSelector(".info-hall > ul > li:nth-child(2)"));
                            for (WebElement cinema : cinemas) {
                                String result = "";
                                for (CinemaCode code : CinemaCode.values()) {
                                    if (cinema.getText().trim().contains("4DX관")) {
                                        result = "4DX";
                                        CinemaEntity cinemaTypeNum = this.ticketMapper.selectCinemaNumByCinemaType(result, theater.cgvName);
                                        screen.setCiNum(cinemaTypeNum.getCiNum());
                                        ciNum = cinemaTypeNum.getCiNum();
                                        break;
                                    }
                                    if (cinema.getText().trim().contains("씨네앤포레")) {
                                        result = "CINE&FORET";
                                        CinemaEntity cinemaTypeNum = this.ticketMapper.selectCinemaNumByCinemaType(result, theater.cgvName);
                                        screen.setCiNum(cinemaTypeNum.getCiNum());
                                        ciNum = cinemaTypeNum.getCiNum();
                                        break;
                                    }
                                    if (cinema.getText().contains("[CGV아트하우스]") ||
                                            cinema.getText().contains("[영남이공대학교]") ||
                                            cinema.getText().contains("[아트기획전관]")) {
                                        result = cinema.getText();
                                        CinemaEntity artCinema = this.ticketMapper.selectCinemaNumByCinemaTitle(result.substring(0, 2), theater.cgvName);
                                        screen.setCiNum(artCinema.getCiNum());
                                        ciNum = artCinema.getCiNum();
                                        break;
                                    }
                                    if (cinema.getText().contains("비상설")) {
                                        continue;
                                    }
                                    if (code.citName.equals(cinema.getText())) {
                                        result = cinema.getText().trim();
                                        CinemaEntity cinemaTypeNum = this.ticketMapper.selectCinemaNumByCinemaType(result, theater.cgvName);
                                        screen.setCiNum(cinemaTypeNum.getCiNum());
                                        ciNum = cinemaTypeNum.getCiNum();
                                        break;
                                    } else {
                                        screen.setCiNum(0);
                                    }
                                }
                                if (screen.getCiNum() == 0) {
                                    if (cinema.getText() != null && cinema.getText().length() >= 3) {
                                        result = cinema.getText().trim();
                                        CinemaEntity cinemaNum = this.ticketMapper.selectCinemaNumByCinemaTitle(result.substring(0, 3), theater.cgvName);
                                        screen.setCiNum(cinemaNum.getCiNum());
                                        ciNum = cinemaNum.getCiNum();
                                    } else if (cinema.getText() != null && cinema.getText().length() >= 2) {
                                        result = cinema.getText().trim();
                                        CinemaEntity cinemaNum = this.ticketMapper.selectCinemaNumByCinemaTitle(result.substring(0, 2), theater.cgvName);
                                        screen.setCiNum(cinemaNum.getCiNum());
                                        ciNum = cinemaNum.getCiNum();
                                    }
                                }
                                timeTable.append("상영관: ").append(result).append("\n");
                                List<WebElement> timeElements = table.findElements(By.cssSelector(".info-timetable > ul > li > a > em"));
                                for (WebElement element : timeElements) {
                                    timeTable.append("상영 시간: ").append(element.getText()).append("\n");
                                    String dateTimeString = date + "T" + element.getText();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm");
                                    screen.setScStartDate(LocalDateTime.parse(dateTimeString, formatter));
                                    ScreenEntity[] screens = this.ticketMapper.selectDuplicateScreen(LocalDateTime.parse(dateTimeString, formatter), this.ticketMapper.selectMovieNumByMovieTitle(movieTitle).getMoNum(), ciNum);
                                    if (screens.length < 1) {
                                        this.ticketMapper.insertScreen(screen);
                                    }
                                }
                            }
                        }
                        System.out.println("------------");
                        System.out.println("영화: " + movieTitle);
                        System.out.println(timeTable.toString().trim());
                    }
                    System.out.println("------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
    // endregion

//    ---------------------------------------

    public SeatVo[] selectSeatByReservationNum(String ciName, String thName, String moTitle, LocalDateTime scStartDate) {

        SeatVo[] seatNum = this.ticketMapper.selectSeatByReservationSeNum(ciName, thName, moTitle, scStartDate);


        return seatNum;
    }

    public CinemaTypeVo[] selectSeatByCitPrice(String ciName, String thName, String moTitle, LocalDateTime scStartDate) {

        CinemaTypeVo[] citPrice = this.ticketMapper.selectSeatByCitPrice(ciName, thName, moTitle, scStartDate);
        return citPrice;
    }

    private List<String> checkSeatsAvailability(String[] seNames, String ciName, String thName, LocalDateTime scStartDate) {
        // 좌석 중 예약된 것이 없으면 seNames 배열을 반환, 예약된 좌석은 제외
        List<String> availableSeats = Arrays.stream(seNames)
                .filter(seName -> this.reservationMapper.isSeatAlreadyReserved(seName, ciName, thName, scStartDate) == 0)  // 예약되지 않은 좌석만 필터링
                .collect(Collectors.toList());

        return availableSeats;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult insertReservationAndPayment(String moTitle, String ciName, String thName, LocalDateTime scStartDate,
                                                    String meName, int usNum, String[] seNames, int paPrice) {
        try {
            // Step 1: 영화 상영 정보 조회
            ScreenEntity[] screenEntities = this.reservationMapper.selectReservationByScNum(moTitle, ciName, thName, scStartDate);
            if (screenEntities == null || screenEntities.length == 0) {
                return CommonResult.FAILURE;  // 영화 상영 정보가 없을 경우
            }

            // Step 2: 기존 Payment 데이터 조회
            PaymentEntity[] existingPayments = this.reservationMapper.selectPaymentByPaNum(meName, usNum);

            // Step 3: Payment 삽입
            PaymentEntity newPayment = new PaymentEntity();
            newPayment.setPaPrice(paPrice);
            newPayment.setUsNum(usNum);
            newPayment.setMeNum(this.methodMapper.selectPaymentMeNum(meName).getMeNum());
            newPayment.setPaState(false);
            newPayment.setPaCreatedAt(LocalDateTime.now());
            newPayment.setPaDeletedAt(null);

            int paymentResult = this.paymentMapper.insertPayment(newPayment);
            if (paymentResult <= 0) {
                return CommonResult.FAILURE;  // 결제 정보 삽입 실패
            }

            // Step 4: Payment 삽입 후 갱신된 Payment 데이터 조회
            PaymentEntity[] updatedPayments = this.reservationMapper.selectPaymentByPaNum(meName, usNum);
            PaymentEntity insertedPayment = findNewPayment(existingPayments, updatedPayments);
            if (insertedPayment == null) {
                return CommonResult.FAILURE;  // 삽입된 결제 정보를 찾을 수 없을 경우
            }

            // Step 5: 좌석 유효성 검사 (중복되지 않은 좌석만 필터링)
            List<String> availableSeats = checkSeatsAvailability(seNames, ciName, thName, scStartDate);
            if (availableSeats.isEmpty()) {
                deletePaymentIfInserted(insertedPayment.getPaNum()); // 결제 정보 삭제
                return CommonResult.FAILURE;  // 모든 좌석이 이미 예약됨
            }

            // Step 6: Reservation 삽입
            for (String seName : availableSeats) {
                SeatEntity[] seatEntities = this.reservationMapper.selectSeatBySeNum(seName, ciName, thName);
                if (seatEntities == null || seatEntities.length == 0) {
                    continue; // 잘못된 좌석 정보
                }

                ReservationEntity reservation = new ReservationEntity();
                reservation.setScNum(screenEntities[0].getScNum());
                reservation.setSeNum(seatEntities[0].getSeNum());
                reservation.setPaNum(insertedPayment.getPaNum());

                int reservationResult = this.reservationMapper.insertReservation(reservation);
                if (reservationResult <= 0) {
                    return CommonResult.FAILURE;  // 예약 실패
                }
            }

            // Step 7: 결제 상태 업데이트
            insertedPayment.setPaState(true);
            int updateResult = this.paymentMapper.updatePaymentState(insertedPayment.getPaNum(), true);
            if (updateResult <= 0) {
                deletePaymentIfInserted(insertedPayment.getPaNum()); // 결제 정보 삭제
                return CommonResult.FAILURE;  // 결제 상태 업데이트 실패
            }

            return CommonResult.SUCCESS;  // 성공
        } catch (Exception e) {
            throw new RuntimeException("예약 및 결제 처리 중 오류", e);  // 예외 발생 시 트랜잭션 롤백
        }
    }


    // 기존 Payment와 새 Payment 비교하여 삽입된 Payment 찾기
    private PaymentEntity findNewPayment(PaymentEntity[] existingPayments, PaymentEntity[] updatedPayments) {
        Set<Integer> existingPaNums = Arrays.stream(existingPayments)
                .map(PaymentEntity::getPaNum)
                .collect(Collectors.toSet());

        for (PaymentEntity payment : updatedPayments) {
            if (!existingPaNums.contains(payment.getPaNum())) {
                return payment;
            }
        }
        return null;
    }

    // 결제 데이터 삭제 (필요한 경우)
    private void deletePaymentIfInserted(int paNum) {
        int result = this.paymentMapper.deletePayment(paNum);
        if (result > 0) {
            System.out.println("결제 정보 삭제 성공. PaNum: " + paNum);
        } else {
            System.out.println("결제 정보 삭제 실패. PaNum: " + paNum);
        }
    }

    public int selectPaymentNum(String moTitle, String ciName, String thName, LocalDateTime scStartDate, int paPrice, int usNum) {

        // selectPaymentNum을 호출하여 결제 번호 조회
        return  this.paymentMapper.selectPaymentNum(moTitle, ciName, thName, scStartDate, paPrice, usNum);
    }

}