package aor.demo.crud.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;

@Entity
@TableGenerator(name="product", initialValue=0, allocationSize=50)
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="product")
    public int id;
    public String reference;
    public float width;
    public float height;
    public float price;
    public String thumbnail;
    public String iamge;
    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(length=1000)
    public String description;
    public int stock;
    public boolean published = true;

    @ManyToOne(cascade = {CascadeType.DETACH})
    public Category categoryId;

    public Product() {}

    @JsonCreator
    public Product(int id) {
        this.id = id;
    }
}