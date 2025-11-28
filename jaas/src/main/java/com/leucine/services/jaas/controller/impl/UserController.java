package com.leucine.services.jaas.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.controller.IUserController;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.partial.PartialFetchUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.service.IUserAuditService;
import com.leucine.services.jaas.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Component
@AllArgsConstructor
public class UserController implements IUserController {

  private final IUserService service;
  private final IUserAuditService userAuditService;

  @Override
  public Response<Page<PartialUserDto>> getAll(String filters, Pageable pageable) {
    return Response.builder().data(service.getAll(filters, pageable)).build();
  }

  @Override
  public Response<Page<PartialUserDto>> getAllUsers(boolean archived, String filters, Pageable pageable) {
    return Response.builder().data(service.getAll(archived, filters, pageable)).build();
  }

  @Override
  public Response<Page<PartialFetchUserDto>> getAllByRoles(List<String> roles, String filters, boolean isAssigned, FilterAssigneeRequest filterAssigneeRequest, Pageable pageable) {
    //TODO split this api and think of a better approach
    return Response.builder().data(service.getAllByRoles(roles, filters, isAssigned, filterAssigneeRequest.getAssignees(), pageable)).build();
  }

  @Override
  public PrincipalUser addUser(UserAddRequest userAddRequest) {
    return service.create(userAddRequest);
  }

  @Override
  public PrincipalUser resetToken(Long userId) {
    return service.resetToken(userId);
  }

  @Override
  public PrincipalUser cancelToken(Long userId) {
    return service.cancelToken(userId);
  }

  @Override
  public Response<BasicDto> isUsernameAvailable(UsernameCheckRequest usernameCheckRequest) {
    return Response.builder().data(service.isUsernameAvailable(usernameCheckRequest.getUsername())).build();
  }

  @Override
  public Response<BasicDto> isEmailAvailable(EmailCheckRequest emailCheckRequest) {
    return Response.builder().data(service.isEmailAvailable(emailCheckRequest.getEmail())).build();
  }

  @Override
  public Response<BasicDto> isEmployeeIdAvailable(EmployeeIdCheckRequest employeeIdCheckRequest) {
    return Response.builder().data(service.isEmployeeIdAvailable(employeeIdCheckRequest.getEmployeeId())).build();
  }

  @Override
  public Response<UserDto> getById(Long userId) {
    return Response.builder().data(service.getById(userId)).build();
  }

  @Override
  public PrincipalUser archive(Long userId, UserArchiveRequest userArchiveRequest) {
    return service.archive(userId, userArchiveRequest);
  }

  @Override
  public PrincipalUser unarchive(Long userId, UserUnarchiveRequest userUnarchiveRequest) {
    return service.unarchive(userId, userUnarchiveRequest);
  }

  @Override
  public PrincipalUser unlock(Long userId) {
    return service.unlock(userId);
  }

  @Override
  public Response<TokenDto> switchFacility(Long userId, Long facilityId) {
    return Response.builder().data(service.switchFacility(userId, facilityId)).build();
  }

  @Override
  public PrincipalUser update(Long userId, UserUpdateRequest userUpdateRequest) {
    userUpdateRequest.setId(userId);
    return service.update(userUpdateRequest);
  }

  @Override
  public PrincipalUser updateBasicInformation(Long userId, UserBasicInformationUpdateRequest userBasicInformationUpdateRequest) {
    userBasicInformationUpdateRequest.setId(userId);
    return service.updateBasicInformation(userBasicInformationUpdateRequest);
  }

  @Override
  public Response<BasicDto> updatePassword(Long userId, PasswordUpdateRequest passwordUpdateRequest) {
    passwordUpdateRequest.setUserId(userId);
    try {
      passwordUpdateRequest.setPassword(new String(Base64.getDecoder().decode(passwordUpdateRequest.getPassword())));
      passwordUpdateRequest.setConfirmPassword(new String(Base64.getDecoder().decode(passwordUpdateRequest.getConfirmPassword())));
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }
    return Response.builder().data(service.updatePassword(passwordUpdateRequest)).build();
  }

  @Override
  public Response<ChallengeQuestionDto> getChallengeQuestionsAnswer(Long userId, String token) {
    return Response.builder().data(service.getChallengeQuestionsAnswer(userId, token)).build();
  }

  @Override
  public Response<BasicDto> updateChallengeQuestionsAnswer(Long userId, ChallengeQuestionsAnswerUpdateRequest challengeQuestionsAnswerUpdateRequest) {
    return Response.builder().data(service.updateChallengeQuestionsAnswer(challengeQuestionsAnswerUpdateRequest)).build();
  }

  @Override
  public Response<Page<UserAuditDto>> getAllUserAudits(String filters, Pageable pageable) {
    return Response.builder().data(userAuditService.getAll(filters, pageable)).build();
  }

  @Override
  public ResponseEntity<byte[]> downloadUserAudits(String filters) throws IOException {
    return userAuditService.downloadUserAudits(filters);
  }

  @Override
  public Response<List<PartialUserDto>> searchUsers(String query, int limit) throws UnsupportedEncodingException, JsonProcessingException {
    return Response.builder().data(service.searchUsers(query, limit)).build();
  }
}
