package com.bapos.smartpark.service;

import com.bapos.smartpark.model.ParkingLot;
import com.bapos.smartpark.model.Vehicle;
import com.bapos.smartpark.model.VehicleType;
import com.bapos.smartpark.repository.ParkingLotRepository;
import com.bapos.smartpark.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingServiceTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ParkingService parkingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterParkingLot() {
        ParkingLot lot = new ParkingLot("LOT-001", "Pasig", 120, 0);
        when(parkingLotRepository.existsById("LOT-001")).thenReturn(false);
        when(parkingLotRepository.save(lot)).thenReturn(lot);

        ParkingLot result = parkingService.registerParkingLot(lot);

        assertNotNull(result);
        assertEquals("LOT-001", result.getLotId());
        verify(parkingLotRepository).save(lot);
    }

    @Test
    void testRegisterVehicle() {
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, "Bruce Apos");
        when(vehicleRepository.existsById("ABC-1234")).thenReturn(false);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle result = parkingService.registerVehicle(vehicle);

        assertNotNull(result);
        assertEquals("ABC-1234", result.getLicensePlate());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testCheckInVehicle() {
        ParkingLot lot = new ParkingLot("LOT-001", "Pasig", 120, 78);
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, "Bruce Apos");
        vehicle.setCurrentLotId(null);

        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(vehicle));

        parkingService.checkInVehicle("LOT-001", "ABC-1234");

        assertEquals("LOT-001", vehicle.getCurrentLotId());
        assertEquals(79, lot.getOccupiedSpaces());
    }

    @Test
    void testCheckInVehicle_LotFull() {
        ParkingLot lot = new ParkingLot("LOT-001", "Pasig", 120, 120);
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, "Bruce Apos");
        vehicle.setCurrentLotId(null);

        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(vehicle));

        assertThrows(ResponseStatusException.class,
                () -> parkingService.checkInVehicle("LOT-001", "ABC-1234"));
    }

    @Test
    void testCheckInVehicle_AlreadyParked() {
        ParkingLot lot = new ParkingLot("LOT-001", "Pasig", 120, 78);
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, "Bruce Apos");
        vehicle.setCurrentLotId("LOT-002");

        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(vehicle));

        assertThrows(ResponseStatusException.class,
                () -> parkingService.checkInVehicle("LOT-001", "ABC-1234"));
    }

    @Test
    void testCheckOutVehicle() {
        ParkingLot lot = new ParkingLot("LOT-001", "Pasig", 120, 78);
        Vehicle vehicle = new Vehicle("ABC-1234", VehicleType.CAR, "Bruce Apos");
        vehicle.setCurrentLotId("LOT-001");

        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(vehicle));

        parkingService.checkOutVehicle("LOT-001", "ABC-1234");

        assertNull(vehicle.getCurrentLotId());
        assertEquals(77, lot.getOccupiedSpaces());
    }

    @Test
    void testGetParkingLot() {
        ParkingLot lot = new ParkingLot("LOT-002", "Taguig", 80, 49);
        when(parkingLotRepository.findById("LOT-002")).thenReturn(Optional.of(lot));

        ParkingLot result = parkingService.getParkingLot("LOT-002");

        assertNotNull(result);
        assertEquals("LOT-002", result.getLotId());
    }
}
