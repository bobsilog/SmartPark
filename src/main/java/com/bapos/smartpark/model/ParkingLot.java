package com.bapos.smartpark.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @Column(name = "lot_id", length = 50, nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String lotId;

    @Column(nullable = false)
    @NotBlank
    private String location;

    @Column(nullable = false)
    @Min(0)
    private int capacity;

    @Column(name = "occupied_spaces", nullable = false)
    @Min(0)
    private int occupiedSpaces;

    protected ParkingLot() {
    }

    public ParkingLot(String lotId, String location, int capacity, int occupiedSpaces) {
        this.lotId = lotId;
        this.location = location;
        this.capacity = capacity;
        this.occupiedSpaces = occupiedSpaces;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupiedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }
}