package ru.zeet.task2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentid;

    @Column(name = "name")
    private String text;

    @Column(name = "haschildren")
    private boolean children;

   /* public Integer getIntParent() {
        if (parentId.equals("#"))
            return null;
        else
            return parentId;// Integer.parseInt(parentId);
    }*/
}