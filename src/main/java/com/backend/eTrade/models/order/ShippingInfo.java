package com.backend.eTrade.models.order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipping_info")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "st1")
    private String street1;

    @Column(name = "st2")
    private String street2;

    @Column(name = "city")
    private String city;

    @Column(name = "cap")
    private String cap;

    @Column(name = "country")
    private String country;

    @Column(name = "payment_method")
    @JsonIgnore
    private String paymentMethod;

    @Column(name = "payment_id")
    @JsonIgnore
    private String paymentId;
}
