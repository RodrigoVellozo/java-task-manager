package com.rvsd.todolist.controller;

import com.rvsd.todolist.entity.Todo;
import com.rvsd.todolist.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todos")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    ResponseEntity<List<Todo>> create(@RequestBody @Valid Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(todo));
    }

    @PutMapping("{id}")
    ResponseEntity<List<Todo>> update(@PathVariable Long id, @RequestBody Todo todo) {
        List<Todo> updatedTodo = todoService.update(id, todo);
        if (updatedTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("{id}")
    List<Todo> delete(@PathVariable("id") Long id) {
        return todoService.delete(id);
    }

    @GetMapping
    List<Todo> list() {
        return todoService.list();
    }
}
