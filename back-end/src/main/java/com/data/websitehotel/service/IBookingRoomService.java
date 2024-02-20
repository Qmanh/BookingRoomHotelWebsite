package com.data.websitehotel.service;

import com.data.websitehotel.model.BookedRoom;

import java.util.List;

public interface IBookingRoomService {
    List<BookedRoom> getAllBookingsByRoomId(Long id);

    void cancelBooking(Long id);

    String saveBooking(Long id, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);
}
