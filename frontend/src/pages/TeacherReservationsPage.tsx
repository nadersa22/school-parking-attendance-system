import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

interface ReservationResponse {
  id: number;
  teacherName: string;
  teacherEmail: string;
  spotNumber: string;
  floor: string;
  reservedAt: string;
  expiresAt: string;
  status: "ACTIVE" | "EXPIRED" | "CANCELLED" | "COMPLETED";
}

function TeacherReservationsPage() {
  const navigate = useNavigate();

  const [reservations, setReservations] = useState<ReservationResponse[]>([]);
  const [message, setMessage] = useState("");

  const loadReservations = async () => {
    try {
      const response = await api.get<ReservationResponse[]>("/reservations/me");
      setReservations(response.data);
    } catch (err) {
      console.error(err);
      setMessage("Could not load reservations.");
    }
  };

  useEffect(() => {
    loadReservations();
  }, []);

  const formatDateTime = (value: string) => {
    return new Date(value).toLocaleString();
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1>My Reservations</h1>
          <p>View your parking reservation history.</p>
        </div>

        <button style={styles.backButton} onClick={() => navigate("/teacher")}>
          Back to Dashboard
        </button>
      </div>

      {message && <p style={styles.message}>{message}</p>}

      <div style={styles.section}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Spot</th>
              <th style={styles.th}>Floor</th>
              <th style={styles.th}>Reserved At</th>
              <th style={styles.th}>Expires At</th>
              <th style={styles.th}>Status</th>
            </tr>
          </thead>

          <tbody>
            {reservations.map((reservation) => (
              <tr key={reservation.id}>
                <td style={styles.td}>{reservation.id}</td>
                <td style={styles.td}>{reservation.spotNumber}</td>
                <td style={styles.td}>{reservation.floor}</td>
                <td style={styles.td}>{formatDateTime(reservation.reservedAt)}</td>
                <td style={styles.td}>{formatDateTime(reservation.expiresAt)}</td>
                <td style={styles.td}>{reservation.status}</td>
              </tr>
            ))}
          </tbody>
        </table>

        {reservations.length === 0 && <p>No reservations found.</p>}
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
    background: "white",
    padding: "20px",
    borderRadius: "12px",
    marginBottom: "25px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  backButton: {
    padding: "10px 16px",
    background: "#374151",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  section: {
    background: "white",
    padding: "20px",
    borderRadius: "12px",
    overflowX: "auto",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    minWidth: "800px",
  },
  th: {
    borderBottom: "1px solid #ddd",
    padding: "12px",
    textAlign: "left",
    background: "#f9fafb",
  },
  td: {
    borderBottom: "1px solid #eee",
    padding: "12px",
  },
  message: {
    background: "#fee2e2",
    color: "#991b1b",
    padding: "12px",
    borderRadius: "8px",
    marginBottom: "20px",
  },
};

export default TeacherReservationsPage;