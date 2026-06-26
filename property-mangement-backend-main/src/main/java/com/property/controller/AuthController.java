package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Account;
import com.property.repository.AccountRepository;
import com.property.util.JwtUtil;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ApiResponse signUp(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String roleStr = body.get("role");
            String email = body.get("email");
            String phone = body.get("phone");
            String password = body.get("password");
            String cnfrmPwd = body.get("cnfrm_pwd");

            if (name == null || name.trim().isEmpty()) {
                return ApiResponse.requiredParam("Name");
            }
            if (roleStr == null || roleStr.trim().isEmpty()) {
                return ApiResponse.requiredParam("Role");
            }
            int role;
            try {
                role = Integer.parseInt(roleStr);
            } catch (NumberFormatException e) {
                return ApiResponse.invalidInput("Role");
            }

            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.requiredParam("Email");
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || email.contains(" ")) {
                return ApiResponse.invalidInput("Email");
            }

            if (phone == null || phone.trim().isEmpty()) {
                return ApiResponse.requiredParam("Phone no.");
            }
            if (!phone.matches("^\\+\\d{10,15}$") || phone.contains(" ")) {
                return ApiResponse.invalidInput("Phone no.");
            }

            if (password == null || password.trim().isEmpty()) {
                return ApiResponse.requiredParam("Password");
            }
            if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]{6,32}$") || password.contains(" ")) {
                return ApiResponse.invalidInput("Password");
            }

            if (!password.equals(cnfrmPwd)) {
                return ApiResponse.notMatch("Password & Confirm password");
            }

            // Check email already exists
            Optional<Account> emailExists = accountRepository.findByEmailAndIsActive(email, 1);
            if (emailExists.isPresent()) {
                return ApiResponse.alreadyExist("Email");
            }

            // Check phone already exists
            Optional<Account> phoneExists = accountRepository.findByPhoneAndIsActive(phone, 1);
            if (phoneExists.isPresent()) {
                return ApiResponse.alreadyExist("Phone");
            }

            // Create account
            // Role constants: 2 = VENDOR, 3 = CUSTOMER
            int finalRole = (role == 2) ? 2 : 3;
            // Verify status constants: 1 = PENDING, 2 = APPROVED (Vendor is pending, seeker is approved)
            int verifyStatus = (finalRole == 2) ? 1 : 2;

            Account account = new Account(
                    name,
                    finalRole,
                    email,
                    phone,
                    passwordEncoder.encode(password),
                    verifyStatus
            );

            accountRepository.save(account);

            String token = null;
            if (finalRole != 2) {
                token = jwtUtil.generateToken(
                        account.getId(),
                        account.getName(),
                        account.getRole(),
                        account.getEmail(),
                        account.getPhone()
                );
            }

            Map<String, Object> data = new HashMap<>();
            data.put("role", account.getRole());
            data.put("access_token", token != null ? token : "Account need to verify");

            return ApiResponse.success(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PostMapping("/signin")
    public ApiResponse signIn(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");

            if (username == null || username.trim().isEmpty()) {
                return ApiResponse.requiredParam("Email/Phone");
            }
            if (password == null || password.trim().isEmpty()) {
                return ApiResponse.requiredParam("Password");
            }

            Optional<Account> activeUser = accountRepository.findActiveUserByEmailOrPhone(username);
            if (activeUser.isPresent()) {
                Account account = activeUser.get();

                if (account.getVerifyStatus() != 2) { // 2 = APPROVED (VERIFY_STATUS.APPROVED)
                    return ApiResponse.needToVerify();
                }

                if (passwordEncoder.matches(password, account.getPassword())) {
                    String token = jwtUtil.generateToken(
                            account.getId(),
                            account.getName(),
                            account.getRole(),
                            account.getEmail(),
                            account.getPhone()
                    );

                    Map<String, Object> data = new HashMap<>();
                    data.put("role", account.getRole());
                    data.put("access_token", token);

                    return ApiResponse.success(data);
                }
            }

            return ApiResponse.notMatch("Email/Phone and Password");

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/edit")
    public ApiResponse editProfile(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            Optional<Account> optionalAccount = accountRepository.findById(currentUser.getId());
            if (!optionalAccount.isPresent() || optionalAccount.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            Account account = optionalAccount.get();
            String name = body.get("name");
            String email = body.get("email");
            String phone = body.get("phone");

            if (name != null && !name.trim().isEmpty()) {
                account.setName(name);
            }

            if (email != null && !email.trim().isEmpty()) {
                if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || email.contains(" ")) {
                    return ApiResponse.invalidInput("Email");
                }
                account.setEmail(email);
            }

            if (phone != null && !phone.trim().isEmpty()) {
                if (!phone.matches("^\\+\\d{10,15}$") || phone.contains(" ")) {
                    return ApiResponse.invalidInput("Phone no.");
                }
                account.setPhone(phone);
            }

            accountRepository.save(account);
            return ApiResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
