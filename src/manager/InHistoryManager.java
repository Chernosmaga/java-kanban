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
        for (Node<Task> node : receivedTasksMap.values()) {
            Node<Task> lookedForNode = receivedTasksMap.get(node); // получаю ноду, которую искал по идентификатору
            removeNode(lookedForNode); // удаляю ноду
            receivedTasksMap.remove(lookedForNode); // удаляю ноду из мапы
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public Node<Task> linkLast(Task task) {
        final Node<Task> oldTail = tail;
        // наставник сказал, что лучше значение добавить в начало
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(); // пустой лист, куда буду складывать задачи для истории
        Node<Task> currentNode = head; // голова списка становится нынешней нодой
        while (!(currentNode == null)) {
            tasks.add(currentNode.data); // добавляю данные о ноде в лист
            currentNode = currentNode.next; // перехожу к следующему значению
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (!(node == null)) {
            final Node<Task> next = node.next;
            final Node<Task> previous = node.previous;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && !(tail == node)) {
                head = next;
                head.previous = null;
            } else if (!(head == node) && tail == node) {
                tail = previous;
                tail.next = null;
            } else {
                previous.next = next;
                next.previous = previous;
            }
        }
    }

}
