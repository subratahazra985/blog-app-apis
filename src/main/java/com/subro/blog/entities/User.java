package com.subro.blog.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="user_name", nullable = false, length = 100)
    private String name;
    private String email;
    private String password;
    private String about;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts=new ArrayList<>();

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Comment> comments=new ArrayList<>();

   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinTable(name = "user_role",
   joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
   inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id"))
   private Set<Role> roles=new HashSet<>();

    /**
     * {@inheritDoc}
     *
     * <p>This implementation returns a list of {@link SimpleGrantedAuthority} objects, where each
     * authority is the name of the role that the user has. The roles are obtained from the
     * {@link #roles} field.
     *
     * @return a list of {@link SimpleGrantedAuthority} objects
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // iterate each role and change it to granted authority
        List<SimpleGrantedAuthority> authorities = this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authorities;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The username is equal to the user's email address.
     * @return the user's email address
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indicates whether the user's account is non-expired.
     *
     * <p>An expired account cannot be authenticated. This method delegates to the
     * default implementation provided by the {@link UserDetails} interface.
     *
     * @return {@code true} if the user's account is non-expired, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indicates whether the user's account is non-locked.
     *
     * <p>A locked account cannot be authenticated. This method delegates to the
     * default implementation provided by the {@link UserDetails} interface.
     *
     * @return {@code true} if the user's account is non-locked, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indicates whether the user's credentials are non-expired.
     *
     * <p>An expired credential cannot be authenticated. This method delegates to the
     * default implementation provided by the {@link UserDetails} interface.
     *
     * @return {@code true} if the user's credentials are non-expired, {@code false} otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indicates whether the user's account is enabled.
     *
     * <p>An enabled account can be authenticated and used normally within the system.
     * This method always returns {@code true}, indicating that the account is enabled by default.
     *
     * @return {@code true} if the user's account is enabled, {@code false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
