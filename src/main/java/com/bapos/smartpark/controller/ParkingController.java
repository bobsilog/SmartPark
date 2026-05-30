package com.bapos.smartpark.controller;

import com.bapos.smartpark.model.ParkingLot;
import com.bapos.smartpark.model.Vehicle;
import com.bapos.smartpark.model.VehicleType;
import com.bapos.smartpark.service.ParkingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping(path = "/parking-lots", consumes = "application/json")
    public ResponseEntity<ParkingLot> registerParkingLot(@Valid @RequestBody ParkingLotRequest request) {
        ParkingLot saved = parkingService.registerParkingLot(new ParkingLot(request.lotId(), request.location(), request.capacity(), 0));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping(path = "/vehicles", consumes = "application/json")
    public ResponseEntity<Vehicle> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle saved = parkingService.registerVehicle(new Vehicle(request.licensePlate(), request.type(), request.ownerName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping(path = "/parking-lots/{lotId}/checkin", consumes = "application/json")
    public ResponseEntity<Void> checkInVehicle(@PathVariable String lotId, @Valid @RequestBody ParkingActionRequest request) {
        parkingService.checkInVehicle(lotId, request.licensePlate());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/parking-lots/{lotId}/checkout", consumes = "application/json")
    public ResponseEntity<Void> checkOutVehicle(@PathVariable String lotId, @Valid @RequestBody ParkingActionRequest request) {
        parkingService.checkOutVehicle(lotId, request.licensePlate());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/parking-lots/{lotId}/occupancy")
    public ResponseEntity<ParkingLot> getOccupancy(@PathVariable String lotId) {
        return ResponseEntity.ok(parkingService.getParkingLot(lotId));
    }

    @GetMapping("/parking-lots/{lotId}/vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles(@PathVariable String lotId) {
        return ResponseEntity.ok(parkingService.getVehiclesInLot(lotId));
    }

    public record ParkingLotRequest(
            @NotBlank @Size(max = 50) String lotId,
            @NotBlank String location,
            @NotNull @Min(0) Integer capacity
    ) {
    }

    public record VehicleRequest(
            @NotBlank @Size(max = 20) @Pattern(regexp = "^[A-Za-z0-9-]+$") String licensePlate,
            @NotNull VehicleType type,
            @NotBlank @Pattern(regexp = "^[A-Za-z ]+$") String ownerName
    ) {
    }

    public record ParkingActionRequest(
            @NotBlank @Size(max = 20) @Pattern(regexp = "^[A-Za-z0-9-]+$") String licensePlate
    ) {
    }
}
