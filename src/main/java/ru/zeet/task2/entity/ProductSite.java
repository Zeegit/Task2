package ru.zeet.task2.entity;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ProductSite {
    private String id;
    private String parentid;
    private String text;
    private String children;
}
