import { useState, useEffect } from "react";


const Signup = () => {
    const [formData, setformData] = useState({
        username: "",
        email: "",
        password: "",
        points: 0,
        proficiency: "", // Based on CEFR A1-C2
        region: "", // Countries
    });

    const [countries, setCountries] = useState([]);
    const [filteredCountries, setFilteredCountries] = useState([]);
    const proficiencyLevels = ["A1","A2","B1","B2","C1","C2"];

    useEffect(() => {
        fetch("https://restcountries.com/v3.1/all")
        .then((response) => response.json())
        .then((data) => {
            const countryNames = data.map((country) => country.name.common);
            setCountries(countryNames);
        })
        .catch((error) => console.error("Error fetching countries:", error));
    }, [])

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "region"){
            const filtered = countries.filter((country) => 
                country.toLowerCase().includes(value.toLowerCase())
            );
            setFilteredCountries(filtered);
        }

        setformData({
            ...formData,
            [name]: value,
        });
    }


    return (
        <>
            <h1>Sign Up</h1>

            <form>
                <div className="user-info">
                    <div>
                        <label>Proficiency: </label>
                        <select 
                            id="proficiency"
                            name="proficiency"
                            value={formData.proficiency}
                            onChange={handleChange}
                            required 
                        >
                            <option value="" disable>Select CEFR Level</option>
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