package stack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class StackTest {

    private static int DEFAULT_INITIALCPAPCITY = 10;
    private static int DEFAULT_INCREASEPAPCITY = 2;

    @Test
    void 스택생성시_기본사이즈는_10() {
        Stack<Integer> stack = new Stack<>();
        assertThat(stack.stackCapacity()).isEqualTo(DEFAULT_INITIALCPAPCITY);
    }

    @Test
    void 스택생성시_기본증가사이즈는_100() {
        Stack<Integer> stack = new Stack<>();
        assertThat(stack.increaseCapacity()).isEqualTo(DEFAULT_INCREASEPAPCITY);
    }

    @Test
    void 스택_1개_푸쉬할경우_사이즈는_1() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        assertThat(stack.size()).isEqualTo(1);
    }

    @Test
    void 스택_11개_푸쉬할경우_스택사이즈_110() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(6);
        stack.push(7);
        stack.push(8);
        stack.push(9);
        stack.push(10);
        stack.push(11);
        assertThat(stack.stackCapacity()).isEqualTo(110);
    }

    @DisplayName("스택_푸쉬_팝_테스트")
    @ParameterizedTest
    @MethodSource("스택_팝_테스트데이터")
    void 스택_팝_테스트(Integer[] pushArray, Integer popSize, List<Integer> result) {
        Stack<Integer> stack = new Stack<>();
        Arrays.stream(pushArray).forEach(stack::push);
        IntStream.range(0, popSize).boxed().forEach(integer -> stack.pop());
        while (stack.empty()) {
            result.contains(stack.pop());
        }
    }

    @DisplayName("천만개에 데이터를 푸쉬하는데 1초안에 처리되야 한다.")
    @Test()
    void 푸쉬_실행시간_테스트() {
        Stack<Integer> stack = new Stack<>();
        assertTimeout(Duration.ofMillis(1000L), () -> {
            for (int i = 0; i < 10000000; i++) {
                stack.push(i);
            }
        });
    }

    static Stream<Arguments> 스택_팝_테스트데이터() {
        return Stream.of(
                Arguments.arguments(new Integer[]{1,2,3,4,5}, 2, List.of(1,2,3)),
                Arguments.arguments(new Integer[]{1,2,3,4,5}, 4, List.of(1))
        );
    }

}