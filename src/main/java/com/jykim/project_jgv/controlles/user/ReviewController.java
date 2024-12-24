package com.jykim.project_jgv.controlles.user;

import com.jykim.project_jgv.entities.user.ReviewEntity;
import com.jykim.project_jgv.entities.user.UserEntity;
import com.jykim.project_jgv.results.CommonResult;
import com.jykim.project_jgv.services.user.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(@RequestBody ReviewEntity review, HttpSession session) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        if(loggedInUser == null) {
            JSONObject obj = new JSONObject();
            obj.put("result", CommonResult.FAILURE);
            return obj.toString();
        }

        review.setUsNum(loggedInUser.getUsNum());
        CommonResult result = this.reviewService.postReview(review);
        JSONObject response = new JSONObject();
        response.put("result", result);
        return response.toString();
    }
}
