import { useNavigate } from "react-router-dom";

const Home = () => {
    const navigate = useNavigate();


    const handleMultiChat = () => {
        navigate("/multichat");
    }

    const handleSingleChat = () => {
        navigate("/singlechat");
    }

    const handleLeaderboard = () => {
        navigate("/leaderboard");
    }

    return (
        <>
            <h1>Home Page</h1>

            <div className="single-chat">
                <button onClick={handleSingleChat}>Single Chat</button>
            </div>

            <div className="multi-chat">
                <button onClick={handleMultiChat}>Multi-Chat</button>
            </div>

            <div className="leaderboard">
                <button onClick={handleLeaderboard}>Leaderboard</button>
            </div>
        </>
    )


}


export default Home;