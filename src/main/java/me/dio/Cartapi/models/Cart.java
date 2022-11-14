package me.dio.Cartapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.Cartapi.enumeration.PaymentMethod;

import javax.persistence.*;
import java.util.List;
@AllArgsConstructor
@Builder
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@NoArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JsonIgnore
    private Client client;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;
    private double totalAmount;

   @Enumerated
    private PaymentMethod paymentMethod;
    private boolean closed;

}
