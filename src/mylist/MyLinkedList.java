package mylist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyLinkedList<T> {
    Node<T> head;
    Node<T> tail;
    int size;

    public void add(T element) {
        if (head == null) {
            addFirst(element);
        } else {
            addLast(element);
        }
    }

    public void add(int index, T element) {
        checker(index);
        Node nextNode = findNode(index);
        if (nextNode.previous != null) {
            Node previousNode = nextNode.previous;
            Node newNode = new Node<>(previousNode, element, nextNode);
            previousNode.next = newNode;
            nextNode.previous = newNode;
        } else {
            head = new Node<>(null, element, nextNode);
            nextNode.previous = head;
        }
        size++;
    }

    private void addFirst(T element) {
        if (head == null) {
            head = new Node<T>(element);
        } else {
            Node oldHead = head;
            head = new Node<T>(null, element, oldHead);
            head.next = oldHead;
            oldHead.previous = head;
        }
        size++;
    }

    private void addLast(T element) {
        if (tail == null) {
            tail = new Node<T>(element);
            tail.previous = head;
            head.next = tail;
        } else {
            Node oldTail = tail;
            tail = new Node<T>(element);
            tail.previous = oldTail;
            oldTail.next = tail;
        }
        size++;
    }

    public T getFirst() {
        if (head == null) {
            throw new NullPointerException();
        }
        return (T) head.element;
    }

    @Override
    public String toString() {
        String result = "[";
        for (Node i = head; i != null; i = i.next) {
            result += i.next == null ? i.element + "]" : i.element + ", ";
        }
        return result;
    }

    public List<T> toArrayList () {
        List <T> convertToArrayList = new ArrayList<>();
        Node node = head;
        while (node.next != null) {
            convertToArrayList.add((T) node.element);
            node = node.next;
        }
        convertToArrayList.add((T)node.element);
        return convertToArrayList;
    }

    public int size() {
        return size;
    }

    public void delete(int index) {
        checker(index);
        redefinitionNodeWhenDeleting(findNode(index));
    }

    public void delete(T element) {
        redefinitionNodeWhenDeleting(findNode(element));
    }

    private void redefinitionNodeWhenDeleting (Node deleteNode) {
        if (deleteNode.previous == null) {
            head = deleteNode.next;
            head.previous = null;
        } else if (deleteNode.next == null){
            deleteNode.previous.next = null;
        } else {
            deleteNode.next.previous = deleteNode.previous;
            deleteNode.previous.next = deleteNode.next;
        }
        deleteNode.element = null;
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

    private Node<T> findNode(Object element) {
            for (Node i = head; i != null; i = i.next) {
                if (i.element.equals(element)) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
    }

    private void checker(int index) {
        if (index < 0 && index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private class Node<T> {
        public Node next;
        public Node previous;
        public T element;

        public Node(T element) {
            this.element = element;
        }

        private Node(Node previous, T element, Node next) {
            this.previous = previous;
            this.element = element;
            this.next = next;
        }

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

    }
}
