package com.data.websitehotel.controller;


import com.data.websitehotel.exception.PhotoRetrievalException;
import com.data.websitehotel.exception.ResourceNotFoundException;
import com.data.websitehotel.model.BookedRoom;
import com.data.websitehotel.model.Room;
import com.data.websitehotel.response.RoomResponse;
import com.data.websitehotel.service.IBookingRoomService;
import com.data.websitehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private IBookingRoomService bookedRoomService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo,roomType,roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(),
                savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> getRoomType (){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse response = getRoomResponse(room);
                response.setPhoto(base64Photo);
                roomResponses.add(response);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }


    @DeleteMapping("/delete/room/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") Long id){
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable("id") Long id,
                                                  @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

        byte[] photoBytes = photo != null && !photo.isEmpty()?
                photo.getBytes(): roomService.getRoomPhotoByRoomId(id);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0
                ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(id,roomType,roomPrice,photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse response = getRoomResponse(theRoom);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable("id") Long id){
        Optional<Room> theRoom = roomService.getRoomById(id);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                                @RequestParam ("checkOutDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
                                                                @RequestParam ("roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate,checkOutDate,roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : availableRooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length>0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(roomResponses);
        }
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingByRoomId(room.getId());
//        List<BookingResponse> bookingInform = bookings
//                .stream()
//                .map(booking -> new BookingResponse(booking.getId(),
//                        booking.getCheckInDate(),
//                        booking.getCheckOutDate(),booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            }catch(SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(), photoBytes);
    }

    private List<BookedRoom> getAllBookingByRoomId(Long id) {
        return bookedRoomService.getAllBookingsByRoomId(id);
    }
}
