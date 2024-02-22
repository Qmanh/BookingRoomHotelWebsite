import { useState } from 'react'
import './App.css'
import AddRoom from './components/room/AddRoom'
import "../node_modules/bootstrap/dist/css/bootstrap.min.css"
import "/node_modules/bootstrap/dist/js/bootstrap.min.js"
import ExistingRooms from './components/room/ExistingRoom'
import { BrowserRouter as Router,Routes,Route } from 'react-router-dom'
import Home from './components/home/Home';
import EditRoom from './components/room/EditRoom'
import NavBar from './components/layout/NavBar'
import Footer from './components/layout/Footer'
import RoomListing from './components/room/RoomListing'
import Admin from './components/admin/Admin'
import CheckOut from './components/bookings/CheckOut'
import BookingSuccess from './components/bookings/BookingSucess'
import Bookings from './components/bookings/Bookings'
import FindBooking from './components/bookings/FindBooking'
import Login from './components/auth/Login'
import Registration from './components/auth/Registration'
import Profile from './components/auth/Profile'
import { AuthProvider } from './components/auth/AuthProvider'
import RequireAuth from './components/auth/RequireAuth'
import Logout from './components/auth/Logout'
import ChangePassword from './components/auth/ChangePassword'
import ForgotPassword from './components/auth/ForgotPassword'

function App() {
  const [count, setCount] = useState(0)

  return (
   <AuthProvider>
    <main>
      <Router>
        <NavBar/>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/edit-room/:id"  element={<EditRoom/>} />
          <Route path="/existing-rooms"  element={<ExistingRooms/>} />
          <Route path="/add-room"  element={<AddRoom/>} />

          <Route
           path="/book-room/:id"  
           element={
              <RequireAuth>
                <CheckOut/>
              </RequireAuth>} />

          <Route path="/browse-all-rooms"  element={<RoomListing/>} />
          <Route path="/admin"  element={<Admin/>} />
          <Route path="/booking-success"  element={<BookingSuccess/>} />
          <Route path="/existing-bookings"  element={<Bookings/>} />
          <Route path="/find-booking"  element={<FindBooking/>} />

          <Route path="/login"  element={<Login/>} />
          <Route path="/register"  element={<Registration/>} />

          <Route path="/profile"  element={<Profile/>} />
          <Route path="/logout"  element={<Logout/>} />
          <Route path="/change-password"  element={<ChangePassword/>} />
          <Route path="/forgotPassword"  element={<ForgotPassword/>} />
        </Routes>
      </Router>
      <Footer/>
    </main>
    </AuthProvider>
  )
}

export default App
