import React, { useEffect, useState } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  useNavigate,
} from "react-router-dom";
import Transactions from "./Transactions";
import "./styles.css";

function DashboardHome() {
  const [health, setHealth] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/health`)
      .then(res => res.json())
      .then(data => setHealth(data))
      .catch(() => setHealth(null));
  }, []);

  return (
    <div style={{ padding: "40px", fontFamily: "Arial" }}>
      <h1>Payment Gateway Dashboard</h1>

      <button onClick={() => navigate("/")}>Home</button>
      <button
        onClick={() => navigate("/transactions")}
        style={{ marginLeft: "10px" }}
      >
        Transactions
      </button>

      <hr />

      {!health && <p>Loading...</p>}

      {health && (
        <div>
          <p><strong>Overall Status:</strong> {health.status}</p>
          <p><strong>Database:</strong> {health.database}</p>
          <p><strong>Redis:</strong> {health.redis}</p>
          <p><strong>Worker:</strong> {health.worker}</p>
          <p>
            <strong>Last Updated:</strong>{" "}
            {new Date(health.timestamp).toLocaleString()}
          </p>
        </div>
      )}
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<DashboardHome />} />
        <Route path="/transactions" element={<Transactions />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
