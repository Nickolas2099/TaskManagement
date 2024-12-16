package com.example.TaskManagement.controller;

import com.example.TaskManagement.domain.api.task.TaskReq;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/byAuthor")
    public ResponseEntity<Response> getTaskByAuthor(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "author") String author) {

        log.info("START endpoint getTaskByAuthor, author: `{}`", author);
        ResponseEntity<Response> resp = taskService.getByAuthor(author, page, size);
        log.info("END endpoint getTaskByAuthor, resp: `{}`", resp);
        return resp;
    }

    @GetMapping("/byExecutor")
    public ResponseEntity<Response> getTaskByAssignment(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "executor") String executor) {

        log.info("START endpoint getTaskByAssignment, executor: `{}`", executor);
        ResponseEntity<Response> resp = taskService.getByAssignment(executor, page, size);
        log.info("END endpoint getTaskByAssignment, resp: `{}`", resp);
        return resp;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> editTask(@PathVariable Integer id, @Validated @RequestBody TaskReq req) {

        log.info("START endpoint editTask, req: `{}`", req);
        ResponseEntity<Response> resp = taskService.edit(id, req);
        log.info("END endpoint editTask, resp: `{}`", resp);
        return resp;
    }

    @PostMapping
    public ResponseEntity<Response> saveTask(@Validated @RequestBody TaskReq req) {

        log.info("START endpoint saveTask, req: `{}`", req);
        ResponseEntity<Response> resp = taskService.save(req);
        log.info("END endpoint saveTask, resp: `{}`", resp);
        return resp;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable Integer id) {

        log.info("START endpoint deleteTask, id: `{}`", id);
        ResponseEntity<Response> resp = taskService.delete(id);
        log.info("END endpoint deleteTask, resp: `{}`", resp);
        return resp;
    }

    @PatchMapping("/changePriority")
    public ResponseEntity<Response> changePriority(@RequestParam String heading, @RequestParam String title) {

        log.info("START endpoint changePriority, heading: `{}`, title: `{}`", heading, title);
        ResponseEntity<Response> resp = taskService.changePriority(heading, title);
        log.info("END endpoint changePriority, resp: `{}`", resp);
        return resp;
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<Response> changeStatus(@RequestParam String heading, @RequestParam String title) {

        log.info("START endpoint changeStatus, heading: `{}`, title: `{}`", heading, title);
        ResponseEntity<Response> resp = taskService.changeStatus(heading, title);
        log.info("END endpoint changeStatus, resp: `{}`", resp);
        return resp;
    }
}
