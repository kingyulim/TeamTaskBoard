package com.teamteskboard.task.entity.repository;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long > {
}
