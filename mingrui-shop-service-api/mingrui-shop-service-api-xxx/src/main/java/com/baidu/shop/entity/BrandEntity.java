package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_brand")
@Data
public class BrandEntity {

    @Id
    private Integer id;

    private String name;

    private String image;

    private Character letter;
}
