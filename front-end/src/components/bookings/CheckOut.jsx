import React, { useEffect, useState } from "react";
import BookingForm from "./BookingForm";
import { getRoomById } from "../utils/ApiFunctions";
import { useParams } from "react-router-dom";
import {FaCar, FaTv, FaUtensils, FaWifi, FaWineGlassAlt} from "react-icons/fa";
import RoomCarousel from "../common/RoomCarousel";

const CheckOut = ()=>{
    const [error,setError] = useState("")
    const [isLoading,setIsLoading] = useState(true)
    const [roomInfo,setRoomInfo] = useState({photo:"",roomType:"",roomPrice:""})

    const {id} = useParams()

    useEffect(()=>{
        setTimeout(()=>{
            getRoomById(id)
            .then((response)=>{
                setRoomInfo(response)
                setIsLoading(false)
            }).catch((error)=>{
                setError(error)
                setIsLoading(false)
            })
        },2000)
    },[id])

    return(
        <div>
            <section className="container">
                <div className="row flex-column flex-md-row align-items-center">
                    <div className="col-md-4 mt-5 mb-5">
                        {isLoading ? (
                            <p>Loading room information</p>
                        ): error? (
                            <p>{error}</p>
                        ):(
                            <div className="room-info">
                                <img
                                src={`data:image/png;base64,${roomInfo.photo}`}
                                alt="Room Photo"
                                style={{width:"100%",height:"200px"}}
                                />

                                <table className="table table-bordered">
                                    <tbody>
                                        <tr className="text-start">
                                            <th>Room Type: </th>
                                            <td>{roomInfo.roomType}</td>
                                        </tr>

                                        <tr className="text-start">
                                            <th>Room Price: </th>
                                            <td>${roomInfo.roomPrice}</td>
                                        </tr>

                                        <tr className="text-start">
                                            <th>Room Service: </th>
                                            <td>
                                                <ul className="list-unstyled">
                                                    <li><FaWifi/> WiFi</li>

                                                    <li><FaTv/> Netflix Prenium</li>

                                                    <li><FaUtensils/> Breakfast</li>

                                                    <li><FaWineGlassAlt/> Mini bar refreshment</li>
                                                    
                                                    <li><FaCar/> Car Service</li>
                                                </ul>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                    <div className="col-md-8 text-start">
                        <BookingForm/>
                    </div>
                </div>
            </section>
            <div className="container">
                <RoomCarousel/>
            </div>
        </div>
    )
}
export default CheckOut