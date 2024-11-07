package com.backend.eTrade.models.products;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_colors")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color_name", nullable = false, unique = true)
    private String colorName;

    @Column(name = "color_hex_code", unique = true, nullable = false)
    private String colorHexCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "colors")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductImage> images = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductColor)) return false;
        ProductColor productColor = (ProductColor) o;
        return id != null && id.equals(productColor.id);
    }

    @Override
    public int hashCode() {
        return 31; // or return a constant if id is null
    }
}
