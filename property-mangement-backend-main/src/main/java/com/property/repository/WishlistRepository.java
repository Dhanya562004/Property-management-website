package com.property.repository;

import com.property.model.Account;
import com.property.model.Property;
import com.property.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserAndIsActiveOrderByIdDesc(Account user, Integer isActive);

    Optional<Wishlist> findByUserAndPropertyAndIsActive(Account user, Property property, Integer isActive);
}
