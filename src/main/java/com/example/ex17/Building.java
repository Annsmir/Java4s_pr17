package com.example.ex17;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Building")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Building {
    // private static long nextId = 1;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "created")
    private String creationDate;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "building")
    private List<Address> addresses;
}
