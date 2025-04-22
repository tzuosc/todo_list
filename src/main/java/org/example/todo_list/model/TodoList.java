package org.example.todo_list.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

/*
 * TODO TodoList 模型
 *   id: Long
 *   category: String
 *   tasks: List<Task> 一对多关系
 *   注意, 为了维护双向关系, 你需要在模型里定义两个方法, 分别是 addTask, removeTask. 当为 todolist 添加任务或者删除任务的时候需要调用对应的方法来维护关系
 * */

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "todoList")
    @ToString.Exclude
    @JsonIgnoreProperties(value = "todoList")
    private List<Task> tasks;

    public void addTask(Task task) {
        tasks.add(task);
        task.setTodoList(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setTodoList(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TodoList todoList = (TodoList) o;
        return getId() != null && Objects.equals(getId(), todoList.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
