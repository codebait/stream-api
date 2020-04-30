package com.codebait.stream.model;

import java.math.BigDecimal;
import java.util.List;

public record Person(String name, String surname, int age, BigDecimal money, List<Car>cars) {

  public Person(String name, String surname, int age, BigDecimal money, Car... cars) {
    this(name, surname, age, money, List.of(cars));
  }


}
