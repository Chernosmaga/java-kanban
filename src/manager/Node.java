package manager;

public class Node<T> {
    public T data;
    public Node<T> previous;
    public Node<T> next;

    public Node(T data, Node<T> previous, Node<T> next) {
        this.previous = previous;
        this.data = data;
        this.next = next;
    }

}
