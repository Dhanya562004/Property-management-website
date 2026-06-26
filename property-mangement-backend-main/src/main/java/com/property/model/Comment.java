package com.property.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(name = "isactive", nullable = false)
    private Integer isActive = 1;

    @Column(name = "posted_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Comment() {
        this.postedOn = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Date getPostedOn() { return postedOn; }
    public void setPostedOn(Date postedOn) { this.postedOn = postedOn; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
