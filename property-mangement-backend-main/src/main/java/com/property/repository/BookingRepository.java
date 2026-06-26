package com.property.repository;

import com.property.model.Booking;
import com.property.model.Property;
import com.property.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByIsActiveAndUserOrderByBookedOnDesc(Integer isActive, Account user);

    List<Booking> findByIsActiveAndVendorOrderByBookedOnDesc(Integer isActive, Account vendor);

    Optional<Booking> findByPropertyAndUserAndIsActive(Property property, Account user, Integer isActive);

    List<Booking> findByPropertyAndIsActive(Property property, Integer isActive);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.property = :property AND b.isActive = 1")
    long countActiveBookingsByProperty(@Param("property") Property property);
}
