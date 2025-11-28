package com.leucine.services.jaas.controller;

import com.leucine.services.jaas.dto.VersionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version.json")
public interface IVersionController {
  @GetMapping
  @ResponseBody
  VersionDto get();
}