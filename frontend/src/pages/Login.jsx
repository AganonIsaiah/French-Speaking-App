import { useState } from "react"
import { useNavigate } from "react-router-dom";

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
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username: formData.username, password: formData.password }),
            });
    
            if (response.ok) {
                alert("Login successful!");
                navigate("/home"); // Redirect to home page
            } else {
                const errorMessage = await response.text();
                alert(`Error: ${errorMessage}`);
            }
        } catch (err) {
            console.error("Error during login: ", err);
            alert("An error has occurred, please try again.");
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