import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Signup = () => {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: "",
        points: 0,
        proficiency: "", 
        region: "", 
    });

    const [countries, setCountries] = useState([]);
    const [filteredCountries, setFilteredCountries] = useState([]);
    const proficiencyLevels = ["A1","A2","B1","B2","C1","C2"];
    const navigate = useNavigate();
    const [regionErr, setRegionErr] = useState("");

    useEffect(() => {
        fetch("https://restcountries.com/v3.1/all")
        .then((response) => response.json())
        .then((data) => {
            const countryNames = data.map((country) => country.name.common);
            countryNames.sort();
            setCountries(countryNames);
        })
        .catch((error) => console.error("Error fetching countries:", error));
    }, [])

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "region") {
            const filtered = countries.filter((country) =>
                country.toLowerCase().startsWith(value.toLowerCase())
            );
            setFilteredCountries(filtered);
        }

        setFormData({
            ...formData,
            [name]: value,
        });
    }

    const handleSignup = async (e) => {
        e.preventDefault();

        if (!countries.includes(formData.region)) {
            setRegionErr("Please select a valid country from the list.");
            return;
        }
        setRegionErr("");
        

        try {
            console.log("Info: ",JSON.stringify(formData))
            const res = await fetch("http://localhost:8080/api/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });

            if (res.ok) {
                alert("Account created successfully!");
                setFormData({
                    username: "",
                    email: "",
                    password: "",
                    points: 0,
                    proficiency: "",
                    region: "",
                });
                navigate("/login");
            } else {
                const errMsg = await res.text();
                alert(`Error: ${errMsg}`);
            }

        } catch (err) {
            console.error("Error during signup: ",err);   
            alert("An error occurred, please try again,");
        }
            
    }

    return (
        <>
            <h1>Sign Up</h1>
            
            <form onSubmit={handleSignup}>
                <div className="user-info">
                <div>
                    <label>Region: </label>
                        <input
                            type="text"
                            id="region"
                            name="region"
                            value={formData.region}
                            onChange={handleChange}
                            placeholder="Search for a Country"
                            required
                        />
                        {filteredCountries.length > 0 && (
                            <ul>
                                {filteredCountries.map((country) => (
                                    <li
                                        key={country}
                                        onClick={() => {
                                            setFormData({ ...formData, region: country });
                                            setFilteredCountries([]);  
                                        }}
                                    >
                                        {country}
                                    </li>
                                ))}
                            </ul>
                        )}
                        {regionErr &&
                            <p>{regionErr}</p>
                        }
                    </div>

                    <div>
                        <label>Proficiency: </label>
                        <select 
                            id="proficiency"
                            name="proficiency"
                            value={formData.proficiency}
                            onChange={handleChange}
                            required 
                        >
                            <option>Select CEFR Level</option>
                            {proficiencyLevels.map((level) => (
                                <option key={level} value={level}>
                                    {level}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                       <label>Points: 0</label>
                    </div>
                </div>

            <div className="login-info">
                    <div>
                        <label>Username: </label>
                        <input 
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Email: </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div>
                        <label>Password: </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>
                <button type="submit">Sign Up</button>
            </form>
        </>
    );
}


export default Signup