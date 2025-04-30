import { useState } from "react";
import { useNav } from "../utils/useNav";
import UserAICamera from "../components/UserAICamera";
import UserAIMessages from "../components/UserAIMessages";
import { simpleGenerate } from "../services/speechService";
import { useSpeechSynthesis } from "react-speech-kit";

const SingleChat = () => {
  const { handleHome } = useNav();
  const [messages, setMessages] = useState([]);
  const { speak, voices } = useSpeechSynthesis();

  const handleTranscript = async (transcript) => {
    const userMsg = { sender: "user", text: transcript };
    setMessages((prev) => [...prev, userMsg]);

    try {
      const aiText = await simpleGenerate(transcript);
      const aiMsg = { sender: "ai", text: aiText };
      setMessages((prev) => [...prev, aiMsg]);

      const frenchVoice = voices.find((v) => v.lang.startsWith("fr"));
      if (frenchVoice) {
        speak({ text: aiText, voice: frenchVoice });
      }
    } catch (err) {
      console.error("Error fetching AI response:", err);
    }
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
