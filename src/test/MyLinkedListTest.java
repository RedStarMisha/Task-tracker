import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    MyLinkedList myLinkedList = new MyLinkedList();

    @Test
    void shouldGetEmptyConvertedToArrayList() {
        Assertions.assertTrue(myLinkedList.isEmpty());
    }

}