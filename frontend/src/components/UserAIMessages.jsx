const UserAIMessages = ({ messages }) => {
  return (
    <div className="messages">
      {messages.map((msg, index) => (
        <div key={index} className={`message ${msg.sender}`}>
          <strong>{msg.sender === "user" ? "Vous" : "AI"}:</strong> {msg.text}
        </div>
      ))}
    </div>
  );
};

export default UserAIMessages;
