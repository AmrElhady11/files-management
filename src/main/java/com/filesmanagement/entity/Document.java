package com.filesmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="Document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String ContentType;
    @Column
    private int userId;
    @Column
    private LocalDateTime creationDate;
    @Column
    private String objectKey;
    @Column
    private String bucketName;
}
