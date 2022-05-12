package com.example.ex17;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @Column(name = "id") 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  long id; 

    @Column(name = "text")
    private String addressText;

    @Column(name = "zip")
    private String zipCode;

    //@JsonIgnore
    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "bid")
    private Building building;

    public static final class DTO {
        public long id;
        public String addressText;
        public String zipCode;
        public long buildingId;
    }

    public DTO toDTO() {
        DTO x = new DTO();
        x.id = this.id;
        x.addressText = this.addressText;
        x.zipCode = this.zipCode;
        x.buildingId = this.building == null ? 0 : this.building.getId();
        return x;
    }
}
