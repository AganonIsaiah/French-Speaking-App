import { useSpeechRecognition } from "../utils/useSpeechService";

const UserAICamera = ({ onTranscript }) => {
  const { startListening, stopListening, listening } = useSpeechRecognition(onTranscript);

  return (
    <div>
      <button onClick={listening ? stopListening : startListening}>
        {listening ? "Arrêtez de Parler" : "Parler avec L'IA"}
      </button>
    </div>
  );
};

export default UserAICamera;
