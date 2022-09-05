package com.example.Project.model;

import javax.persistence.*;


@Entity
@Table(name = "stash_sets")
public class StashSet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_car")
    private Car car;
    private int amount;


    public StashSet(User user, Car car, int amount) {
        this.user = user;
        this.car = car;
        this.amount = amount;
    }

    public StashSet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
