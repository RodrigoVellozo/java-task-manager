package com.rvsd.todolist;

import com.rvsd.todolist.entity.Todo;

import java.util.ArrayList;
import java.util.List;

public class TestConstants {
    public static final List<Todo> TODOS = new ArrayList<>(){
        {
            add(new Todo(1L,"teste", "descricao teste", false, 2));
            add(new Todo(2L,"dar banho no cachorro", "dar banho no cachorro com shampoo neutro", false, 2));
            add(new Todo(3L,"trabalhar", "construir apps para terceiros", false, 1));
        }
    };

    public static final Todo TODO = TODOS.get(0);
}
