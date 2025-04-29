
import { useNav } from "../utils/useNav";

const SingleChat = () => {
    const { handleHome } = useNav();

    return (
        <>
            <h1>Single Chat</h1>


            <button onClick={handleHome}>Home</button>
        </>
    )
}

export default SingleChat;