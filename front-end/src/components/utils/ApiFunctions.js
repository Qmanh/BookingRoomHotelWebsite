import axios from "axios"

export const api = axios.create({
    baseURL : "http://localhost:8080" 
})

export const getHeader = () =>{
    const token  = localStorage.getItem("token")
    return{
        Authorization : `Bearer ${token}`,
        "Content-Type" : "application/json"
    }
}

export async function addRoom(photo, roomType,roomPrice){
    const formData = new FormData()
    formData.append("photo",photo);
    formData.append("roomType",roomType);
    formData.append("roomPrice",roomPrice);

    const response = await api.post("/rooms/add/new-room", formData)
    if(response.status === 201){
        return true;
    }else{
        return false;
    }
}

export async function getRoomTypes() {
    try{
        const response = await api.get("/rooms/room/types");
        return response.data
    }catch(error){
        throw new Error("Error fetching room types");
    }
}

export async function getAllRooms(){
    try{
        const result= await api.get("/rooms/all-rooms")
        return result.data
    }catch(error){
        throw new Error("Error fetching rooms")
    }
}

export async function deleteRoom(id){
    try{
        const result = await api.delete(`/rooms/delete/room/${id}`)
        return result.data
    }catch(error){
        throw new Error(`Error deleting room ${error.message}`)

    }
}

export async function updateRoom(id, roomData) {
	const formData = new FormData()
	formData.append("roomType", roomData.roomType)
	formData.append("roomPrice", roomData.roomPrice)
	formData.append("photo", roomData.photo)
	const response = await api.put(`/rooms/update/${id}`, formData)
	return response
}
export async function getRoomById(id){
    try{
        const result = await api.get(`/rooms/room/${id}`)
        return result.data
    }catch(error){
        throw new Error(`Error fetching room ${error.message}`)
    }
   
}

export async function bookRoom(id,booking){
    try{
        const response = await api.post(`/bookings/room/${id}/booking`,booking)
        return response.data
    }catch(error){
        if(error.response && error.response.data){
            throw new Error(error.response.data)
        }else{
            throw new Error(`Error booking room : ${error.message}`)
        }
    }
}

export async function getAllBookings(){
    try{
        const result = await api.get("/bookings/all-bookings")
        return result.data
    }catch(error){
        throw new Error(`Error fetching bookings: ${error.message}`)
    }
}

export async function getBookingByConfirmationCode(confirmationCode){
    try{
        const result = await api.get(`/bookings/confirmation/${confirmationCode}`)
        return result.data
    }catch(error){
        if(error.response && error.response.data){
            throw new Error(error.response.data)
        }else{
            throw new Error(`Error find booking: ${error.message}`)
        }
    }
}

export async function cancelBooking(id){
    try{
        const result = await api.delete(`/bookings/booking/${id}/delete`)
        return result.data

    }catch(error){
        throw new Error(`Error cancelling booking: ${error.message}`)
    }
}

export async function getAvailableRooms(checkInDate,checkOutDate,roomType){
    const result = await api.get
    (`/rooms/available-rooms?checkInDate=${checkInDate}&checkOutDate=${checkOutDate}&roomType=${roomType}`)

    return result
}

export async function registerUser(registration){
    try{

        const response = await api.post("/auth/register-user",registration)
        return response.data
    }catch(error){
        if(error.response && error.response.data){
            throw new Error(error.response.data)
        }else{
            throw new Error(`User registration error : ${error.message}`)
        }
    }
}

export async function loginUser(login){
    try{
        const response = await api.post("/auth/login",login)
        if(response.status >= 200 && response.status < 300){
            return response.data
        }else{
            return null
        }
    }catch(error){
        console.error(error)
        return null
    }    
}

export async function getUserProfile(userId, token){
    try{
        const response = await api.get(`user/profile/${userId}`,{
            headers : getHeader()
        })
        return response.data
    }catch(error){
        throw error
    }
}

export async function deleteUser(userId){
    try{
        const response = await api.delete(`/user/delete/${userId}`, {
            headers : getHeader()
        })
        return response.data

    }catch(error){
        return error.message()
    }
}

export async function getUser(userId, token){
    try{
        const response = await api.get(`/user/${userId}`,{
            headers : getHeader()
        })
        return response.data
    }catch(error){
        throw error
    }
}

export async function getBookingsByUserId(userId, token) {
	try {
		const response = await api.get(`/bookings/user/${userId}/bookings`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		console.error("Error fetching bookings:", error.message)
		throw new Error("Failed to fetch bookings")
	}
}