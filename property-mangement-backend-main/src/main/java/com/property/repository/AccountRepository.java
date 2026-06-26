package com.property.repository;

import com.property.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByIsActiveAndRoleOrderByCreatedAtDesc(Integer isActive, Integer role);

    List<Account> findByIsActiveAndRoleAndVerifyStatusOrderByCreatedAtDesc(Integer isActive, Integer role, Integer verifyStatus);

    @Query("SELECT a FROM Account a WHERE a.isActive = 1 AND (a.email = :username OR a.phone = :username)")
    Optional<Account> findActiveUserByEmailOrPhone(@Param("username") String username);

    Optional<Account> findByEmailAndIsActive(String email, Integer isActive);

    Optional<Account> findByPhoneAndIsActive(String phone, Integer isActive);

    long countByIsActiveAndRole(Integer isActive, Integer role);
}
