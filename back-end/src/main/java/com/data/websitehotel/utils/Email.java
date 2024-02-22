package com.data.websitehotel.utils;

import com.data.websitehotel.model.BookedRoom;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class Email {
    public static String writeEmailSendPassword(String password){
        String message= String.format("Hello,\n" +
                "New password for your account: "+ password +"\n"+
                "When you login. you should change a new password \n"+
                "Thanks, \n"+
                "The Bear hotel is sent this mail!"
        );
        return message;
    }
    public static String writeSubject(String username){
        return "Reset Password Username: "+username +" at the Bear hotel";
    }

    public static String writeSubjectInformBooking(String username){
        return "This is about information booking: "+username +" at the Bear hotel";
    }
    public static String writeInformBooking(BookedRoom bookedRoom){
        Long range = ChronoUnit.DAYS.between(bookedRoom.getCheckInDate(), bookedRoom.getCheckOutDate());
        String message= String.format("Hello, This is information about your booking,\n" +
                "Booking Confirmation Code: "+bookedRoom.getBookingConfirmationCode()+"\n"+
                "Room Number: "+bookedRoom.getRoom().getId()+"\n"+
                "Room Type: "+bookedRoom.getRoom().getRoomType()+"\n"+
                "Check-in Date: "+bookedRoom.getCheckInDate()+"\n"+
                "Check-out Date: "+bookedRoom.getCheckOutDate()+"\n"+
                "Full Name: "+bookedRoom.getGuestFullName()+"\n"+
                "Email Address: "+bookedRoom.getGuestEmail()+"\n"+
                "Adults: "+bookedRoom.getNumOfAdults()+"\n"+
                "Children: "+bookedRoom.getNumOfChildren()+"\n"+
                "Total Guest: "+bookedRoom.getTotalNumOfGuest()+"\n"+
                "Total Days: "+range+"\n"+
                "Total Price: "+bookedRoom.getTotalPrices()+"$\n"+
                "Thanks, \n"+
                "The Bear hotel is sent this mail!");
        return message;
    }

}
