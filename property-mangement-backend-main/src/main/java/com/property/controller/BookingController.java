package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Account;
import com.property.model.Booking;
import com.property.model.Property;
import com.property.repository.AccountRepository;
import com.property.repository.BookingRepository;
import com.property.repository.PropertyRepository;
import com.property.service.MailService;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/property/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MailService mailService;

    @Value("${server.port:9291}")
    private String serverPort;

    private String getBaseUrl() {
        return "http://localhost:" + serverPort + "/uploads/";
    }

    @PostMapping("/add")
    public ApiResponse addBooking(@RequestBody Map<String, String> body) {
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

            Long propertyId;
            try {
                propertyId = Long.parseLong(propertyIdStr);
            } catch (NumberFormatException e) {
                return ApiResponse.invalidInput("Property id");
            }

            Optional<Property> optProp = propertyRepository.findById(propertyId);
            if (!optProp.isPresent() || optProp.get().getIsActive() != 1) {
                return ApiResponse.invalidInput("Property not found");
            }

            Property property = optProp.get();
            Optional<Account> optUser = accountRepository.findById(currentUser.getId());
            if (!optUser.isPresent()) {
                return ApiResponse.unknownError();
            }

            Account user = optUser.get();

            // Check if already booked
            Optional<Booking> existingBooking = bookingRepository.findByPropertyAndUserAndIsActive(property, user, 1);
            if (existingBooking.isPresent()) {
                return ApiResponse.alreadyExist("Bookings");
            }

            // Check property status (Only READY_TO_BUY (1) can be booked)
            if (!property.getPropStatus().equals(1)) {
                return ApiResponse.readyToBookOnly();
            }

            // Create booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setProperty(property);
            booking.setVendor(property.getListedby());
            booking.setIsAccepted(0); // 0 = Pending/Not Accepted
            bookingRepository.save(booking);

            // Send notification mail to Vendor
            String vendorEmail = property.getListedby().getEmail();
            String subject = "Property is Booked! \uD83C\uDFE1";
            String text = currentUser.getName() + " has Booked Your property " + property.getPropertyName() + ", Confirm in the Dashboard";
            mailService.sendMail(vendorEmail, subject, text);

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @GetMapping("/list")
    public ApiResponse listBookings() {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() == 1) { // ADMIN has no access to client bookings
                return ApiResponse.noAccess();
            }

            Optional<Account> optAccount = accountRepository.findById(currentUser.getId());
            if (!optAccount.isPresent()) {
                return ApiResponse.unknownError();
            }

            Account account = optAccount.get();
            List<Booking> bookings;

            if (currentUser.getRole() == 2) { // VENDOR
                bookings = bookingRepository.findByIsActiveAndVendorOrderByBookedOnDesc(1, account);
            } else { // CUSTOMER
                bookings = bookingRepository.findByIsActiveAndUserOrderByBookedOnDesc(1, account);
            }

            List<Map<String, Object>> responseList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

            for (Booking b : bookings) {
                Map<String, Object> bMap = new HashMap<>();
                bMap.put("bookin_id", b.getId().toString());
                bMap.put("id", b.getId());
                bMap.put("isactive", b.getIsActive());
                bMap.put("booked_on", sdf.format(b.getBookedOn()));
                bMap.put("isaccepted", b.getIsAccepted());

                Property p = b.getProperty();
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("property_id", p.getId().toString());
                pMap.put("id", p.getId());
                pMap.put("property_name", p.getPropertyName());
                pMap.put("city", p.getCity());
                pMap.put("locality", p.getLocality());
                pMap.put("description", p.getDescription());
                pMap.put("prop_status", p.getPropStatus());
                pMap.put("price", p.getPrice());
                pMap.put("deposit", p.getDeposit());

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
                bMap.put("propertyInfo", pMap);

                Account relativeAccount = (currentUser.getRole() == 2) ? b.getUser() : b.getVendor();
                Map<String, Object> accMap = new HashMap<>();
                accMap.put("account_id", relativeAccount.getId().toString());
                accMap.put("id", relativeAccount.getId());
                accMap.put("name", relativeAccount.getName());
                accMap.put("role", relativeAccount.getRole());
                accMap.put("email", relativeAccount.getEmail());
                accMap.put("phone", relativeAccount.getPhone());
                bMap.put("accountInfo", accMap);

                responseList.add(bMap);
            }

            return ApiResponse.success(responseList);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/accept/{id}")
    public ApiResponse acceptBooking(@PathVariable("id") Long id) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            if (currentUser.getRole() != 2) { // 2 = VENDOR
                return ApiResponse.noAccess();
            }

            Optional<Booking> optBooking = bookingRepository.findById(id);
            if (!optBooking.isPresent() || optBooking.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            Booking booking = optBooking.get();
            if (!booking.getVendor().getId().equals(currentUser.getId())) {
                return ApiResponse.noAccess();
            }

            booking.setIsAccepted(1); // 1 = ACCEPTED
            bookingRepository.save(booking);

            // Send notification mail to Customer
            String customerEmail = booking.getUser().getEmail();
            String subject = "Your Booking is Accepted \uD83E\uDD73";
            String text = "Congrats! Your dream Property " + booking.getProperty().getPropertyName() + ", worth \u20B9 " + booking.getProperty().getPrice() + ", is now yours.";
            mailService.sendMail(customerEmail, subject, text);

            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
