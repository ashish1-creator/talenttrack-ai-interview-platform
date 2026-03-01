package com.talenttrack.controller;

import com.talenttrack.model.InterviewRequest;
import com.talenttrack.model.InterviewEvaluationRequest;
import com.talenttrack.model.InterviewEvaluationResponse;
import com.talenttrack.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PostMapping("/generate")
    public List<String> generateQuestions(@RequestBody InterviewRequest request) {

        return interviewService.generateQuestions(
                request.getRole(),
                request.getLevel()
        );
    }

    // ==========================================
    // 2️⃣ Evaluate Full Interview
    // ==========================================
    @PostMapping("/evaluate-interview")
    public InterviewEvaluationResponse evaluateInterview(
            @RequestBody InterviewEvaluationRequest request) throws Exception {

        return interviewService.evaluateInterview(
                request.getRole(),
                request.getLevel(),
                request.getQuestions(),
                request.getAnswers()
        );
    }
}