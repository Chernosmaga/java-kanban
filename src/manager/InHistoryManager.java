package manager;

import task.Task;

import java.util.*;

public class InHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> receivedTasksMap;
    private Node<Task> head;
    private Node<Task> tail;

    public InHistoryManager() {
        receivedTasksMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Node<Task> node = linkLast(task); // беру возвращаемую ноду
        receivedTasksMap.put(task.getId(), node); // добавляю в мапу
    }

    @Override
    public void remove(int id) {
        for (Integer identifier : receivedTasksMap.keySet()) {
            receivedTasksMap.remove(identifier); // получаю идентификатор ноды и удаляю данные
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public Node<Task> linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        if (receivedTasksMap.containsKey(task.getId())) {
            receivedTasksMap.remove(newNode);
            removeNode(newNode);
        }
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>(); // пустой лист, куда буду складывать задачи для истории
        Node<Task> currentNode = head; // голова списка становится нынешней нодой
        while (currentNode != null) {
            tasks.add(currentNode.getData()); // добавляю данные о ноде в лист
            currentNode = currentNode.getNext(); // перехожу к следующему значению
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrevious();
            node.setData(null);

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && !(tail == node)) {
                head = next;
                head.setPrevious(null);
            } else if (!(head == node) && tail == node) {
                tail = previous;
                tail.setNext(null);
            } else {
                previous.setNext(next);
                next.setPrevious(previous);
            }
        }
    }

}
