package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Account;
import com.property.model.Property;
import com.property.model.Wishlist;
import com.property.repository.AccountRepository;
import com.property.repository.PropertyRepository;
import com.property.repository.WishlistRepository;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${server.port:9291}")
    private String serverPort;

    private String getBaseUrl() {
        return "http://localhost:" + serverPort + "/uploads/";
    }

    @PostMapping("/wishlist/add")
    public ApiResponse addToWishlist(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 3) { // 3 = CUSTOMER
                return ApiResponse.noAccess();
            }

            String propertyIdStr = body.get("property_id");
            if (propertyIdStr == null || propertyIdStr.trim().isEmpty()) {
                return ApiResponse.requiredParam("Property id");
            }

            Long propertyId = Long.parseLong(propertyIdStr);
            Optional<Property> optProp = propertyRepository.findById(propertyId);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.invalidInput("Property not found");
            }

            Optional<Account> optUser = accountRepository.findById(currentUser.getId());
            if (!optUser.isPresent()) {
                return ApiResponse.unknownError();
            }

            Account user = optUser.get();
            Property property = optProp.get();

            // Check if already exists in wishlist
            Optional<Wishlist> existing = wishlistRepository.findByUserAndPropertyAndIsActive(user, property, 1);
            if (existing.isPresent()) {
                return ApiResponse.alreadyExist("Wishlist");
            }

            Wishlist wishlist = new Wishlist(user, property);
            wishlistRepository.save(wishlist);

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/my/wishlists")
    public ApiResponse getMyWishlist() {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 3) {
                return ApiResponse.noAccess();
            }

            Optional<Account> optUser = accountRepository.findById(currentUser.getId());
            if (!optUser.isPresent()) {
                return ApiResponse.unknownError();
            }

            List<Wishlist> wishlists = wishlistRepository.findByUserAndIsActiveOrderByIdDesc(optUser.get(), 1);
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Wishlist w : wishlists) {
                Map<String, Object> wMap = new HashMap<>();
                wMap.put("_id", w.getId().toString());
                wMap.put("id", w.getId());
                wMap.put("isactive", w.getIsActive());

                Property p = w.getProperty();
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("_id", p.getId().toString());
                pMap.put("id", p.getId());
                pMap.put("property_name", p.getPropertyName());
                pMap.put("city", p.getCity());
                pMap.put("locality", p.getLocality());

                List<String> imageUrls = new ArrayList<>();
                if (p.getImages() != null) {
                    for (String img : p.getImages()) {
                        if (img.startsWith("http")) {
                            imageUrls.add(img);
                        } else {
                            imageUrls.add(getBaseUrl() + img);
                        }
                    }
                }
                pMap.put("images", imageUrls);
                wMap.put("propertyInfo", pMap);

                responseList.add(wMap);
            }

            return ApiResponse.success(responseList);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @DeleteMapping("/wishlist/remove/{id}")
    public ApiResponse removeFromWishlist(@PathVariable("id") Long id) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 3) {
                return ApiResponse.noAccess();
            }

            Optional<Wishlist> optWishlist = wishlistRepository.findById(id);
            if (optWishlist.isPresent()) {
                Wishlist w = optWishlist.get();
                if (!w.getUser().getId().equals(currentUser.getId())) {
                    return ApiResponse.noAccess();
                }
                w.setIsActive(0);
                wishlistRepository.save(w);
            }

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
