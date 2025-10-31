package study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AssertJTest {

    @Test
    void equalsTest() {
        int result = 2 + 2;
        assertThat(result).isEqualTo(4);
    }

    @Test
    void stringTest() {
        String name = "Spring";
        assertThat(name)
                .startsWith("Sp")
                .endsWith("ing")
                .contains("rin");
    }

    @Test
    void collectionTest() {
        List<String> list = List.of("A", "B", "C");
        assertThat(list)
                .hasSize(3)
                .contains("A")
                .doesNotContain("D");
    }

    @Test
    void numberTest() {
        int value = 15;
        assertThat(value)
                .isPositive()
                .isBetween(10, 20)
                .isNotZero();
    }

    @Test
    void booleanTest() {
        boolean flag = true;
        Assertions.assertThat(flag).isTrue();
    }

    @Test
    void nullCheck() {
        String text = null;
        Assertions.assertThat(text).isNull();
    }

    @Test
    void shouldThrowException() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("잘못된 입력");
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된");
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "3, 4, 7",
            "10, 5, 15"
    })
    void addTest1(int a, int b, int expected) {
        int result = a + b;
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3, 6",
            "5, 5, 5, 15",
            "100, 200, 300, 600"
    })
    void addTest2(int a, int b, int c, int expected) {
        int result = a + b + c;
        assertThat(result).isEqualTo(expected);
    }

    @RepeatedTest(5)
    void randomTest(RepetitionInfo info) {
        int value = ThreadLocalRandom.current().nextInt(1, 10);
        System.out.println("반복 횟수: " + info.getCurrentRepetition());
        assertThat(value).isBetween(1, 9);
    }
}
