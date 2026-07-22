package com.example.profile.config.security;

import com.example.profile.modules.user.dto.UserCacheDto;
import com.example.profile.modules.user.entity.User;
import com.example.profile.shared.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final UserCacheDto user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    /**
     * Phương thức kiểm tra tài khoản đã hết hạn chưa?
     * return fasle (không cho đăng nhập)
     * Ví dụ: đối với những tài khoản đùng thử (có thuộc tính thời gian)
     *
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Phương thức kiểm tra tài khoản có bị khóa không?
     * return true (không cho đăng nhập)
     *
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Phương thức kiểm tra mật khẩu còn hiệu lực không?
     *
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Phương thức kiểm tra tài khoản có hoạt động không?
     *
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public String getPublicId() {
        return user.getPublicId();
    }

    public Roles getRole() {
        return user.getRole();
    }

    public int getVersion() {
        return user.getVersion();
    }
}
