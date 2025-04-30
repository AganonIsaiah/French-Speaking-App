import { useEffect, useRef, useState } from 'react';
import { useNav } from "../utils/useNav";
import UserCamera from '../components/UserCamera';

const SingleChat = () => {
    const { handleHome } = useNav();

    return (
        <>
            <h1>Single Chat</h1>

            <UserCamera />

            <button onClick={handleHome}>Home</button>
        </>
    )
}

export default SingleChat;