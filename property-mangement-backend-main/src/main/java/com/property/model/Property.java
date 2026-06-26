package com.property.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String locality;

    private Integer bedrooms;
    private Integer balconies;
    private Integer bathrooms;

    @Column(length = 2000)
    private String description;

    @Column(name = "posted_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedOn;

    @Column(name = "prop_status", nullable = false)
    private Integer propStatus; // 1=READY_TO_BUY, 2=UPCOMING, 3=SOLD, 4=RENTED

    private Integer lift;
    private Integer parking;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String flooring;

    @Column(nullable = false)
    private Double price;

    private Double deposit;

    @Column(name = "pay_on_month")
    private Integer payOnMonth;

    @Column(name = "isactive", nullable = false)
    private Integer isActive = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subcategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listedby_id", nullable = false)
    private Account listedby;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    public Property() {
        this.postedOn = new Date();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String propertyName) { this.propertyName = propertyName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }

    public Integer getBalconies() { return balconies; }
    public void setBalconies(Integer balconies) { this.balconies = balconies; }

    public Integer getBathrooms() { return bathrooms; }
    public void setBathrooms(Integer bathrooms) { this.bathrooms = bathrooms; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getPostedOn() { return postedOn; }
    public void setPostedOn(Date postedOn) { this.postedOn = postedOn; }

    public Integer getPropStatus() { return propStatus; }
    public void setPropStatus(Integer propStatus) { this.propStatus = propStatus; }

    public Integer getLift() { return lift; }
    public void setLift(Integer lift) { this.lift = lift; }

    public Integer getParking() { return parking; }
    public void setParking(Integer parking) { this.parking = parking; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getFlooring() { return flooring; }
    public void setFlooring(String flooring) { this.flooring = flooring; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getDeposit() { return deposit; }
    public void setDeposit(Double deposit) { this.deposit = deposit; }

    public Integer getPayOnMonth() { return payOnMonth; }
    public void setPayOnMonth(Integer payOnMonth) { this.payOnMonth = payOnMonth; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public SubCategory getSubcategory() { return subcategory; }
    public void setSubcategory(SubCategory subcategory) { this.subcategory = subcategory; }

    public Account getListedby() { return listedby; }
    public void setListedby(Account listedby) { this.listedby = listedby; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
