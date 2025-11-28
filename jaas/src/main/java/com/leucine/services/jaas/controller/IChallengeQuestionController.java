package com.leucine.services.jaas.controller;

import com.leucine.services.jaas.dto.ChallengeQuestionDto;
import com.leucine.services.jaas.dto.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/challenge-questions")
public interface IChallengeQuestionController {
  @GetMapping
  @ResponseBody
  Response<List<ChallengeQuestionDto>> getAll(@RequestParam(name = "archived", defaultValue = "false", required = false) boolean archived);
}