package com.data.websitehotel.service;

import com.data.websitehotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException, SQLException;
    List<String> getAllRoomTypes();
    List<Room> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long id) throws SQLException;

    void deleteRoom(Long id);

    Room updateRoom(Long id, String roomType, BigDecimal roomPrice, byte[] photoBytes);

    Optional<Room> getRoomById(Long id);

    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
