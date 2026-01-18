import React, { useEffect, useState } from "react";
import "./styles.css";

const API_URL = process.env.REACT_APP_API_URL || "http://api:8000";

const HEADERS = {
  "X-Api-Key": "key_test_abc123",
  "X-Api-Secret": "secret_test_xyz789",
};

function Transactions() {
  const [orders, setOrders] = useState([]);
  const [payments, setPayments] = useState([]);
  const [error, setError] = useState(false);
  const openCheckout = (orderId) => {
    window.open(`http://localhost:3001/checkout/${orderId}`, "_blank");
  };


  useEffect(() => {
    Promise.all([
      fetch(`${API_URL}/api/v1/orders`, { headers: HEADERS }).then(r => {
        if (!r.ok) throw new Error("Orders API failed");
        return r.json();
      }),
      fetch(`${API_URL}/api/v1/payments`, { headers: HEADERS }).then(r => {
        if (!r.ok) throw new Error("Payments API failed");
        return r.json();
      }),
    ])
      .then(([ordersRes, paymentsRes]) => {
        console.log("ORDERS RAW:", ordersRes);
        console.log("PAYMENTS RAW:", paymentsRes);
        setOrders(ordersRes || []);      // use the raw array
        setPayments(paymentsRes || []);  // use the raw array
      })
      .catch(err => {
        console.error("API fetch failed:", err);
        setError(true);
      });
  }, []);

  if (error) return <p style={{ color: "red" }}>Failed to load transactions</p>;

  return (
    <div style={{ padding: "40px", fontFamily: "Arial" }}>
      <h2>Transactions</h2>

      <h3>Orders</h3>
      <table border="1" cellPadding="10" width="100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>Amount</th>
            <th>Currency</th>
            <th>Status</th>
            <th>Created At</th>
          </tr>
        </thead>
        <tbody>
          {orders.length === 0 ? (
            <tr><td colSpan="6">No orders found</td></tr>
          ) : orders.map(o => (
            <tr key={o.id}>
              <td>{o.id}</td>
              <td>{o.amount}</td>
              <td>{o.currency}</td>
              <td>{o.status}</td>
              <td>{new Date(o.createdAt || o.created_at).toLocaleString()}</td>

              {/* ✅ PAY BUTTON COLUMN */}
              <td>
                {o.status === "created" && (
                  <button onClick={() => openCheckout(o.id)}>
                    Pay
                  </button>
                )}
              </td>
            </tr>
          ))}

        </tbody>
      </table>

      <h3 style={{ marginTop: "40px" }}>Payments</h3>
      <table border="1" cellPadding="10" width="100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>Order ID</th>
            <th>Method</th>
            <th>Status</th>
            <th>Amount</th>
            <th>Created At</th>
          </tr>
        </thead>
        <tbody>
          {payments.length === 0 ? (
            <tr><td colSpan="6">No payments found</td></tr>
          ) : payments.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.order_id || p.orderId}</td>
              <td>{p.method}</td>
              <td>{p.status}</td>
              <td>{p.amount}</td>
              <td>{new Date(p.createdAt || p.created_at).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Transactions;
