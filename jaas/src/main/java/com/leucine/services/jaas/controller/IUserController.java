package com.leucine.services.jaas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.partial.PartialFetchUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.UserAudit;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/users")
public interface IUserController {

  @GetMapping("/all")
  @ResponseBody
  Response<Page<PartialUserDto>> getAll(@RequestParam(name = "filters", defaultValue = "", required = false) String filters,
                                        @SortDefault.SortDefaults({
                                          @SortDefault(sort = User.SORT_FIRST_NAME, direction = Sort.Direction.ASC),
                                          @SortDefault(sort = User.SORT_LAST_NAME, direction = Sort.Direction.ASC)}) Pageable pageable);

  @GetMapping
  @ResponseBody
  Response<Page<PartialUserDto>> getAllUsers(@RequestParam(name = "archived", defaultValue = "false", required = false) boolean archived,
                                             @RequestParam(name = "filters", defaultValue = "", required = false) String filters,
                                             @SortDefault.SortDefaults({
                                               @SortDefault(sort = User.SORT_FIRST_NAME, direction = Sort.Direction.ASC),
                                               @SortDefault(sort = User.SORT_LAST_NAME, direction = Sort.Direction.ASC)}) Pageable pageable);

  @PatchMapping("/by/roles")
  @ResponseBody
  Response<Page<PartialFetchUserDto>> getAllByRoles(@RequestParam List<String> roles,
                                                    @RequestParam(name = "filters", defaultValue = "", required = false) String filters,
                                                    @RequestParam(name = "isAssigned", defaultValue = "false", required = false) boolean isAssigned,
                                                    @RequestBody(required = false) FilterAssigneeRequest filterAssigneeRequest,
                                                    @SortDefault.SortDefaults({
                                                      @SortDefault(sort = User.SORT_FIRST_NAME, direction = Sort.Direction.ASC),
                                                      @SortDefault(sort = User.SORT_LAST_NAME, direction = Sort.Direction.ASC)})
                                                    @PageableDefault(size = 250) Pageable pageable);

  @PostMapping
  @ResponseBody
  PrincipalUser addUser(@Valid @RequestBody UserAddRequest userAddRequest);

  @PostMapping("/username/check")
  Response<BasicDto> isUsernameAvailable(@Valid @RequestBody UsernameCheckRequest usernameCheckRequest);

  @PostMapping("/email/check")
  Response<BasicDto> isEmailAvailable(@Valid @RequestBody EmailCheckRequest emailCheckRequest);

  @PostMapping("/employee-id/check")
  Response<BasicDto> isEmployeeIdAvailable(@Valid @RequestBody EmployeeIdCheckRequest employeeIdCheckRequest);

  @PatchMapping("/{userId}/token/reset")
  PrincipalUser resetToken(@PathVariable Long userId);

  @PatchMapping("/{userId}/token/cancel")
  PrincipalUser cancelToken(@PathVariable Long userId);

  @GetMapping("/{userId}")
  Response<UserDto> getById(@PathVariable Long userId);

  @PatchMapping("/{userId}")
  PrincipalUser update(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest);

  @PatchMapping("/{userId}/basic")
  PrincipalUser updateBasicInformation(@PathVariable Long userId, @Valid @RequestBody UserBasicInformationUpdateRequest userBasicInformationUpdateRequest);

  @PatchMapping("/{userId}/password")
  Response<BasicDto> updatePassword(@PathVariable Long userId, @RequestBody PasswordUpdateRequest passwordUpdateRequest);

  @GetMapping("/{userId}/challenge-questions")
  Response<ChallengeQuestionDto> getChallengeQuestionsAnswer(@PathVariable Long userId, @RequestParam String token);

  @PatchMapping("/{userId}/challenge-questions")
  Response<BasicDto> updateChallengeQuestionsAnswer(@PathVariable Long userId, @RequestBody ChallengeQuestionsAnswerUpdateRequest challengeQuestionsAnswerUpdateRequest);

  @PatchMapping("/{userId}/archive")
  PrincipalUser archive(@PathVariable Long userId, @Valid @RequestBody UserArchiveRequest userArchiveRequest);

  @PatchMapping("/{userId}/unarchive")
  PrincipalUser unarchive(@PathVariable Long userId, @Valid @RequestBody UserUnarchiveRequest userUnarchiveRequest);

  @PatchMapping("/{userId}/unlock")
  PrincipalUser unlock(@PathVariable Long userId);

  @PatchMapping("/{userId}/facilities/{facilityId}/switch")
  @ResponseBody
  Response<TokenDto> switchFacility(@PathVariable Long userId, @PathVariable Long facilityId);

  @GetMapping("/audits")
  @ResponseBody
  Response<Page<UserAuditDto>> getAllUserAudits(@RequestParam(name = "filters", defaultValue = "") String filters, @SortDefault(sort = UserAudit.DEFAULT_SORT) Pageable pageable);

  @GetMapping("/audits/download")
  ResponseEntity<byte[]> downloadUserAudits(@RequestParam(name = "filters", defaultValue = "") String filters) throws IOException;

  @GetMapping("/directory/search")
  Response<List<PartialUserDto>> searchUsers(@RequestParam(name = "query", defaultValue = "", required = false) String query, @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) throws UnsupportedEncodingException, JsonProcessingException;
}
