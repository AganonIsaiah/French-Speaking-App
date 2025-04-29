import { useNav } from "../utils/useNav";
import { useEffect, useState } from "react";
 
const Home = () => {
    const { handleMultiChat, handleSingleChat, handleLeaderboard } = useNav();
     
    return (
        <>
            <h1>Home Page</h1>
            <div className="buttons">
                <div className="single-chat">
                    <button onClick={handleSingleChat}>Single Chat</button>
                </div>

                <div className="multi-chat">
                    <button onClick={handleMultiChat}>Multi-Chat</button>
                </div>

                <div className="leaderboard">
                    <button onClick={handleLeaderboard}>Leaderboard</button>
                </div>
            </div>

           

            
        </>
    )


}


export default Home;