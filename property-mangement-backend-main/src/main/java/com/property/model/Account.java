package com.property.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer role; // 1 = ADMIN, 2 = VENDOR, 3 = CUSTOMER

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "isactive", nullable = false)
    private Integer isActive = 1;

    @Column(name = "verify_status", nullable = false)
    private Integer verifyStatus = 1; // 1 = PENDING, 2 = APPROVED, 3 = REJECTED (matching constants)

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    public Account() {
        this.createdAt = new Date().getTime();
    }

    public Account(String name, Integer role, String email, String phone, String password, Integer verifyStatus) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.verifyStatus = verifyStatus;
        this.createdAt = new Date().getTime();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Integer getVerifyStatus() { return verifyStatus; }
    public void setVerifyStatus(Integer verifyStatus) { this.verifyStatus = verifyStatus; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
