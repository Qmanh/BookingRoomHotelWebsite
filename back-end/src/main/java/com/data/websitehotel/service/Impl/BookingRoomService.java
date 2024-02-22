package com.data.websitehotel.service.Impl;

import com.data.websitehotel.exception.InvalidBookingRequestException;
import com.data.websitehotel.exception.ResourceNotFoundException;
import com.data.websitehotel.model.BookedRoom;
import com.data.websitehotel.model.Room;
import com.data.websitehotel.model.User;
import com.data.websitehotel.repository.BookingRepository;
import com.data.websitehotel.repository.UserRepository;
import com.data.websitehotel.security.user.HotelUserDetails;
import com.data.websitehotel.service.IBookingRoomService;
import com.data.websitehotel.service.IRoomService;
import com.data.websitehotel.utils.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingRoomService implements IBookingRoomService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long id) {
        return bookingRepository.findByRoomId(id);
    }

    @Override
    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public String saveBooking(Long id, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date musr come before check-out date");
        }



        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var user = (HotelUserDetails) auth.getPrincipal();
        User user1 = userRepository.findByEmail(user.getEmail()).get();

        Room room = roomService.getRoomById(id).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean  roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);



        if(roomIsAvailable){
            room.addBooking(bookingRequest);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(Email.writeSubjectInformBooking(user1.getLastName()+""+user1.getFirstName()));
            message.setText(Email.writeInformBooking(bookingRequest));
            mailSender.send(message);

            Long range = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
            Long totalPrice = bookingRequest.getRoom().getRoomPrice().longValue() * range ;
            bookingRequest.setTotalPrices(totalPrice);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("Sorry, This room is not availabel for the selected dates!");
        }
        return bookingRequest.getBookingConfirmationCode();
    }


    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking  found with booking code: "+confirmationCode));
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }


}
