package com.themoment.officialgsm.domain.Admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    private String id;
    private String name;
    private String password;
}
