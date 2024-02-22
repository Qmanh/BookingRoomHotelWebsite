import React, { useContext, useState } from "react";
import {Link, useNavigate,useLocation} from "react-router-dom";
import { ForgotPasswordUser } from "../utils/ApiFunctions";
import { useAuth } from "./AuthProvider";

const ForgotPassword = () =>{
    const[errorMessage, setErrorMessage] = useState("")
    const [email,setEmail] = useState("")
    const [successMessage, setSuccessMessage] = useState("")
    const [isConfirm, setIsConfirm] = useState(false)
    const navigate = useNavigate()

    const auth = useAuth()
    const location = useLocation()

    const redirectUrl = location.state?.path || "/"
    const handleInputChange = (e) => {
        setEmail(e.target.value)
	}

    const handleSubmit = async(e) =>{
        e.preventDefault()
        
        console.log(email)
        const success = await ForgotPasswordUser(email)
        if(success){
            setIsConfirm(true)
            setSuccessMessage("Please check email and change password now!")
            setEmail("")
    
        }else{
            setErrorMessage("Email not existing")
        }
        setTimeout(()=>{
            setErrorMessage("")
        },4000)
    }
    

    return (
        <section className="container col-6 mt-5 mb-5">
			{errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
			<h2>FORGOT PASSWORD</h2>
            {successMessage && <div className="alert alert-success mt-3 fade show">{successMessage}</div>}
			<form onSubmit={handleSubmit}>
				<div className="row mb-3">
					<label htmlFor="email" className="col-sm-3 col-form-label">
						<b>Email</b>
					</label>
					<div>
						<input
							id="email"
							name="email"
							type="email"
							className="form-control"
							value={email}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-3">
					<button type="submit" className="btn btn-hotel" style={{ marginRight: "10px" }}>
						OK
					</button>
                    <Link to="/">
                        <button type="submit" className="btn btn-warning" style={{ marginRight: "10px" }}>
						    BACK
					    </button>
                    </Link>
				</div>
			</form>
		</section>
	)
}

export default ForgotPassword