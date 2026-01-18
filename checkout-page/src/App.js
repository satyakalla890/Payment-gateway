import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Checkout from "./pages/Checkout";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/checkout/:order_id" element={<Checkout />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
