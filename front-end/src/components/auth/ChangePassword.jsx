import React, { useContext, useState } from "react";
import {Link, useNavigate,useLocation} from "react-router-dom";
import { ChangePasswordUser } from "../utils/ApiFunctions";
import { useAuth } from "./AuthProvider";

const ChangePassword = () =>{
    const[errorMessage, setErrorMessage] = useState("")
	const [successMessage, setSuccessMessage] = useState("")
	const [isConfirm, setIsConfirm] = useState(false)
    const[enterPassword , setEnterPassword] = useState({
        currentPassword: "",
        newPassword: "",
        confirmationPassword:"",
    })
    const navigate = useNavigate()

    const auth = useAuth()
    const location = useLocation()

    const redirectUrl = location.state?.path || "/"
    const handleInputChange = (e) => {
		const { name, value } = e.target
		setEnterPassword({ ...enterPassword, [name]: value })
		setErrorMessage("")
	}

    const handleSubmit = async(e) =>{
        e.preventDefault()
        
        console.log(enterPassword)
		try{
        	const success = await ChangePasswordUser(enterPassword)
			setIsConfirm(true)
            setSuccessMessage(success)
			setEnterPassword({
				currentPassword: "",
        		newPassword: "",
        		confirmationPassword:"",
			})
		}catch(error){
			setErrorMessage(`ChangePassword error : ${error.message}`)
			setEnterPassword({
				currentPassword: "",
        		newPassword: "",
        		confirmationPassword:"",
			})
		}
        
        setTimeout(()=>{
            setErrorMessage("")
        },4000)
    }
    

    return (
        <section className="container col-6 mt-5 mb-5">
			{errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
			<h2>CHANGE PASSWORD</h2>
			{successMessage && <div className="alert alert-success mt-3 fade show">{successMessage}</div>}
			<form onSubmit={handleSubmit}>
				<div className="row mb-3">
					<label htmlFor="currentPassword" className="col-sm-3 col-form-label">
						<b>Current Password</b>
					</label>
					<div>
						<input
							id="currentPassword"
							name="currentPassword"
							type="password"
							className="form-control"
							value={enterPassword.currentPassword}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="row mb-3">
					<label htmlFor="newPassword" className="col-sm-3 col-form-label">
						<b>New Password</b>
					</label>
					<div>
						<input
							id="newPassword"
							name="newPassword"
							type="password"
							className="form-control"
							value={enterPassword.newPassword}
							onChange={handleInputChange}
						/>
					</div>
				</div>

                <div className="row mb-3">
					<label htmlFor="confirmationPassword" className="col-sm-3 col-form-label">
						<b>Confirmation Password</b>
					</label>
					<div>
						<input
							id="confirmationPassword"
							name="confirmationPassword"
							type="password"
							className="form-control"
							value={enterPassword.confirmationPassword}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3">
					<button type="submit" className="btn btn-hotel" style={{ marginRight: "10px" }}>
						OK
					</button>
				</div>
			</form>
		</section>
	)
}

export default ChangePassword