import React from "react";
import Header from "../common/Header";
import { useLocation, Link } from "react-router-dom";

const BookingSuccess =() =>{
    const location = useLocation()
	const message = location.state?.message
	const error = location.state?.error

	console.log(message , error)
	return (
		<div className="container">
			<Header title="Booking Success" />
			<div className="mt-5">
				{message ? (
					<div>
						<h3 className="text-success"> Booking Success!</h3>
						<p className="text-success">{message}</p>
					</div>
				):error?(
					<div>
						<h3 className="text-danger"> Error Booking Room!</h3>
						<p className="text-danger">{error}</p>
						
					</div>
				):(
					<div>
						<h3 className="text-danger"> Processing!</h3>
					</div>
				)}
			</div>
		</div>
	)
}

export default BookingSuccess