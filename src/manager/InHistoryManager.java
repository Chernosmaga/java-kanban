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
        viewedTasks.add(task);
    }

    @Override
    public void removeTask(int id) {
        if (viewedTasks.size() > 10) { // проверяю, чтоб лист не превышал 10 элементов
            viewedTasks.remove(viewedTasks.get(id));
        }
    }

    @Override
    public List<Task> getHistory() { // непосредственно, получаю историю
        return viewedTasks;
    }

}
