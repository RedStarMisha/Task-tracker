import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        checkerIndex(index);
        Node nextNode = findNode(index);
        if (index == 0) {
            head = new Node<>(null, element, nextNode);
            nextNode.previous = head;
        } else  {
            Node previousNode = nextNode.previous;
            Node newNode = new Node<>(previousNode, element, nextNode);
            previousNode.next = newNode;
            nextNode.previous = newNode;
        }
        size++;
    }

    private void addFirst(T element) {
        if (head == null) {
            head = new Node<>(element);
        } else {
            Node oldHead = head;
            head = new Node<T>(null, element, oldHead);
            oldHead.previous = head;
        }
        size++;
    }

    private void addLast(T element) {
        if (tail == null) {
            tail = new Node<>(head, element, null);
            head.next = tail;
        } else {
            Node oldTail = tail;
            tail = new Node<>(oldTail, element, null);
            oldTail.next = tail;
        }
        size++;
    }

    public T getFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Список пуст");
        }
        return head.element;
    }

    public List<T> toArrayList() throws NoSuchElementException {
            if (isEmpty()) {
                return new ArrayList<>();
            }
            List<T> convertedToArrayList = new ArrayList<>();
            Node<T> node = head;
            while (node.next != null) {
                convertedToArrayList.add(node.element);
                node = node.next;
            }
            convertedToArrayList.add(node.element);
            return convertedToArrayList;
    }

    public int size() {
        return size;
    }

    public void delete(int index) {
        checkerIndex(index);
        redefinitionNodeWhenDeleting(findNode(index));
    }

    public void delete(T element) throws NoSuchElementException {
        try{
            redefinitionNodeWhenDeleting(findNode(element));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "[";
        for (Node i = head; i != null; i = i.next) {
            result += i.next == null ? i.element + "]" : i.element + ", ";
        }
        return result;
    }



    /**
     * метод переопределяет ссылки next и previous
     * при удалении элемента
     * @param deleteNode - удаляемый узел
     */
    private void redefinitionNodeWhenDeleting (Node deleteNode) {
        if (deleteNode.previous == null) {
            if (size > 1) {
                head = deleteNode.next;
                head.previous = null;
            } else {
                head = null;
            }
        } else if (deleteNode.next == null){
            if (size > 2) {
                tail = deleteNode.previous;
                tail.next = null;
            } else {
                tail = null;
            }
//            tail = deleteNode.previous.equals(head) ? null : deleteNode.previous;
//            tail.next = null;
        } else {
            deleteNode.next.previous = deleteNode.previous;
            deleteNode.previous.next = deleteNode.next;
        }
        size--;
    }

    /**
     * метод поиска узла с конца или
     * начала списка по индексу
     * @param index
     * @return - возвращает искомый узел
     */
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

    /**
     * метод поиска узла по элементу
     * @param element - элемент хранящийся в списке
     * @return
     */

    private Node<T> findNode(Object element) {
            for (Node i = head; i != null; i = i.next) {
                if (i.element.equals(element)) {
                    return i;
                }
            }
            throw new NoSuchElementException("Элемент отсутствует");
    }


    /**
     * метод проверки индекса элемента
     * @param index
     */
    private void checkerIndex(int index) {
        if (index < 0 && index >= size) {
            throw new IndexOutOfBoundsException("Элемент с таким индексом отсутствует");
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
