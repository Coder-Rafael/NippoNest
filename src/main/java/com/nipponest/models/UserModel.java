package com.nipponest.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "phone", length = 15)
    private String phone;

    private String imgAvatar = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZAiOzNMEBAd0Hush3L38Ih9VSBnEQOlgJPAqFn7E0FysGm0YKjwvMXomlFdjfIzIDGXc&usqp=CAU";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductModel> products = new ArrayList<>();

    public UserModel(String name, String login, String senha, String cep, String telefone) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.cep = cep;
        this.phone = telefone;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    // IGNORAR DAQUI PRA BAIXO POR HORA, OBRIGADO A IMPLEMENTAR
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

