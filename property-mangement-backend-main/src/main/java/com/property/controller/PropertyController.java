package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Account;
import com.property.model.Category;
import com.property.model.Property;
import com.property.model.SubCategory;
import com.property.repository.AccountRepository;
import com.property.repository.CategoryRepository;
import com.property.repository.PropertyRepository;
import com.property.repository.SubCategoryRepository;
import com.property.repository.WishlistRepository;
import com.property.service.FileStorageService;
import com.property.service.MailService;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MailService mailService;

    @Value("${server.port:9291}")
    private String serverPort;

    private String getBaseUrl() {
        return "http://localhost:" + serverPort + "/uploads/";
    }

    private Map<String, Object> mapPropertyToDto(Property p) {
        Map<String, Object> map = new HashMap<>();
        map.put("_id", p.getId().toString());
        map.put("id", p.getId());
        map.put("property_name", p.getPropertyName());
        map.put("city", p.getCity());
        map.put("locality", p.getLocality());
        map.put("bedrooms", p.getBedrooms());
        map.put("balconies", p.getBalconies());
        map.put("bathrooms", p.getBathrooms());
        map.put("description", p.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        map.put("posted_on", sdf.format(p.getPostedOn()));
        map.put("prop_status", p.getPropStatus());
        map.put("lift", p.getLift());
        map.put("parking", p.getParking());
        map.put("age", p.getAge());
        map.put("flooring", p.getFlooring());
        map.put("price", p.getPrice());
        map.put("deposit", p.getDeposit());
        map.put("pay_on_month", p.getPayOnMonth());

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
        map.put("images", imageUrls);

        if (p.getCategory() != null) {
            Map<String, Object> catMap = new HashMap<>();
            catMap.put("_id", p.getCategory().getId().toString());
            catMap.put("id", p.getCategory().getId());
            catMap.put("name", p.getCategory().getName());
            map.put("categoryInfo", catMap);
        }

        if (p.getSubcategory() != null) {
            Map<String, Object> subMap = new HashMap<>();
            subMap.put("_id", p.getSubcategory().getId().toString());
            subMap.put("id", p.getSubcategory().getId());
            subMap.put("name", p.getSubcategory().getName());
            map.put("subcategoryInfo", subMap);
        }

        if (p.getListedby() != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("_id", p.getListedby().getId().toString());
            userMap.put("id", p.getListedby().getId());
            userMap.put("name", p.getListedby().getName());
            userMap.put("email", p.getListedby().getEmail());
            userMap.put("phone", p.getListedby().getPhone());
            map.put("listedby", userMap);
        }

        return map;
    }

    @PostMapping(value = "/manage/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse postProperty(
            @RequestParam("property_name") String propertyName,
            @RequestParam("city") String city,
            @RequestParam("locality") String locality,
            @RequestParam("description") String description,
            @RequestParam("prop_status") Integer propStatus,
            @RequestParam("age") String age,
            @RequestParam("flooring") String flooring,
            @RequestParam("price") Double price,
            @RequestParam("category") Long categoryId,
            @RequestParam("subcategory") Long subcategoryId,
            @RequestParam(value = "bedrooms", required = false) Integer bedrooms,
            @RequestParam(value = "balconies", required = false) Integer balconies,
            @RequestParam(value = "bathrooms", required = false) Integer bathrooms,
            @RequestParam(value = "lift", required = false) Integer lift,
            @RequestParam(value = "parking", required = false) Integer parking,
            @RequestParam(value = "deposit", required = false) Double deposit,
            @RequestParam(value = "pay_on_month", required = false) Integer payOnMonth,
            @RequestParam("images") MultipartFile[] images
    ) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 2) { // 2 = VENDOR
                return ApiResponse.noAccess();
            }

            if (propertyName == null || propertyName.trim().isEmpty()) return ApiResponse.requiredParam("Property name");
            if (city == null || city.trim().isEmpty()) return ApiResponse.requiredParam("City");
            if (locality == null || locality.trim().isEmpty()) return ApiResponse.requiredParam("Locality");
            if (description == null || description.trim().isEmpty()) return ApiResponse.requiredParam("Description");
            if (propStatus == null) return ApiResponse.requiredParam("prop_status");
            if (age == null || age.trim().isEmpty()) return ApiResponse.requiredParam("Age");
            if (flooring == null || flooring.trim().isEmpty()) return ApiResponse.requiredParam("Flooring");
            if (price == null) return ApiResponse.requiredParam("Price");
            if (categoryId == null) return ApiResponse.requiredParam("Category");
            if (subcategoryId == null) return ApiResponse.requiredParam("Subcategory");

            Optional<Category> optCat = categoryRepository.findById(categoryId);
            Optional<SubCategory> optSub = subCategoryRepository.findById(subcategoryId);

            if (!optCat.isPresent() || !optSub.isPresent() || !optSub.get().getCategory().getId().equals(categoryId)) {
                return ApiResponse.notMatch("Category and Subcategory");
            }

            if (images == null || images.length == 0) {
                return ApiResponse.requiredParam("Property Image");
            }

            List<String> imageFilenames = new ArrayList<>();
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String filename = fileStorageService.storeFile(file);
                    imageFilenames.add(filename);
                }
            }

            Optional<Account> listedBy = accountRepository.findById(currentUser.getId());
            if (!listedBy.isPresent()) {
                return ApiResponse.unknownError();
            }

            Property property = new Property();
            property.setPropertyName(propertyName);
            property.setCity(city);
            property.setLocality(locality);
            property.setDescription(description);
            property.setPropStatus(propStatus);
            property.setAge(age);
            property.setFlooring(flooring);
            property.setPrice(price);
            property.setCategory(optCat.get());
            property.setSubcategory(optSub.get());
            property.setBedrooms(bedrooms != null ? bedrooms : 0);
            property.setBalconies(balconies != null ? balconies : 0);
            property.setBathrooms(bathrooms != null ? bathrooms : 0);
            property.setLift(lift != null ? lift : 0);
            property.setParking(parking != null ? parking : 0);
            property.setDeposit(deposit != null ? deposit : 0.0);
            property.setPayOnMonth(payOnMonth != null ? payOnMonth : 0);
            property.setListedby(listedBy.get());
            property.setImages(imageFilenames);

            propertyRepository.save(property);
            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/manage/list")
    public ApiResponse listProperties(
            @RequestParam(value = "vendor_id", required = false) Long vendorId,
            @RequestParam(value = "prop_status", required = false) Integer propStatus
    ) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            Long targetVendorId = vendorId;
            if (currentUser.getRole() == 2) { // VENDOR
                targetVendorId = currentUser.getId();
            }

            if (targetVendorId == null) {
                return ApiResponse.requiredParam("Vendor_id");
            }

            List<Property> properties = propertyRepository.findByIsActiveAndListedbyIdOrderByPostedOnDesc(1, targetVendorId);
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Property p : properties) {
                if (propStatus != null && !p.getPropStatus().equals(propStatus)) {
                    continue;
                }
                responseList.add(mapPropertyToDto(p));
            }

            return ApiResponse.success(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @DeleteMapping("/manage/delete/{id}")
    public ApiResponse deleteProperty(@PathVariable("id") Long id) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            Optional<Property> optProp = propertyRepository.findById(id);
            if (optProp.isPresent()) {
                Property p = optProp.get();
                if (currentUser.getRole() == 2 && !p.getListedby().getId().equals(currentUser.getId())) {
                    return ApiResponse.noAccess();
                }
                p.setIsActive(0);
                propertyRepository.save(p);
            }
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/manage/list/particular")
    public ApiResponse getPropertyById(@RequestParam("property_id") Long propertyId) {
        try {
            Optional<Property> optProp = propertyRepository.findById(propertyId);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.success(new ArrayList<>());
            }

            List<Map<String, Object>> result = new ArrayList<>();
            result.add(mapPropertyToDto(optProp.get()));

            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/manage/edit/{id}")
    public ApiResponse editProperty(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }
            if (currentUser.getRole() != 2) {
                return ApiResponse.noAccess();
            }

            Optional<Property> optProp = propertyRepository.findById(id);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            Property p = optProp.get();
            if (!p.getListedby().getId().equals(currentUser.getId())) {
                return ApiResponse.noAccess();
            }

            if (body.containsKey("property_name")) p.setPropertyName((String) body.get("property_name"));
            if (body.containsKey("city")) p.setCity((String) body.get("city"));
            if (body.containsKey("locality")) p.setLocality((String) body.get("locality"));
            if (body.containsKey("description")) p.setDescription((String) body.get("description"));
            if (body.containsKey("age")) p.setAge((String) body.get("age"));
            if (body.containsKey("flooring")) p.setFlooring((String) body.get("flooring"));
            if (body.containsKey("price")) p.setPrice(Double.valueOf(body.get("price").toString()));

            propertyRepository.save(p);
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/manage/edit/status/{id}")
    public ApiResponse changePropertyStatus(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }
            if (currentUser.getRole() != 2) {
                return ApiResponse.noAccess();
            }

            Optional<Property> optProp = propertyRepository.findById(id);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            Property p = optProp.get();
            if (!p.getListedby().getId().equals(currentUser.getId())) {
                return ApiResponse.noAccess();
            }

            if (body.containsKey("prop_status")) {
                Integer newStatus = Integer.parseInt(body.get("prop_status").toString());
                p.setPropStatus(newStatus);
                propertyRepository.save(p);

                // If status becomes READY_TO_BUY (1), notify wishlist users
                if (newStatus == 1) {
                    // Find users who have this property wishlisted
                    // We will query wishlistRepository and send email
                    // Node code collects emails and sends
                    List<com.property.model.Wishlist> wishlists = wishlistRepository.findByUserAndIsActiveOrderByIdDesc(null, 1);
                    // Filter manually because we want all wishlists for this property
                    for (com.property.model.Wishlist w : wishlistRepository.findAll()) {
                        if (w.getProperty().getId().equals(p.getId()) && w.getIsActive() == 1) {
                            String email = w.getUser().getEmail();
                            String subject = "Dream Property is ready to Buy \uD83C\uDFE1";
                            String text = "Hurry Up!, Your wishlisted property " + p.getPropertyName() + " is now ready to book";
                            mailService.sendMail(email, subject, text);
                        }
                    }
                }
            }

            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/seach/filter") // matches Node.js typo /seach/filter
    public ApiResponse searchAndFilter(
            @RequestParam(value = "property_name", required = false) String propertyName,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "locality", required = false) String locality
    ) {
        try {
            String pName = (propertyName == null || propertyName.trim().isEmpty()) ? null : propertyName;
            String cName = (city == null || city.trim().isEmpty()) ? null : city;
            String lName = (locality == null || locality.trim().isEmpty()) ? null : locality;

            List<Property> properties = propertyRepository.searchProperties(pName, cName, lName);
            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Property p : properties) {
                responseList.add(mapPropertyToDto(p));
            }
            return ApiResponse.success(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/list/bycat")
    public ApiResponse listByCategory(
            @RequestParam("cattegory_id") Long categoryId,
            @RequestParam("subcat_id") Long subcategoryId,
            @RequestParam(value = "prop_status", required = false) Integer propStatus
    ) {
        try {
            Optional<Category> optCat = categoryRepository.findById(categoryId);
            Optional<SubCategory> optSub = subCategoryRepository.findById(subcategoryId);

            if (!optCat.isPresent() || !optSub.isPresent()) {
                return ApiResponse.requiredParam("Category or Subcategory not found");
            }

            List<Property> properties = propertyRepository.findByIsActiveAndCategoryAndSubcategoryOrderByPostedOnDesc(1, optCat.get(), optSub.get());
            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Property p : properties) {
                if (propStatus != null && !p.getPropStatus().equals(propStatus)) {
                    continue;
                }
                responseList.add(mapPropertyToDto(p));
            }
            return ApiResponse.success(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/user/list")
    public ApiResponse propertyListUser(@RequestParam("properties") Integer propertiesType) {
        try {
            if (propertiesType == null || propertiesType < 1 || propertiesType > 3) {
                return ApiResponse.invalidInput("properties");
            }

            List<Property> properties;
            if (propertiesType == 1) {
                properties = propertyRepository.findByIsActiveAndPropStatusOrderByPostedOnDesc(1, 1); // 1 = READY_TO_BUY
            } else if (propertiesType == 2) {
                properties = propertyRepository.findByIsActiveAndPropStatusOrderByPostedOnDesc(1, 2); // 2 = UPCOMMING
            } else {
                properties = propertyRepository.findRandomProperties(); // 3 = Recommended random 8
            }

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Property p : properties) {
                Map<String, Object> dto = new HashMap<>();
                dto.put("_id", p.getId().toString());
                dto.put("id", p.getId());
                dto.put("property_name", p.getPropertyName());
                dto.put("city", p.getCity());
                dto.put("locality", p.getLocality());
                dto.put("description", p.getDescription());
                dto.put("prop_status", p.getPropStatus());
                dto.put("price", p.getPrice());
                dto.put("deposit", p.getDeposit());

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
                dto.put("images", imageUrls);
                responseList.add(dto);
            }

            return ApiResponse.success(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
