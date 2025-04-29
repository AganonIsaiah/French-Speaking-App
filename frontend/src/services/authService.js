const BASE_URL = "http://localhost:8080/api/auth"

export const signUpAuth = async (formData) => {
    try {
        const res = await fetch(`${BASE_URL}/signup`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        })
        if (!res.ok) {
            const errMsg = await res.text();
            throw new Error(errMsg); 
        }
        return await res.json();
    } catch (err) {
        throw err;
    }
};

export const loginAuth = async ( username, password ) => {
    try {
        const res = await fetch(`${BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        if (!res.ok) {
            const errMsg = await res.text();
            throw new Error(errMsg);
        }
        return await res.json();
    } catch (err) {
        throw err;
    }
};