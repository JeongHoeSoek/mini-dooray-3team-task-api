package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.TaskRequest;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskResponse;
import com.nhnacademy.minidooray3teamaccountapi.service.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Task 생성
    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequest taskRequest) {

        TaskResponse taskResponse = taskService.createTask(projectId, taskRequest);
        return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    // Task 개별 조회
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        TaskResponse taskResponse = taskService.getTaskById(projectId, taskId);
        return ResponseEntity.ok(taskResponse);
    }

    // Task 수정
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody TaskRequest taskRequest) {

        TaskResponse taskResponse = taskService.updateTask(projectId, taskId, taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    // Task 삭제
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    // 특정 멤버가 작성한 Task 조회
    @GetMapping("/members/{memberId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByMemberId(
            @PathVariable Long projectId,
            @PathVariable Long memberId) {

        List<TaskResponse> tasks = taskService.getTasksByMemberId(projectId, memberId);
        return ResponseEntity.ok(tasks);
    }
}
