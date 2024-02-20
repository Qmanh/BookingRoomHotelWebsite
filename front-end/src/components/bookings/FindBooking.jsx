import React, { useState } from "react";
import { cancelBooking, getBookingByConfirmationCode } from "../utils/ApiFunctions";
import moment from "moment";
const FindBooking = () =>{
    const [confirmationCode, setConfirmationCode] = useState("")
    const [error, setError] = useState("")
    const [isLoading, setIsLoading]= useState(false)
    const [successMessage, setSuccessMessage] = useState("")
    const [bookingInfo, setBookingInfo] = useState({
        id: "",
		bookingConfirmationCode: "",
		room: { id: "", roomType: "" },
		roomNumber: "",
		checkInDate: "",
		checkOutDate: "",
		guestName: "",
		guestEmail: "",
		numOfAdults: "",
		numOfChildren: "",
		totalNumOfGuests: ""

    })
    const [isDeleted, setIsDeleted] = useState(false)

    const clearBookingInfo = {
        id: "",
		bookingConfirmationCode: "",
		room: { id: "", roomType: "" },
		roomNumber: "",
		checkInDate: "",
		checkOutDate: "",
		guestName: "",
		guestEmail: "",
		numOfAdults: "",
		numOfChildren: "",
		totalNumOfGuests: ""
     }

    const handleInputChange = (e)=>{
        setConfirmationCode(e.target.value)
    }

    const handleFormSubmit = async (event) => {
		event.preventDefault()
		setIsLoading(true)

		try {
			const data = await getBookingByConfirmationCode(confirmationCode)
			setBookingInfo(data)
			setError(null)
		} catch (error) {
			setBookingInfo(clearBookingInfo)
			if (error.response && error.response.status === 404) {
				setError(error.response.data.message)
			} else {
				setError(error.message)
			}
		}

		setTimeout(() => setIsLoading(false), 2000)
	}
    const handleBookingCancellation = async(id)=>{
        try{
            await cancelBooking(bookingInfo.id)
            setIsDeleted(true)
            setBookingInfo(clearBookingInfo)
            setSuccessMessage("Booking has been cancelled successfully!")
            setConfirmationCode("")
            setError("")
        }catch(error){
            setError(error.message)
        }
        setTimeout(() => {
			setSuccessMessage("")
			setIsDeleted(false)
		}, 2000)
    }
        
return (
    <>
    <div className="container mt-5 d-flex flex-column justify-content-center align-items-center text-start">
        <h2>Find My Booking</h2>
        <form onSubmit={handleFormSubmit} className="col-md-6">
            <div className="input-group mb-3">
                <input
                className="form-control"
                id="confirmationCode"
                name = "confirmationCode"
                value={confirmationCode}
                onChange={handleInputChange}
                placeholder="Enter the booking confirmation code"
                />

                <button className="btn btn-hotel input-group-text">Find booking </button>
            </div>
        </form>
        {isLoading ? (<div>Finding booking ....</div>
        ):error ?(
            <div className="text-danger">{error}</div>
        ):bookingInfo.bookingConfirmationCode ? (
            <div className="col-md-6 mt-4 mb-5">
                <h3>Booking Information</h3>
                <p>Booking Confirmation Code : {bookingInfo.bookingConfirmationCode}</p>
                <p>Booking ID: {bookingInfo.id}</p>
                <p>Room Number: {bookingInfo.room.id}</p>
                <p>Room Type: {bookingInfo.room.roomType}</p>

                <p>
					Check-in Date:{" "}
					{moment(bookingInfo.checkInDate).subtract(1, "month").format("MMM Do, YYYY")}
				</p>
				<p>
					Check-out Date:{" "}
					{moment(bookingInfo.checkOutDate).subtract(1, "month").format("MMM Do, YYYY")}
				</p>

                <p>Full Name: {bookingInfo.guestFullName}</p>
				<p>Email Address: {bookingInfo.guestEmail}</p>
				<p>Adults: {bookingInfo.numOfAdults}</p>
				<p>Children: {bookingInfo.numOfChildren}</p>
				<p>Total Guest: {bookingInfo.totalNumOfGuest}</p>

                {!isDeleted && (
                    <button
                    className="btn btn-danger"
                    onClick={() => handleBookingCancellation(bookingInfo.id)}
                    >
                    Cancel Booking:</button>
                )}
                </div>
                ):(
                    <div>
                        find booking ....
                    </div>
                )}
        {isDeleted && <div className="alert alert-success mt-3 fade show">{successMessage}</div>}
    </div>
    </>
)
}

export default FindBooking