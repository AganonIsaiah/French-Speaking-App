import { useState, useEffect } from "react";

const UserAIMessages = ({ messages }) => {
  return (
    <div className="messages-container">
      <div className="messages">
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.sender === "Vous" ? "user" : "ai"}`}>
            <strong>{msg.sender === "Vous" ? "Vous" : "AI"}:</strong> {msg.text}
          </div>
        ))}
      </div>
    </div>
  );
};

export default UserAIMessages;
