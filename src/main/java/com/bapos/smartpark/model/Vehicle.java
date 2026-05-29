package com.bapos.smartpark.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "license_plate", length = 20, nullable = false, unique = true)
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^[A-Za-z0-9-]+$")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType type;

    @Column(name = "owner_name", nullable = false)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z ]+$")
    private String ownerName;

    protected Vehicle() {
    }

    public Vehicle(String licensePlate, VehicleType type, String ownerName) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.ownerName = ownerName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}