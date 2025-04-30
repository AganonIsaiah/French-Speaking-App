import { useState } from "react";
import { useNav } from "../utils/useNav";
import UserAICamera from "../components/UserAICamera";
import UserAIMessages from "../components/UserAIMessages";

const SingleChat = () => {
  const { handleHome } = useNav();
  const [messages, setMessages] = useState([]);

  // Function to handle the transcript update
  const handleTranscript = (sender, text) => {
    setMessages((prevMessages) => [
      ...prevMessages,
      { sender, text },
    ]);
  };

  return (
    <div>
      <h1>Single Chat</h1>

      <div className="chat-container">
        <div className="camera-container">
          <UserAICamera onTranscript={handleTranscript} />
        </div>

        <div className="messages-container">
          <UserAIMessages messages={messages} />
        </div>
      </div>

      <button onClick={handleHome}>Home</button>
    </div>
  );
};

export default SingleChat;
