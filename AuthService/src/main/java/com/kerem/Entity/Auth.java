package com.kerem.Entity;

import com.kerem.Constant.Role;
import com.kerem.Constant.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblauth")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 20)
    String username;

    @NotNull
    @Size(min = 6, max = 20)
    String password;

    @Email
    @NotNull
    String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Status status = Status.PENDING;

    String activationCode;

}
