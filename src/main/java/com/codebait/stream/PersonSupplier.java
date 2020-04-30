package com.codebait.stream;

import com.codebait.stream.model.Car;
import com.codebait.stream.model.Person;
import java.math.BigDecimal;
import java.util.stream.Stream;

class PersonSupplier {

  public static final Person PERSON_1 = new Person(
      "Przemek", "Zawadzki", 24, BigDecimal.valueOf(1000),
      new Car("Ford", 2001)
  );
  public static final Person PERSON_2 = new Person(
      "Roman", "Kowalski", 30, BigDecimal.valueOf(12000),
      new Car("Fiat", 2003),
      new Car("Mercedes", 2020)
  );
  public static final Person PERSON_3 = new Person(
      "Tomek", "Wasiluk", 14, BigDecimal.valueOf(1200),
      new Car("Ford", 2010)
  );
  public static final Person PERSON_4 = new Person(
      "Władek", "Wasiluk", 14, BigDecimal.valueOf(1000),
      new Car("Opel", 2010)
  );
  public static final Person PERSON_5 = new Person(
      "Monika", "Kowalska", 20, BigDecimal.valueOf(3000),
      new Car("Fiat", 2010),
      new Car("Ferrari", 2019),
      new Car("Tesla", 2018)

  );
  public static final Person PERSON_6 = new Person(
      "Stefan", "Kiepski", 45, BigDecimal.valueOf(7000),
      new Car("Tesla", 2017)
  );



  Stream<Person> getPersonStream() {
    return Stream.of(
        PERSON_1,
        PERSON_2,
        PERSON_3,
        PERSON_4,
        PERSON_5,
        PERSON_6
    );
  }

}
