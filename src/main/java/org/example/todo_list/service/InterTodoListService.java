package org.example.todo_list.service;

import org.example.todo_list.dto.response.GetListResponse;

public interface InterTodoListService {
    void create(String category);

    void delete(Long id);

    void changeListCategory(Long id, String newCategory);


    GetListResponse getListById(Long id);

//    public List<GetListByIdResponse> getAllLists();
}
