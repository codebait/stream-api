package com.codebait.stream;

import com.codebait.stream.model.Person;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamAdvancedMethodsTest {

  private final PersonSupplier personSupplier = new PersonSupplier();


  @Test
  void mapToInt() {
    IntStream intStream = personSupplier.getPersonStream()
        .mapToInt(Person::age);
    System.out.println(intStream.sum());
  }


  @Test
  void sortedV2() {

    Comparator<Person> comparing = Comparator.comparing(Person::money).reversed();
    personSupplier.getPersonStream()
        .sorted(Comparator.comparing(Person::age).thenComparing(comparing))
        .forEach(System.out::println);
  }


  @Test
  void takeWhile() {
    personSupplier.getPersonStream()
        .takeWhile(person -> person.age() > 20)
        .forEach(System.out::println);
  }


  @Test
  void dropWhile() {
    personSupplier.getPersonStream()
        .dropWhile(person -> person.age() > 20)
        .forEach(System.out::println);
  }


  @Test
  void min() {
    Optional<Person> max = personSupplier.getPersonStream()
        .min(Comparator.comparing(Person::age));
    System.out.println(max);
  }


  @Test
  void max() {
    Optional<Person> max = personSupplier.getPersonStream()
        .max(Comparator.comparing(Person::age));
    System.out.println(max);
  }


  @Test
  void parallel() {
    personSupplier.getPersonStream()
        .parallel()
        .map(Person::name)
        .sorted()
        .forEachOrdered(System.out::println);

  }


  @Test
  void reduceV1() {
    Optional<BigDecimal> reduce = personSupplier.getPersonStream()
        .map(Person::money)
        .reduce(
            (bigDecimal, augend) -> {
              return bigDecimal.add(augend);
            }
        );
    System.out.println(reduce);
  }


  @Test
  void reduceV2() {
    BigDecimal reduce = personSupplier.getPersonStream()
        .map(Person::money)
        .reduce(BigDecimal.ZERO,
            BigDecimal::add
        );
    System.out.println(reduce);
  }


  @Test
  void reduceV3() {
    BigDecimal reduce = personSupplier.getPersonStream()
        .map(Person::money)
        .reduce(BigDecimal.ZERO,
            (bigDecimal1, augend1) -> bigDecimal1.add(augend1),
            (bigDecimal, augend) -> bigDecimal.add(augend));
    System.out.println(reduce);
  }


  @Test
  void reduceV2_debug() {
    BigDecimal reduce = personSupplier.getPersonStream()
        .parallel()
        .map(Person::money)
        .reduce(BigDecimal.ZERO,
            (bigDecimal, augend) -> {
              System.out
                  .println(Thread.currentThread() + " accumulator " + bigDecimal + " " + augend);
              return bigDecimal.add(augend);
            });
    System.out.println(reduce);
  }


  @Test
  void reduceV3_debug() {
    BigDecimal reduce = personSupplier.getPersonStream()
        .parallel()
        .map(Person::money)
        .reduce(BigDecimal.ZERO,
            (bigDecimal, augend) -> {
              System.out
                  .println(Thread.currentThread() + " accumulator " + bigDecimal + " " + augend);
              return bigDecimal.add(augend);
            },
            (bigDecimal, augend) -> {
              System.out
                  .println(Thread.currentThread() + " combiner " + bigDecimal + " " + augend);
              return bigDecimal.add(augend);
            });
    System.out.println(reduce);
  }


  @Test
  void reduceV4() {
    BigDecimal reduce = personSupplier.getPersonStream()
        .reduce(BigDecimal.ZERO,
            (bigDecimal1, person) -> bigDecimal1.add(person.money()),
            (bigDecimal, augend) -> bigDecimal.add(augend));
    System.out.println(reduce);
  }


  @Test
  void collectmapV1() {
    Map<String, Integer> collect = personSupplier.getPersonStream()
        .collect(Collectors.toMap(Person::name, Person::age));
    System.out.println(collect);
    System.out.println(collect.getClass());
  }


  @Test
  void collectMapV2() {
    Map<Integer, String> collect = personSupplier.getPersonStream()
        .collect(Collectors.toMap(Person::age, Person::name, (s, s1) -> s1));
    System.out.println(collect);
    System.out.println(collect.getClass());
  }


  @Test
  void collectV2() {
    Map<Boolean, List<Person>> collect = personSupplier.getPersonStream()
        .collect(Collectors.groupingBy(person -> person.age() >= 18));
    System.out.println(collect);
  }


  @Test
  void collectV3() {
    Map<Boolean, Set<Person>> collect = personSupplier.getPersonStream()
        .collect(Collectors.groupingBy(person -> person.age() >= 18, Collectors.toSet()));
    System.out.println(collect);
  }


  @Test
  void collectV4() {
    Map<Boolean, List<String>> collect = personSupplier.getPersonStream()
        .collect(Collectors.groupingBy(person -> person.age() >= 18,
            Collectors.mapping(Person::name, Collectors.toList())));
    System.out.println(collect);
  }


  @Test
  void collectV5() {
    Map<Boolean, Long> collect = personSupplier.getPersonStream()
        .collect(Collectors.groupingBy(person -> person.age() >= 18, Collectors.counting()));
    System.out.println(collect);
  }


  @Test
  void collectV6() {

    HashMap<String, Person> collect = personSupplier.getPersonStream()
        .collect(
            Collectors.teeing(
                Collectors.minBy(Comparator.comparing(Person::money)),
                Collectors.maxBy(Comparator.comparing(Person::money)),
                (min, max) -> {
                  HashMap<String, Person> map = new HashMap<>();
                  min.ifPresent(p -> map.put("min", p));
                  max.ifPresent(p -> map.put("max", p));
                  return map;
                }
            )
        );
    System.out.println(collect);
  }


  @Test
  void parallelV1() {
    List<String> list =
        Stream.generate(new AtomicInteger()::getAndIncrement)
            .parallel()
            .limit(10)
            .map(integer -> integer + " " + Thread.currentThread().getName())
            .collect(Collectors.toList());

    list.forEach(System.out::println);
  }


  @Test
  void parallelV2() throws ExecutionException, InterruptedException {
    ForkJoinPool customThreadPool = new ForkJoinPool(4);
    List<String> list = customThreadPool.submit(() ->
        Stream.generate(new AtomicInteger()::getAndIncrement)
            .parallel()
            .limit(10)
            .map(integer -> integer + " " + Thread.currentThread().getName())
            .collect(Collectors.toList())
    ).get();
    list.forEach(System.out::println);
  }


  @Test
  void file() throws IOException {
    Stream<String> lines = Files.lines(Paths.get("pom.xml"));
    lines.forEach(System.out::println);

  }


  @Test
  void fileFixed() throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get("pom.xml"))) {
      lines.forEach(System.out::println);
    }

  }


}
