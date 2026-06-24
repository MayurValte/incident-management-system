package com.eimp.entity;

import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alerts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlertsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assigned_to",
            foreignKey = @ForeignKey(name = "fk_alert_assigned_user")
    )
    private UsersEntity assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "device_id",
            foreignKey = @ForeignKey(name = "fk_alert_device")
    )
    private DevicesEntity device;

    @ManyToMany
    @JoinTable(name = "alert_tags",
            joinColumns = @JoinColumn(name = "alert_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagsEntity> tags = new HashSet<>();
}
