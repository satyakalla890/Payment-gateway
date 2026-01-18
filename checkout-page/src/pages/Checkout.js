import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL || "http://api:8000";

const AUTH_HEADERS = {
  "Content-Type": "application/json",
  "X-Api-Key": "key_test_abc123",
  "X-Api-Secret": "secret_test_xyz789",
};

function Checkout() {
  const { order_id } = useParams();

  const [order, setOrder] = useState(null);
  const [method, setMethod] = useState(null);

  const [upiId, setUpiId] = useState("");
  const [card, setCard] = useState({ number: "", expiry: "", cvv: "" });

  const [paymentId, setPaymentId] = useState(null);
  const [status, setStatus] = useState("idle"); // idle | processing | success | failed
  const [error, setError] = useState(null);

  // 🔹 Fetch order
  useEffect(() => {
    fetch(`${API_URL}/api/v1/orders/${order_id}`, {
      headers: AUTH_HEADERS,
    })
      .then(res => {
        if (!res.ok) throw new Error("Order fetch failed");
        return res.json();
      })
      .then(setOrder)
      .catch(() => {
        setError("Failed to load order");
        setStatus("failed");
      });
  }, [order_id]);

  // 🔹 Poll payment status every 2s
  useEffect(() => {
    if (!paymentId) return;

    const interval = setInterval(() => {
      fetch(`${API_URL}/api/v1/payments/${paymentId}`, {
        headers: AUTH_HEADERS,
      })
        .then(res => {
          if (!res.ok) throw new Error("Status fetch failed");
          return res.json();
        })
        .then(data => {
          if (data.status === "success") {
            setStatus("success");
            clearInterval(interval);
          }
          if (data.status === "failed") {
            setStatus("failed");
            clearInterval(interval);
          }
        })
        .catch(() => {
          setStatus("failed");
          clearInterval(interval);
        });
    }, 2000);

    return () => clearInterval(interval);
  }, [paymentId]);

  // 🔹 Submit payment
  const submitPayment = () => {
    setError(null);

    if (!method) {
      setError("Select payment method");
      return;
    }

    if (method === "upi" && !upiId) {
      setError("Enter UPI ID");
      return;
    }

    if (method === "card") {
      if (!card.number || !card.expiry || !card.cvv) {
        setError("Enter complete card details");
        return;
      }
      if (!card.expiry.includes("/")) {
        setError("Expiry must be MM/YY");
        return;
      }
    }

    setStatus("processing");

    const [expiry_month, expiry_year] =
      method === "card" ? card.expiry.split("/") : [];

    fetch(`${API_URL}/api/v1/payments`, {
      method: "POST",
      headers: AUTH_HEADERS,
      body: JSON.stringify({
        order_id,
        method,
        vpa: method === "upi" ? upiId : null,
        card:
          method === "card"
            ? {
                number: card.number,
                expiry_month,
                expiry_year,
                cvv: card.cvv,
              }
            : null,
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error("Payment failed");
        return res.json();
      })
      .then(data => setPaymentId(data.id))
      .catch(() => {
        setStatus("failed");
        setError("Payment failed");
      });
  };

  if (!order) return <p>Loading order...</p>;

  return (
    <div style={{ padding: "40px", fontFamily: "Arial" }}>
      <h2>Checkout</h2>

      <p><b>Order ID:</b> {order.id}</p>
      <p><b>Amount:</b> {order.amount} {order.currency}</p>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {status === "idle" && (
        <>
          <h3>Select Payment Method</h3>

          <button onClick={() => setMethod("upi")}>UPI</button>
          <button onClick={() => setMethod("card")} style={{ marginLeft: 10 }}>
            Card
          </button>

          {method === "upi" && (
            <div>
              <input
                placeholder="example@upi"
                value={upiId}
                onChange={e => setUpiId(e.target.value)}
              />
            </div>
          )}

          {method === "card" && (
            <div>
              <input
                placeholder="Card Number"
                value={card.number}
                onChange={e => setCard({ ...card, number: e.target.value })}
              />
              <input
                placeholder="MM/YY"
                value={card.expiry}
                onChange={e => setCard({ ...card, expiry: e.target.value })}
              />
              <input
                placeholder="CVV"
                type="password"
                value={card.cvv}
                onChange={e => setCard({ ...card, cvv: e.target.value })}
              />
            </div>
          )}

          {method && (
            <button onClick={submitPayment} style={{ marginTop: 20 }}>
              Pay Now
            </button>
          )}
        </>
      )}

      {status === "processing" && <p>⏳ Processing payment...</p>}
      {status === "success" && <p style={{ color: "green" }}>✅ Payment Successful</p>}
      {status === "failed" && (
        <>
          <p style={{ color: "red" }}>❌ Payment Failed</p>
          <button onClick={() => {
            setStatus("idle");
            setPaymentId(null);
            setError(null);
          }}>
            Retry
          </button>
        </>
      )}
    </div>
  );
}

export default Checkout;
