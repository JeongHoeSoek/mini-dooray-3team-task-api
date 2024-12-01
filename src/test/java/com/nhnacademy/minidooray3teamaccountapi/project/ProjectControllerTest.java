package com.nhnacademy.minidooray3teamaccountapi.project;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.ProjectController;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectCreateRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectDetailsDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberSummaryDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.UserProjectDTO;
import com.nhnacademy.minidooray3teamaccountapi.exception.GlobalExceptionHandler;
import com.nhnacademy.minidooray3teamaccountapi.exception.MemberAlreadyExistsException;
import com.nhnacademy.minidooray3teamaccountapi.exception.ProjectNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.ProjectService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ProjectControllerTest {

    private final ProjectService projectService = mock(ProjectService.class);
    private final ProjectController projectController = new ProjectController(projectService);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(projectController)
            .setControllerAdvice(new GlobalExceptionHandler()) // Exception Handler 적용
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createProject_Success() throws Exception {
        ProjectCreateRequestDTO requestDTO = new ProjectCreateRequestDTO("New Project", "ACTIVE");
        ProjectDTO responseDTO = new ProjectDTO(1L, "New Project", "ACTIVE", null);

        when(projectService.createProject(eq("user1"), any(ProjectCreateRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/projects")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(1L))
                .andExpect(jsonPath("$.name").value("New Project"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getProjectDetails_Success() throws Exception {
        ProjectDetailsDTO responseDTO = new ProjectDetailsDTO(
                1L, "Test Project", "ACTIVE", null, List.of(), List.of(), List.of(), List.of()
        );

        when(projectService.getProjectDetails(eq("user1"), eq(1L))).thenReturn(responseDTO);

        mockMvc.perform(get("/projects/1")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getProjectDetails_NotFound() throws Exception {
        when(projectService.getProjectDetails(eq("user1"), eq(1L)))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(get("/projects/1")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Project not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getUserProjects_Success() throws Exception {
        List<UserProjectDTO> responseDTOs = List.of(
                new UserProjectDTO(1L, "Project 1", "ACTIVE"),
                new UserProjectDTO(2L, "Project 2", "INACTIVE")
        );

        when(projectService.getUserProjects(eq("user1"))).thenReturn(responseDTOs);

        mockMvc.perform(get("/projects/users/user1/projects")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].projectId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].projectId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Project 2"))
                .andExpect(jsonPath("$[1].status").value("INACTIVE"));
    }

//    @Test
//    void deleteProject_Success() throws Exception {
//        doNothing().when(projectService).deleteProject(eq("user1"), eq(1L));
//
//        mockMvc.perform(delete("/projects/1")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isNoContent());
//    }

//    @Test
//    void deleteProject_Unauthorized() throws Exception {
//        doThrow(new UnauthorizedAccessException("User is not authorized to delete the project"))
//                .when(projectService).deleteProject(eq("user1"), eq(1L));
//
//        mockMvc.perform(delete("/projects/1")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.title").value("User is not authorized to delete the project"))
//                .andExpect(jsonPath("$.status").value(403))
//                .andExpect(jsonPath("$.timestamp").exists());
//    }

    @Test
    void addMemberToProject_Success() throws Exception {
        // Given
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("user2", "MEMBER");
        ProjectMemberDTO responseDTO = new ProjectMemberDTO(1L, "user2", "MEMBER"); // projectMemberId를 반환

        // Mock Service 호출 시 반환값 설정
        when(projectService.addMemberToProject(eq("user1"), eq(1L), any(ProjectMemberSummaryDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/projects/1/members")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO)))
                .andDo(print()) // 실제 응답 출력
                .andExpect(status().isOk()) // HTTP 200 상태 확인
                .andExpect(jsonPath("$.projectMemberId").value(1L)) // JSON 응답의 projectMemberId 확인
                .andExpect(jsonPath("$.userId").value("user2")) // JSON 응답의 userId 확인
                .andExpect(jsonPath("$.role").value("MEMBER")); // JSON 응답의 role 확인
    }


    @Test
    void addMemberToProject_AlreadyExists() throws Exception {
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("user2", "MEMBER");

        when(projectService.addMemberToProject(eq("user1"), eq(1L), any(ProjectMemberSummaryDTO.class)))
                .thenThrow(new MemberAlreadyExistsException("Member already exists"));

        mockMvc.perform(post("/projects/1/members")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Member already exists"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }

//    @Test
//    void removeMemberFromProject_Success() throws Exception {
//        doNothing().when(projectService).removeMemberFromProject(eq("user1"), eq(1L), eq("user2"));
//
//        mockMvc.perform(delete("/projects/1/members/user2")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isNoContent());
//    }

//    @Test
//    void removeMemberFromProject_NotFound() throws Exception {
//        doThrow(new MemberNotFoundException("Member not found in the project"))
//                .when(projectService).removeMemberFromProject(eq("user1"), eq(1L), eq("user2"));
//
//        mockMvc.perform(delete("/projects/1/members/user2")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.title").value("Member not found in the project"))
//                .andExpect(jsonPath("$.status").value(404))
//                .andExpect(jsonPath("$.timestamp").exists());
//    }
}
