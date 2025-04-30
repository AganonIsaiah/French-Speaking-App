const BASE_URL = "http://localhost:8080/api/chat"

export const simpleGenerate = async (transcript) => {
    try {
        const res = await fetch (`${BASE_URL}/generate/simple`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(transcript), 
        })

        if (!res.ok) {
            const errMsg = await res.text();
            throw new Error(errMsg);
        }
        return await res.text();
    } catch (err) {
        throw err;
    }
};