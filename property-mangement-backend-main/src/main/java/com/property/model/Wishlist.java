package com.property.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isactive", nullable = false)
    private Integer isActive = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Wishlist() {}

    public Wishlist(Account user, Property property) {
        this.user = user;
        this.property = property;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Account getUser() { return user; }
    public void setUser(Account user) { this.user = user; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
