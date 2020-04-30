package com.codebait.stream;

import com.codebait.stream.model.Car;
import com.codebait.stream.model.Person;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamBaseMethodsTest {

  private final PersonSupplier personSupplier = new PersonSupplier();

  @Test
  void filter() {
    personSupplier.getPersonStream()
        .filter(person -> person.age() > 20)
        .forEach(System.out::println);
  }


  @Test
  void map() {
    Stream<String> stringStream = personSupplier.getPersonStream()
        .map(Person::name);
    stringStream
        .forEach(System.out::println);
  }


  @Test
  void flatMap() {
    personSupplier.getPersonStream()
        .map(Person::cars)
        .flatMap(Collection::stream)
        .map(Car::model)
        .forEach(System.out::println);
  }


  @Test
  void distinct() {
    Stream<String> distinct = personSupplier.getPersonStream()
        .map(Person::cars)
        .flatMap(List::stream)
        .map(Car::model)
        .distinct();
    distinct
        .forEach(System.out::println);
  }


  @Test
  void sortedV1() {
    personSupplier.getPersonStream()
        .map(Person::cars)
        .flatMap(List::stream)
        .map(Car::model)
        .distinct()
        .sorted()
        .forEach(System.out::println);
  }


  @Test
  void sortedV2() {
    personSupplier.getPersonStream()
        .map(Person::cars)
        .flatMap(List::stream)
        .map(Car::model)
        .distinct()
        .sorted(Comparator.reverseOrder())
        .forEach(System.out::println);
  }


  @Test
  void sortedV3() {

    personSupplier.getPersonStream()
        .sorted(Comparator.comparing(Person::age).reversed())
        .forEach(System.out::println);
  }


  @Test
  void peek() {
    personSupplier.getPersonStream()
        .peek(this::toService)
        .forEach(System.out::println);

  }


  private void toService(Person person) {
    System.out.println("Załadowano wiek " + person.age());
  }


  @Test
  void limit() {
    personSupplier.getPersonStream()
        .limit(2)
        .forEach(System.out::println);
  }


  @Test
  void skip() {
    personSupplier.getPersonStream()
        .skip(2)
        .forEach(System.out::println);
  }


  @Test
  void forEach() {
    personSupplier.getPersonStream()
        .sorted(Comparator.comparing(Person::age))
        .peek(person -> System.out.println(person.age() + Thread.currentThread().toString()))
        .forEach(x -> System.out.println(x + Thread.currentThread().toString()));
  }


  @Test
  void collectV1() {
    Set<String> collect = personSupplier.getPersonStream()
        .map(Person::name)
        .collect(Collectors.toSet());
    System.out.println(collect);
    System.out.println(collect.getClass());
  }


  @Test
  void collectV2() {
    List<BigDecimal> collect = personSupplier.getPersonStream()
        .map(Person::money)
        .collect(Collectors.toCollection(LinkedList::new));
    System.out.println(collect);
    System.out.println(collect.getClass());
  }


  @Test
  void collectV3() {

    String string = personSupplier.getPersonStream()
        .map(Person::name)
        .collect(Collectors.joining(" - ", "s: [", "]"));
    System.out.println(string);
    System.out.println(string.getClass());
  }


}
