package com.codebait.stream;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import org.junit.jupiter.api.Test;

class StreamBuilderBaseTest {


  @Test
  void shouldGenerateByStreamMethod() {

    List<String> list = List.of("pies", "kot");
    Stream<String> stream = list.stream();
    stream.forEach(s -> System.out.println(s));


  }


  @Test
  void shouldGenerateByFactoryMethodOf() {

    Stream<String> stream = Stream.of("pies", "kot", "ryba");
    stream.forEach(System.out::println);

  }


  @Test
  void shouldGenerateByFactoryMethodOfNullable() {
    List<String> list = new ArrayList<>();
    list.add("test");
    list.add("test2");
    list.add(null);

    String text = "text";
    Stream<List<String>> stream = Stream.ofNullable(list);
    stream.forEach(System.out::println);

  }


  @Test
  void shouldGenerateByBuilder() {
    Builder<String> builder = Stream.builder();
    builder.add("ryba1");
    builder.add("ryba2");
    Stream<String> stream = builder
        .add("pies")
        .add("kot")
        .build();
    stream
        .forEach(System.out::println);


  }


}
