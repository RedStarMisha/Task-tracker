package mylist;

public class MyLinkedList<T> {
    Node head;
    Node tail;
    int size;

    public void add(T element) {
        if (head == null) {
            addFirst(element);
        } else {
            addLast(element);
        }
    }

    public void add(int index, T element) {
        indChecher(index);
        Node previousNode = findNode(index -1);
        Node nextNode = findNode(index);
        Node newNode = new Node<>(element);
        previousNode.next = newNode;
        newNode.previous = previousNode;
        newNode.next = nextNode;
        nextNode.previous = newNode;
        size++;
    }

    private void addFirst(T element) {
        if (head == null) {
            head = new Node(element);
        } else {
            Node oldHead = head;
            head = new Node(element);
            head.next = oldHead;
            oldHead.previous = head;
        }
        size++;
    }

    private void addLast(T element) {
        if (tail == null) {
            tail = new Node<>(element);
            tail.previous = head;
            head.next = tail;
        } else {
            Node oldTail = tail;
            tail = new Node(element);
            tail.previous = oldTail;
            oldTail.next = tail;
        }
        size++;
    }

    public T getFirst() {
        if (head == null) {
            throw new IndexOutOfBoundsException();
        }
        return (T) head.element;
    }

    @Override
    public String toString() {
        String result = "[" + getFirst();
        //Node node = head.next;

        for (Node i = head; i != null; i = i.next) {
            result += ", " + node.element;
            node = node.next;
        }
        result +="]";
        return result;
    }

    public int size() {
        return size;
    }

    public void delete(int index) {
        indChecher(index);
        Node deleteNode = findNode(index);
        deleteNode.next.previous = deleteNode.previous;
        deleteNode.previous.next = deleteNode.next;
        size--;
    }

    private Node<T> findNode(int index) {
        Node findNode;
        if (index >= size/2) {
            findNode = tail;
            for (int i = size - 2; i >= index; i--) {
                findNode = findNode.previous;
            }
        } else {
            findNode = head;
            for (int i = 1; i <= index; i++) {
                findNode = findNode.next;
            }
        }
        return findNode;
    }

    private void indChecher(int index) {
        if (index < 0 && index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private class Node<T> {
        public Node next;
        public Node previous;
        public T element;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<?> node = (Node<?>) o;

            if (next != null ? !next.equals(node.next) : node.next != null) return false;
            if (previous != null ? !previous.equals(node.previous) : node.previous != null) return false;
            return element.equals(node.element);
        }

        @Override
        public int hashCode() {
            int result = next != null ? next.hashCode() : 0;
            result = 31 * result + (previous != null ? previous.hashCode() : 0);
            result = 31 * result + element.hashCode();
            return result;
        }

        public Node(T element) {
            this.element = element;
        }
    }
}
