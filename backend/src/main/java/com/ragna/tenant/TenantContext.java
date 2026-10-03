package com.ragna.tenant;

public final class TenantContext {

    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static Long tenantId() {
        LoginUser u = HOLDER.get();
        return u == null ? null : u.tenantId();
    }

    public static void clear() {
        HOLDER.remove();
    }

}
