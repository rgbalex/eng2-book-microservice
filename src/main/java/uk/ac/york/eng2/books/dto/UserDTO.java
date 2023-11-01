package uk.ac.york.eng2.books.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserDTO {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
