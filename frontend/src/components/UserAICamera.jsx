import { useSpeechRecognition } from "../services/speechService";

const UserAICamera = ({ onTranscript }) => {
  const { startListening, stopListening, listening } = useSpeechRecognition(onTranscript);

  return (
    <div>
      <button onClick={listening ? stopListening : startListening}>
        {listening ? "Stop Talking" : "Speak to AI"}
      </button>
    </div>
  );
};

export default UserAICamera;
