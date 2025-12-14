package com.teamteskboard.domain.team.service;

import com.teamteskboard.common.entity.Team;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.common.entity.UserTeams;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.domain.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.domain.team.dto.request.UpdatedTeamRequest;
import com.teamteskboard.domain.team.dto.response.CreatedTeamResponse;
import com.teamteskboard.domain.team.dto.response.GetAllTeamsResponse;
import com.teamteskboard.domain.team.dto.response.TeamMemberResponse;
import com.teamteskboard.domain.team.repository.TeamRepository;
import com.teamteskboard.domain.team.repository.UserTeamsRepository;
import com.teamteskboard.domain.user.enums.UserRoleEnum;
import com.teamteskboard.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserTeamsRepository userTeamsRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void createTeam_success() {
        // given
        CreatedTeamRequest request =
                new CreatedTeamRequest("팀이름", "설명");

        when(teamRepository.existsByName("팀이름")).thenReturn(false);

        // when
        CreatedTeamResponse response = teamService.createTeam(request);

        // then
        assertNotNull(response);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void createTeam_duplicateName() {
        // given
        CreatedTeamRequest request =
                new CreatedTeamRequest("중복팀", "설명");

        when(teamRepository.existsByName("중복팀")).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> teamService.createTeam(request)
        );

        assertEquals(
                ExceptionMessageEnum.TEAM_NAME_DUPLICATE,
                exception.getExceptionMessage()
        );
    }

    @Test
    void getOneTeam_notFound() {
        // given
        when(teamRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CustomException.class,
                () -> teamService.getOneTeam(1L)
        );
    }

    @Test
    void deleteTeam_hasMembers() {
        // given
        Team team = mock(Team.class);

        when(teamRepository.findById(1L))
                .thenReturn(Optional.of(team));
        when(userTeamsRepository.findAllByTeam(team))
                .thenReturn(List.of(mock(UserTeams.class)));

        // when & then
        assertThrows(
                CustomException.class,
                () -> teamService.deleteTeam(1L)
        );
    }

    @Test
    void getAllTeams_empty() {
        // given
        when(teamRepository.findAll())
                .thenReturn(List.of());

        // when
        List<GetAllTeamsResponse> responses =
                teamService.getAllTeams();

        // then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void getAllTeams_success() {
        Team team = mock(Team.class);
        when(team.getId()).thenReturn(1L);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUserName()).thenReturn("user1");
        when(user.getName()).thenReturn("유저");
        when(user.getEmail()).thenReturn("test@test.com");
        when(user.getRole()).thenReturn(UserRoleEnum.USER);

        UserTeams ut = mock(UserTeams.class);
        when(ut.getUser()).thenReturn(user);
        when(ut.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(teamRepository.findAll()).thenReturn(List.of(team));
        when(userTeamsRepository.findAllByTeamId(1L)).thenReturn(List.of(ut));

        List<GetAllTeamsResponse> result = teamService.getAllTeams();

        assertEquals(1, result.size());
    }

    @Test
    void updateTeam_forbidden() {
        // given
        Team team = mock(Team.class);
        UpdatedTeamRequest request =
                new UpdatedTeamRequest("수정", "설명");

        when(teamRepository.findById(1L))
                .thenReturn(Optional.of(team));
        when(teamRepository.existsByName("수정"))
                .thenReturn(false);
        when(userTeamsRepository.existsByTeamIdAndUserId(1L, 100L))
                .thenReturn(false); // 권한 없음

        // when & then
        assertThrows(
                CustomException.class,
                () -> teamService.updateTeam(1L, request, 100L)
        );
    }

    @Test
    void removeTeamMember_teamNotFound() {
        // given
        when(teamRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CustomException.class,
                () -> teamService.removeTeamMember(1L, 2L, 3L)
        );
    }

    @Test
    void getTeamMembers_success() {
        Team team = mock(Team.class);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUserName()).thenReturn("user");
        when(user.getName()).thenReturn("name");
        when(user.getEmail()).thenReturn("email");
        when(user.getRole()).thenReturn(UserRoleEnum.USER);
        when(user.getCreatedAt()).thenReturn(LocalDateTime.now());

        UserTeams ut = mock(UserTeams.class);
        when(ut.getUser()).thenReturn(user);

        when(userTeamsRepository.findAllByTeam(team)).thenReturn(List.of(ut));

        List<TeamMemberResponse> result = teamService.getTeamMembers(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getTeamMembers_teamNotFound() {
        // given
        when(teamRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CustomException.class,
                () -> teamService.getTeamMembers(1L)
        );
    }

}