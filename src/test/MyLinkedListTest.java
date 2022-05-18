import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    MyLinkedList myLinkedList = new MyLinkedList();

    @Test
    void shouldGetEmptyConvertedToArrayList() {
        Assertions.assertTrue(myLinkedList.isEmpty());
    }

    @Test
    void shouldClearMyLinkedList() {
        myLinkedList.add(1);
        myLinkedList.add(2);
        myLinkedList.add(3);
        myLinkedList.add(4);
        myLinkedList.clear();
        Assertions.assertTrue(myLinkedList.isEmpty());
    }

}