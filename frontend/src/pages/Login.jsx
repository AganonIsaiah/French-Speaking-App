import { useState } from "react"
import { useNavigate } from "react-router-dom";
import { loginAuth } from "../services/authService";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();


    /**
     * For login
     */
    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            await loginAuth(username, password);
            localStorage.setItem('username', username);
           // alert("Login successful.");
            navigate("/home")
        } catch (err) {
            console.error("Error during login: ", err);
           // alert("An error has occurred, please try again.");
        }
    }

    /**
     * For register
     */
    const handleRegister = (e) => {
        e.preventDefault();
        navigate("/signup");
    };

    return (
        <>
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                <div className="username">
                <label>Username: </label>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                </div>
                <div className="password">
                    <label>Password: </label>
                    <input 
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    />
                </div>
                <button type="submit">
                    Login
                </button>
            </form>

            <button type="button" onClick={handleRegister}>
                Register
            </button>
        </>
    )
}

export default Login;