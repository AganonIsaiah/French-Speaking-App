import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"

import Home from "./pages/Home"
import Login from "./pages/Login"
import Signup from "./pages/Signup"
import MultiChat from "./pages/MultiChat"
import SingleChat from "./pages/SingleChat"


function App() {
   
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          <Route path="/home" element={<Home />} />

          <Route path="/multichat" element={<MultiChat />} />
          <Route path="/singleChat" element={<SingleChat />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
