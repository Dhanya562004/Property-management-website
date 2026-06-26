package com.property.util;

public class UserContext {

    private static final ThreadLocal<UserClaims> threadLocal = new ThreadLocal<>();

    public static void setClaims(UserClaims claims) {
        threadLocal.set(claims);
    }

    public static UserClaims getClaims() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }

    public static class UserClaims {
        private Long id;
        private String name;
        private Integer role;
        private String email;
        private String phone;

        public UserClaims(Long id, String name, Integer role, String email, String phone) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.email = email;
            this.phone = phone;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public Integer getRole() { return role; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }
}
