package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Account;
import com.property.model.Booking;
import com.property.model.Property;
import com.property.repository.AccountRepository;
import com.property.repository.BookingRepository;
import com.property.repository.PropertyRepository;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdminController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/api/admin/vendor/list")
    public ApiResponse listVendors(@RequestParam(value = "status", required = false) Integer status) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 1) { // 1 = ADMIN
                return ApiResponse.noAccess();
            }

            List<Account> vendors;
            if (status != null) {
                vendors = accountRepository.findByIsActiveAndRoleAndVerifyStatusOrderByCreatedAtDesc(1, 2, status); // 2 = VENDOR
            } else {
                vendors = accountRepository.findByIsActiveAndRoleOrderByCreatedAtDesc(1, 2);
            }

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Account v : vendors) {
                Map<String, Object> map = new HashMap<>();
                map.put("vendor_id", v.getId().toString());
                map.put("id", v.getId());
                map.put("name", v.getName());
                map.put("role", v.getRole());
                map.put("email", v.getEmail());
                map.put("phone", v.getPhone());
                map.put("verify_status", v.getVerifyStatus());
                responseList.add(map);
            }

            return ApiResponse.success(responseList);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/api/admin/accept/vendor")
    public ApiResponse acceptVendor(@RequestParam("vendor_id") Long vendorId, @RequestBody Map<String, Object> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 1) { // 1 = ADMIN
                return ApiResponse.noAccess();
            }

            if (vendorId == null) {
                return ApiResponse.requiredParam("vendor id");
            }

            Optional<Account> optVendor = accountRepository.findById(vendorId);
            if (!optVendor.isPresent() || optVendor.get().getIsActive() != 1 || optVendor.get().getRole() != 2) {
                return ApiResponse.invalidInput("Vendor not found");
            }

            Account vendor = optVendor.get();

            if (body.containsKey("verify_status")) {
                Integer verifyStatus = Integer.parseInt(body.get("verify_status").toString());
                if (verifyStatus == 2 || verifyStatus == 3) { // 2 = APPROVED, 3 = REJECTED
                    vendor.setVerifyStatus(verifyStatus);
                    accountRepository.save(vendor);
                }
            }

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/api/project/analysis")
    public ApiResponse getBookingsAnalysis() {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 1) { // 1 = ADMIN
                return ApiResponse.noAccess();
            }

            List<Property> properties = propertyRepository.findByIsActiveOrderByPostedOnDesc(1);
            int[] counts = new int[12]; // index 0-11 for 12 months

            for (Property p : properties) {
                List<Booking> bookings = bookingRepository.findByPropertyAndIsActive(p, 1);
                for (Booking b : bookings) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(b.getBookedOn());
                    int month = cal.get(Calendar.MONTH); // 0-11
                    counts[month] += 1;
                }
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookingsData", counts);

            return ApiResponse.success(responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/api/project/analysis/count")
    public ApiResponse getCountsAnalysis() {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 1) { // 1 = ADMIN
                return ApiResponse.noAccess();
            }

            long userCount = accountRepository.countByIsActiveAndRole(1, 3); // 3 = CUSTOMER
            long vendorCount = accountRepository.countByIsActiveAndRole(1, 2); // 2 = VENDOR
            long propertyCount = propertyRepository.countByIsActive(1);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("totalUser", userCount);
            responseData.put("totalVendor", vendorCount);
            responseData.put("totalProperty", propertyCount);

            return ApiResponse.success(responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
