package com.springcloud.EurekaClientTest.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    private String name;

    private String passwd;

    @Column(nullable = false, unique = true)
    private String userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String encryptedPwd;

}
