package com.example.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "price")
    private Integer price ;

    @Column(name = "description")
    private Integer description ;

    @Column(name = "is_feature")
    private String isFeature ;

    @Column(name = "status")
    private String status ;

    @Column(name = "sku")
    private String sku ;

    @Column(name = "img_url")
    private String imgUrl ;

    @Column(name = "category_id")
    private String categoryId ;
}
