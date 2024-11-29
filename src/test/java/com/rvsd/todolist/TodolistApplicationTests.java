package com.rvsd.todolist;

import com.rvsd.todolist.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.rvsd.todolist.TestConstants.TODO;
import static com.rvsd.todolist.TestConstants.TODOS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodolistApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateTodoSuccess() {
        var todo = new Todo();
        todo.setNome("primeira tarefa");
        todo.setDescricao("estudar leis123");
        todo.setRealizado(false);
        todo.setPrioridade(2);

        webTestClient
                .post()
                .uri("/api/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].nome").isEqualTo(todo.getNome())
                .jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
                .jsonPath("$[0].realizado").isEqualTo(todo.getRealizado())
                .jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());
    }

    @Test
    void testCreateTodoFailure() {
        var todo = new Todo(0L, "", "", false, 0);

        webTestClient
                .post()
                .uri("/api/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Sql("/import.sql")
    @Test
    void testUpdateTodoSuccess() {
        var todo = new Todo();
        todo.setId(TODO.getId());
        todo.setNome(TODO.getNome() + " Up");
        todo.setDescricao(TODO.getDescricao() + " Up");
        todo.setRealizado(!TODO.getRealizado());
        todo.setPrioridade(TODO.getPrioridade() + 1);

        webTestClient
                .put()
                .uri("/api/todos/" + todo.getId())
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].length()").isEqualTo(5)
                .jsonPath("$[0].nome").isEqualTo(todo.getNome())
                .jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
                .jsonPath("$[0].realizado").isEqualTo(todo.getRealizado())
                .jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());
    }

    @Test
    void testUpdateTodoFailure() {
        var unexistingId = 999999999L;
        var todo = new Todo(unexistingId, "", "", false, 0);

        webTestClient
                .put()
                .uri("/api/todos/" + unexistingId)
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Sql("/import.sql")
    @Test
    void testDeleteTodoSuccess() {
        webTestClient
                .delete()
                .uri("/api/todos/" + TODOS.get(0).getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(4)
                .jsonPath("$[0].nome").isEqualTo(TODOS.get(1).getNome())
                .jsonPath("$[0].descricao").isEqualTo(TODOS.get(1).getDescricao())
                .jsonPath("$[0].realizado").isEqualTo(TODOS.get(1).getRealizado())
                .jsonPath("$[0].prioridade").isEqualTo(TODOS.get(1).getPrioridade());
    }

    @Test
    void testDeleteTodoFailure() {
        var unexistingId = 0L;
        webTestClient
                .delete()
                .uri("/api/todos/" + unexistingId)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Sql("/import.sql")
    @Test
    public void testListTodos() throws Exception {
        webTestClient
                .get()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0]").isEqualTo(TODOS.get(0))
                .jsonPath("$[1]").isEqualTo(TODOS.get(1))
                .jsonPath("$[2]").isEqualTo(TODOS.get(2))
                .jsonPath("$[3]").isEqualTo(TODOS.get(3))
                .jsonPath("$[4]").isEqualTo(TODOS.get(4));
    }
}
