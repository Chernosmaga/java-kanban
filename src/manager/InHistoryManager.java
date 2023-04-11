package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InHistoryManager implements HistoryManager {

    protected List<Task> viewedTasks; // лист для хранения просмотренных задач

    public InHistoryManager() {
        this.viewedTasks = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        if (viewedTasks.size() <= 10) { // проверяю, чтоб лист не превышал 10 элементов
            viewedTasks.add(task);
        } else {
            removeTask(task.getId()); // если больше 10, то задача удаляется
        }
    }

    @Override
    public void removeTask(int id) {
        // этот метод я оставил, потому что при удалении Объекта из мапы в классе InMemoryManager
        // нужно так же удалить его из листа просмотренных задач, потому что его больше не существует
        viewedTasks.remove(viewedTasks.get(id));
    }

    @Override
    public List<Task> getHistory() { // непосредственно, получаю историю
        return viewedTasks;
    }

}
