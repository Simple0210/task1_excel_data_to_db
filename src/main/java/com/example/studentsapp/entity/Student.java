package com.example.studentsapp.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String firstname;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column(columnDefinition = "varchar(13) check (length(phone_number)>=7)")
    private String phoneNumber;

    @Column
    private String address;

    private Integer age;

    public Student(String firstname, String lastName, String middleName, String phoneNumber, String address,Integer age) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age=age;
    }
}
