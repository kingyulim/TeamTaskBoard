package com.teamteskboard.domain.comment;

import com.teamteskboard.common.entity.Comment;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.comment.dto.request.CreatedCommentRequest;
import com.teamteskboard.domain.comment.dto.request.UpdateCommentRequest;
import com.teamteskboard.domain.comment.dto.response.CreatedCommentResponse;
import com.teamteskboard.domain.comment.dto.response.UpdateCommentResponse;
import com.teamteskboard.domain.comment.repository.CommentRepository;
import com.teamteskboard.domain.comment.service.CommentService;
import com.teamteskboard.domain.task.repository.TaskRepository;
import com.teamteskboard.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.teamteskboard.domain.task.enums.TaskPriorityEnum.HIGH;
import static com.teamteskboard.domain.user.enums.UserRoleEnum.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;


    // 댓글 생성 단위 테스트

    @Test
    @DisplayName("댓글 성공 - 태스크, 유저, 페런트 아이디가 일치 시 - 성공")
    void createComment_success() {
        //given
        //댓글이 달릴 때 task Id
        Long taskId = 1L;
        // 댓글 작성자 user Id
        Long userId = 1L;

        //댓글 생성 요청 DTO (보통 content, parentId 등을 담음)
        CreatedCommentRequest request = new CreatedCommentRequest();

        //테스트용 유저 객체 생성
        // 이 유저가 실제 DB에 있다고 가정
        User testUser = new User("Lee", "Leee", "test22@test.com", "12345678", ADMIN);

        //이 유저가 실제 DB에 있다고 가정
        Task testTask = new Task("테스트", "체스트", HIGH, testUser, LocalDateTime.now());

        // 레포지토리.커리문에서 호출하면 testUser를 Optional로 감싸서 반환하도록 설정
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(testUser));

        // 레포지토리.커리문에서 호출하면 testTake를 Optional로 감싸서 반환하도록 설정
        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(testTask));

        //savedComment 먼저 만들어야 함 (DB가 저장 후 돌려주는 역할)
        Comment savedComment = new Comment(testUser, testTask, "댓글 내용", null);
        ReflectionTestUtils.setField(savedComment, "id", 100L);

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment); //생성해야함

        //when
        CreatedCommentResponse result = commentService.save(taskId, userId, request);

        //then

        //반환된 댓글 내용이 저장된 내용과 같은지 확인
        assertThat(result.getId()).isEqualTo(100L);

        //반환된 댓글 내용이 저장된 내용과 같은지
        assertThat(result.getContent()).isEqualTo("댓글 내용");

        //부모 댓글이 없는 일반 댓글이므로 parentId가 null인지 확인
        assertThat(result.getParentId()).isNull();
    }

    //수정
    @Test
    @DisplayName("update: 댓글 수정 성공")
    void update() {
        //given
        Long taskId = 1L;
        Long userId = 1L; //작성자
        Long commentId = 2L;

        //수정 요청용 DTO 생성
        UpdateCommentRequest request = new UpdateCommentRequest(); //수정 요청 DTO 생성
        ReflectionTestUtils.setField(request, "content", "수정된 댓글");

        User user = buildUser(userId);
        Task task = buildTask(taskId, user);

        Comment comment = new Comment(user, task, "댓글", null); //기존 댓글
        ReflectionTestUtils.setField(comment, "id", commentId); // 댓글 id

        when(commentRepository.findByIdAndTaskIdAndIsDeletedFalse(commentId, taskId))
                .thenReturn(Optional.of(comment));

        //when
        UpdateCommentResponse result = commentService.update(userId, taskId, commentId, request);

        //then
        assertThat(result.getId()).isEqualTo(commentId);
        assertThat(result.getContent()).isEqualTo("수정된 댓글");
    }

    private User buildUser(Long userId) {
        User user = new User("Lee", "lee", "test22@test.com", "12345678", ADMIN);
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }

    private Task buildTask(Long taskId, User assignee) {
        Task task = new Task("Test", "test", HIGH, assignee, LocalDateTime.now());
        ReflectionTestUtils.setField(task, "id", taskId);
        return task;
    }
}
