package com.example.Project.model;

import javax.persistence.*;

@Entity
@Table(name = "order_sets")
public class OrderSet {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    private Order order;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_car")
    private Car car;
    private int amount;


    public OrderSet(Order order, Car car, int amount) {
        this.order = order;
        this.car = car;
        this.amount = amount;
    }

    public OrderSet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
