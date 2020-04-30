package com.codebait.stream;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StreamBuilderAdvancedTest {


  @Test
  void shouldGenerateBySupplierV1() {
    Supplier<Integer> supplier = new Supplier<>() {
      final AtomicInteger atomicInteger = new AtomicInteger();

      @Override
      public Integer get() {
        return atomicInteger.getAndIncrement();
      }
    };

    List<Integer> list = Stream.generate(supplier)
        .limit(10)
        .collect(Collectors.toList());
    Assertions.assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);
    System.out.println(list);
  }


  @Test
  void shouldGenerateBySupplierV2() {

    List<Integer> list = Stream.generate(new AtomicInteger()::getAndIncrement)
        .limit(10)
        .collect(Collectors.toList());
    Assertions.assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);
    System.out.println(list);

  }


  @Test
  void shouldGenerateByIterate() {

    List<String> list = Stream.iterate("a", i -> "-" + i)
        .limit(5)
        .collect(Collectors.toList());
    System.out.println(list);
    Assertions.assertIterableEquals(List.of("a", "-a", "--a", "---a", "----a"), list);

  }


  @Test
  void shouldGenerateByIterateV2() {

    List<String> list = Stream.iterate("a", text -> text.length() <= 5, i -> "--" + i)
        .collect(Collectors.toList());
    System.out.println(list);
    Assertions.assertIterableEquals(List.of("a", "--a", "----a"), list);
  }


}
