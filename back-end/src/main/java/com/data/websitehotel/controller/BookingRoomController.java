package com.data.websitehotel.controller;

import com.data.websitehotel.exception.InvalidBookingRequestException;
import com.data.websitehotel.exception.ResourceNotFoundException;
import com.data.websitehotel.model.BookedRoom;
import com.data.websitehotel.model.Room;
import com.data.websitehotel.response.BookingResponse;
import com.data.websitehotel.response.RoomResponse;
import com.data.websitehotel.service.IBookingRoomService;
import com.data.websitehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingRoomController {
    @Autowired
    private IBookingRoomService bookingService;

    @Autowired
    private IRoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBooking(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable("confirmationCode") String confirmationCode){
        try{
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse  bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @PostMapping("/room/{id}/booking")
    public ResponseEntity<?>saveBooking(@PathVariable("id") Long id,
                                        @RequestBody BookedRoom bookingRequest){
        try{
            String confirmationCode = bookingService.saveBooking(id,bookingRequest);
            return ResponseEntity.ok(
                    "Room booked successfully ! Your booking confirmation code is : "+confirmationCode);
        }catch(InvalidBookingRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }



    @DeleteMapping("/booking/{id}/delete")
    public void cancelBooking(@PathVariable("id") Long id){
        bookingService.cancelBooking(id);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new BookingResponse(
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),booking.getGuestFullName(),
                booking.getGuestEmail(), booking.getNumOfAdults(),
                booking.getNumOfChildren(),booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(), room);

    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

}
