package stack;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Stack<E> {

    private static int DEFAULT_INITIALCPAPCITY = 10;
    private static int DEFAULT_INCREASEPAPCITY = 2;

    private int increaseCapacity;
    private int head;
    private Object[] array;

    public Stack() {
        this(DEFAULT_INITIALCPAPCITY, DEFAULT_INCREASEPAPCITY);
    }

    public Stack(int initialCapacity) {
        this(initialCapacity, DEFAULT_INCREASEPAPCITY);
    }

    public Stack(int initialCapacity, int increaseCapacity) {
        this.increaseCapacity = increaseCapacity;
        this.array = new Object[initialCapacity];
        this.head = -1;
    }

    public E push(E item) {
        if (head == array.length - 1) {
            array = increaseArray();
        }

        array[++head] = item;
        return item;
    }

    /**
     * 스택이 가득 찼을경우 Array 확장하는 과정이 자주 일어나면 속도가 느려지며
     * 확장을 너무크게하면 메모리낭비가 심하므로 적당한 수준에서 확장할 필요가 있다.
     *
     * Java.Util.Stack 스펙에서는 기본길이에 2배로 늘리는 방식
     */
    @NotNull
    private Object[] increaseArray() {
        return Arrays.copyOf(array, array.length * increaseCapacity);
    }

    /**
     * 가장 나중에 들어온 아이템이 가장 먼저 나간다.
     */
    public synchronized E pop() {
        validateOutOfIndex();
        E item = peek();
        array[head--] = null;
        return item;
    }

    public synchronized E peek() {
        validateOutOfIndex();
        return (E) array[head];
    }

    public boolean empty() {
        return size() == 0;
    }

    public int stackCapacity() {
        return array.length;
    }

    public int increaseCapacity() {
        return increaseCapacity;
    }

    public int size() {
        return head + 1;
    }

    private void validateOutOfIndex() {
        if (empty()) {
            throw new IndexOutOfBoundsException();
        }
    }

}
