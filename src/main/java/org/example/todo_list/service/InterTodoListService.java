package org.example.todo_list.service;

import org.example.todo_list.dto.response.GetListResponse;

public interface InterTodoListService {
    public void create(String category);

    public void delete(Long id);

    public void changeListCategory(Long id, String newCategory);


    public GetListResponse getListById(Long id);

//    public List<GetListByIdResponse> getAllLists();
}
