import { useState, useRef } from "react";
import { useSpeechSynthesis } from "react-speech-kit";

export const useSpeechRecognition = (onTranscript) => {
  const [listening, setListening] = useState(false);
  const recognitionRef = useRef(null);
  const { speak, voices } = useSpeechSynthesis();
  const lastMessageRef = useRef(""); // Track the sender of the last message

  const startListening = () => {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
      alert("Speech Recognition not supported in this browser.");
      return;
    }

    const recognition = new SpeechRecognition();
    recognition.lang = "fr-FR";
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.onresult = async (event) => {
      const transcript = event.results[0][0].transcript;
      console.log("You said:", transcript);
      
      // Only send the user message if the last message wasn't from the AI
      if (lastMessageRef.current !== "AI") {
        onTranscript("Vous", transcript); // Send the user's message to the parent

        try {
          const response = await fetch("http://localhost:8080/api/chat/generate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(transcript),
          });

          const aiText = await response.text();
          console.log("AI says:", aiText);
          
          lastMessageRef.current = "AI"; // Mark the last message as from the AI
          onTranscript("AI", aiText); // Send the AI's message to the parent

          // Check if we have a French voice available
          const frenchVoice = voices.find((v) => v.lang.startsWith("fr"));
          if (frenchVoice) {
            speak({ text: aiText, voice: frenchVoice });
          }
        } catch (err) {
          console.error("Failed to fetch AI response:", err);
        }
      }
    };

    recognition.onerror = (event) => {
      console.error("Speech recognition error", event.error);
    };

    recognition.onend = () => {
      setListening(false);
    };

    recognition.start();
    setListening(true);
    recognitionRef.current = recognition;
  };

  const stopListening = () => {
    recognitionRef.current?.stop();
    setListening(false);
  };

  return { startListening, stopListening, listening };
};
