package com.bapos.smartpark.service;

import com.bapos.smartpark.model.ParkingLot;
import com.bapos.smartpark.model.Vehicle;
import com.bapos.smartpark.repository.ParkingLotRepository;
import com.bapos.smartpark.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ParkingService {

    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;

    public ParkingService(ParkingLotRepository parkingLotRepository, VehicleRepository vehicleRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // validate existence and save
    public ParkingLot registerParkingLot(ParkingLot parkingLot) {
        if (parkingLotRepository.existsById(parkingLot.getLotId())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Parking lot already exists");
        }
        parkingLot.setOccupiedSpaces(0);
        return parkingLotRepository.save(parkingLot);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsById(vehicle.getLicensePlate())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Vehicle already exists");
        }
        return vehicleRepository.save(vehicle);
    }

    // ensure vehicle not parked, lot has space, then update both
    @Transactional
    public void checkInVehicle(String lotId, String licensePlate) {
        ParkingLot lot = parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Lot not found"));
        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Vehicle not found"));

        if (vehicle.getCurrentLotId() != null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Vehicle already parked");
        }
        if (lot.getOccupiedSpaces() >= lot.getCapacity()) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Lot is full");
        }

        vehicle.setCurrentLotId(lotId);
        lot.setOccupiedSpaces(lot.getOccupiedSpaces() + 1);

        vehicleRepository.save(vehicle);
        parkingLotRepository.save(lot);
    }

    @Transactional
    public void checkOutVehicle(String lotId, String licensePlate) {
        ParkingLot lot = parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Lot not found"));
        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Vehicle not found"));

        if (!lotId.equals(vehicle.getCurrentLotId())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Vehicle not parked in this lot");
        }

        vehicle.setCurrentLotId(null);
        lot.setOccupiedSpaces(Math.max(0, lot.getOccupiedSpaces() - 1));

        vehicleRepository.save(vehicle);
        parkingLotRepository.save(lot);
    }

    public ParkingLot getParkingLot(String lotId) {
        return parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Lot not found"));
    }

    public List<Vehicle> getVehiclesInLot(String lotId) {
        if (!parkingLotRepository.existsById(lotId)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Lot not found");
        }
        return vehicleRepository.findByCurrentLotId(lotId);
    }
}
