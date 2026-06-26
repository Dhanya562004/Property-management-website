package com.property.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "replycomments")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "replycmt", nullable = false, length = 1000)
    private String replycmt;

    @Column(name = "isactive", nullable = false)
    private Integer isActive = 1;

    @Column(name = "posted_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public Reply() {
        this.postedOn = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReplycmt() { return replycmt; }
    public void setReplycmt(String replycmt) { this.replycmt = replycmt; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Date getPostedOn() { return postedOn; }
    public void setPostedOn(Date postedOn) { this.postedOn = postedOn; }

    public Comment getComment() { return comment; }
    public void setComment(Comment comment) { this.comment = comment; }
}
