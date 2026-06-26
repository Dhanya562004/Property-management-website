package com.property.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Account vendor;

    @Column(name = "booked_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookedOn;

    @Column(name = "isaccepted", nullable = false)
    private Integer isAccepted = 0; // 0 = PENDING, 1 = ACCEPTED, 2 = REJECTED (matching constants)

    public Booking() {
        this.bookedOn = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Account getUser() { return user; }
    public void setUser(Account user) { this.user = user; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }

    public Account getVendor() { return vendor; }
    public void setVendor(Account vendor) { this.vendor = vendor; }

    public Date getBookedOn() { return bookedOn; }
    public void setBookedOn(Date bookedOn) { this.bookedOn = bookedOn; }

    public Integer getIsAccepted() { return isAccepted; }
    public void setIsAccepted(Integer isAccepted) { this.isAccepted = isAccepted; }
}
