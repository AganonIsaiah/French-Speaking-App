
import { useNavigate } from "react-router-dom";

export const useNav = () => {
    const navigate = useNavigate();

    const handleMultiChat = () => {navigate("/multichat")}
    const handleSingleChat = () => {navigate("/singlechat")}
    const handleLeaderboard = () => {navigate("/leaderboard")}
    const handleHome = () => {navigate("/home")}

    return {
        handleMultiChat,
        handleSingleChat,
        handleLeaderboard, 
        handleHome
    }
}