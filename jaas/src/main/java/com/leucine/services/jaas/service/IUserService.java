package com.leucine.services.jaas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.constant.Action;
import com.leucine.services.jaas.constant.IErrorCode;
import com.leucine.services.jaas.dto.BasicDto;
import com.leucine.services.jaas.dto.ChallengeQuestionDto;
import com.leucine.services.jaas.dto.TokenDto;
import com.leucine.services.jaas.dto.UserDto;
import com.leucine.services.jaas.dto.partial.PartialFetchUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService extends UserDetailsService {

  boolean sendEmail(User user, String token, Action.Email action, String serviceId);

  BasicDto isEmployeeIdAvailable(String username);

  BasicDto isEmailAvailable(String email);

  PrincipalUser create(UserAddRequest userAddRequest);

  BasicDto isUsernameAvailable(String username);

//  AuthenticationDto register(UserRegistrationRequest userRegistrationRequest, String serviceId);

  UserDto getById(Long userId);

  Page<PartialUserDto> getAll(String filters, Pageable pageable);

  Page<PartialUserDto> getAll(boolean archived, String filters, Pageable pageable);

  Page<PartialFetchUserDto> getAllByRoles(List<String> roles, String filters, boolean isAssigned, List<Object> assignees, Pageable pageable);

  PrincipalUser archive(Long userId, UserArchiveRequest userArchiveRequest);

  PrincipalUser unarchive(Long userId, UserUnarchiveRequest userUnarchiveRequest);

  PrincipalUser unlock(Long userId);

  PrincipalUser update(UserUpdateRequest userUpdateRequest);

  PrincipalUser updateBasicInformation(UserBasicInformationUpdateRequest userBasicInformationUpdateRequest);

  PrincipalUser resetToken(Long userId);

  PrincipalUser cancelToken(Long userid);

  BasicDto updatePassword(PasswordUpdateRequest passwordUpdateRequest);

  ChallengeQuestionDto getChallengeQuestionsAnswer(Long userId, String token);

  BasicDto updateChallengeQuestionsAnswer(ChallengeQuestionsAnswerUpdateRequest challengeQuestionsAnswerUpdateRequest);

  List<IErrorCode> validatePasswordAgainstPasswordPolicy(User user, String username, String confirmPassword, String password);

  TokenDto switchFacility(Long userId, Long facilityId);

  List<PartialUserDto> searchUsers(String query, int limit) throws UnsupportedEncodingException, JsonProcessingException;
}
