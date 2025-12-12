package com.teamteskboard.domain.user.service;

import com.teamteskboard.domain.user.enums.UserRoleEnum;
import com.teamteskboard.common.regexp.RegExp;
import com.teamteskboard.common.config.PasswordEncoder;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.common.jwt.utils.JwtUtil;
import com.teamteskboard.domain.team.dto.response.TeamMemberResponse;
import com.teamteskboard.common.entity.Team;
import com.teamteskboard.common.entity.UserTeams;
import com.teamteskboard.domain.team.repository.TeamRepository;
import com.teamteskboard.domain.team.repository.UserTeamsRepository;
import com.teamteskboard.domain.user.dto.request.*;
import com.teamteskboard.domain.user.dto.response.*;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamsRepository userTeamsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 비지니스 로직 처리
     * @param request 회원가입 입력값 파라미터
     * @return ApiResponse<CreateUserResponse> json 반환
     */
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        // userName 중복 체크
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new CustomException(ExceptionMessageEnum.USER_SAME_USERNAME);

        }

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ExceptionMessageEnum.USER_SAME_EMAIL);
        }

        // 이메일 형식 체크
        if (!request.getEmail().matches(RegExp.EMAIL)) {
            throw new CustomException(ExceptionMessageEnum.PATTERN_VALIDATION_FAILED_EXCEPTION);
        }

        // User 생성
        User user = new User(
                request.getName(),
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                UserRoleEnum.USER
        );

        // 저장
        User createdUser = userRepository.save(user);

        return CreateUserResponse.from(createdUser);
    }

    /**
     * 로그인
     * @param request 로그인 요청 DTO (아이디, 비밀번호)
     * @return 로그인 응답 DTO (토큰)
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 아이디 확인 → 사용자 조회
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS));

        // 삭제된 계정인지, 비밀번호가 일치하는지 확인
        if (user.getIsDeleted() || !passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS);

        // 토큰 생성
        String token = jwtUtil.generateToken(user.getId(), user.getUserName(), user.getRole());

        return LoginResponse.from(token);
    }

    /**
     * 비밀번호 확인
     * @param id 로그인한 유저 ID
     * @param request 비밀번호 확인 요청 DTO (비밀번호)
     * @return 비밀번호 응답 DTO (일치 여부)
     */
    @Transactional(readOnly = true)
    public PasswordResponse verifyPassword(Long id, PasswordRequest request) {
        // 로그인된 아이디 확인 → 사용자 조회
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.INVALID_CREDENTIALS));

        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        return PasswordResponse.from(valid);
    }

    /**
     * 사용자 정보 조회
     * @param userId 회원 고유 번호
     * @return ApiResponse<GetUserResponse> json 반환
     */
    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long userId) {
        // 로그인된 아이디 확인 → 사용자 조회
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        return GetUserResponse.from(user);
    }

    /**
     * 사용자 목록 조회
     * @return List<GetUserResponse> 반환
     */
    @Transactional(readOnly = true)
    public List<GetUserResponse> getUserList() {
        List<User> user = userRepository.findAll();

        List<GetUserResponse> getUserResponseList = user
                .stream()
                .map(u -> GetUserResponse.from(u))
                .toList();

        return getUserResponseList;
    }

    /**
     * 회원정보 수정 비지니스 로직 처리
     * @param userId 회원 고유 번호
     * @param request 수정할 데이터 파라미터
     * @return UpdateUserResponse json 반환
     */
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 이메일 중복 체크 (자기 자신 제외)
        if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            throw new CustomException(ExceptionMessageEnum.USER_SAME_EMAIL);
        }

        // 내 정보의 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionMessageEnum.INVALID_MEMBER_INFO);
        }

        user.userUpdate(
                request.getName(),
                request.getEmail()
        );

        return UpdateUserResponse.from(user);
    }

    /**
     * 회원탈퇴 비지니스 로직 처리
     * @param userId 회원 고유 번호
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        user.userDelete(true);
    }

    /**
     * 추가 가능한 사용자 목록 조회 비지니스 로직 처리
     * @param teamId 팀 고유 번호
     * @return TeamMemberResponse list json 반환
     */
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> addTeamUser(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        // 회원정보 전체
        List<User> users = userRepository.findAll();

        // 내가 입력한 팀 정보 전체
        List<UserTeams> userTeams = userTeamsRepository.findAllByTeam(team);

        // 내가 입력한 팀의 모든 id 정보 가져오기
        Set<Long> userIds = userTeams
                .stream()
                .map(u -> u.getUser().getId())
                .collect(Collectors.toSet());

        return users
                .stream()
                .filter(t -> !userIds.contains(t.getId()))
                .map(u -> new TeamMemberResponse(
                        u.getId(),
                        u.getUserName(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole().name(),
                        u.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}