import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import type { MeResponse } from "../types/auth";

function TeacherDashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState<MeResponse | null>(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    let cancelled = false;

    api
      .get<MeResponse>("/auth/me")
      .then((response) => {
        if (cancelled) {
          return;
        }

        if (response.data.role !== "TEACHER") {
          navigate("/admin");
          return;
        }

        setUser(response.data);
      })
      .catch((err) => {
        if (!cancelled) {
          console.error(err);
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          navigate("/");
        }
      });

    return () => {
      cancelled = true;
    };
  }, [navigate]);

  const checkIn = async () => {
    try {
      setMessage("");

      await api.post("/attendance/me/check-in");

      setMessage("Checked in successfully.");
    } catch (err) {
      console.error(err);
      setMessage("Could not check in. Make sure you reserved a spot first.");
    }
  };

  const checkOut = async () => {
    try {
      setMessage("");

      await api.post("/attendance/me/check-out");

      setMessage("Checked out successfully.");
    } catch (err) {
      console.error(err);
      setMessage("Could not check out. Maybe you are not checked in.");
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1>Teacher Dashboard</h1>
          <p>Welcome, {user?.name}</p>
        </div>

        <button style={styles.logoutButton} onClick={logout}>
          Logout
        </button>
      </div>

      {message && <p style={styles.message}>{message}</p>}

      <div style={styles.grid}>
        <div style={styles.card}>
          <h2>Parking Spots</h2>
          <p>View available parking spots and reserve one.</p>
          <button
            style={styles.blueButton}
            onClick={() => navigate("/teacher/parking")}
          >
            View Parking Spots
          </button>
        </div>

        <div style={styles.card}>
          <h2>My Reservations</h2>
          <p>View your reservation history.</p>
          <button
            style={styles.blueButton}
            onClick={() => navigate("/teacher/reservations")}
          >
            View Reservations
          </button>
        </div>

        <div style={styles.card}>
          <h2>My Attendance</h2>
          <p>View your check-in and check-out records.</p>
          <button
            style={styles.blueButton}
            onClick={() => navigate("/teacher/attendance")}
          >
            View Attendance
          </button>
        </div>

        <div style={styles.card}>
          <h2>Check In</h2>
          <p>Check in when you arrive at school.</p>
          <button style={styles.greenButton} onClick={checkIn}>
            Check In
          </button>
        </div>

        <div style={styles.card}>
          <h2>Check Out</h2>
          <p>Check out when you leave school.</p>
          <button style={styles.orangeButton} onClick={checkOut}>
            Check Out
          </button>
        </div>
      </div>
    </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    padding: "30px",
    background: "#f3f4f6",
    fontFamily: "Arial, sans-serif",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    background: "white",
    padding: "20px",
    borderRadius: "12px",
    marginBottom: "25px",
  },
  logoutButton: {
    padding: "10px 16px",
    background: "#dc2626",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(260px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "white",
    padding: "24px",
    borderRadius: "12px",
    boxShadow: "0 5px 15px rgba(0,0,0,0.08)",
  },
  blueButton: {
    padding: "12px 18px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  greenButton: {
    padding: "12px 18px",
    background: "#16a34a",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  orangeButton: {
    padding: "12px 18px",
    background: "#ea580c",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  message: {
    background: "#dbeafe",
    padding: "12px",
    borderRadius: "8px",
    color: "#1e40af",
    marginBottom: "20px",
  },
};

export default TeacherDashboard;