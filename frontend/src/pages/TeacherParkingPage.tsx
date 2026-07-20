import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

interface ParkingSpotResponse {
  id: number;
  spotNumber: string;
  floor: string;
  status: "AVAILABLE" | "RESERVED" | "OCCUPIED";
}

function TeacherParkingPage() {
  const navigate = useNavigate();

  const [spots, setSpots] = useState<ParkingSpotResponse[]>([]);
  const [message, setMessage] = useState("");

  const loadSpots = async () => {
    try {
      const response = await api.get<ParkingSpotResponse[]>("/parking-spots");
      setSpots(response.data);
    } catch (err) {
      console.error(err);
      setMessage("Could not load parking spots.");
    }
  };

  useEffect(() => {
    loadSpots();
  }, []);

  const reserveSpot = async (spotId: number) => {
    try {
      setMessage("");

      await api.post("/reservations/me", {
        spotId,
      });

      setMessage("Parking spot reserved successfully.");
      loadSpots();
    } catch (err) {
      console.error(err);
      setMessage("Could not reserve this spot.");
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1>Parking Spots</h1>
          <p>Choose an available spot and reserve it.</p>
        </div>

        <button style={styles.backButton} onClick={() => navigate("/teacher")}>
          Back to Dashboard
        </button>
      </div>

      {message && <p style={styles.message}>{message}</p>}

      <div style={styles.grid}>
        {spots.map((spot) => (
          <div key={spot.id} style={styles.card}>
            <h2>Spot {spot.spotNumber}</h2>
            <p>Floor: {spot.floor}</p>
            <p>
              Status:{" "}
              <strong
                style={{
                  color:
                    spot.status === "AVAILABLE"
                      ? "#16a34a"
                      : spot.status === "RESERVED"
                      ? "#ea580c"
                      : "#dc2626",
                }}
              >
                {spot.status}
              </strong>
            </p>

            {spot.status === "AVAILABLE" ? (
              <button
                style={styles.reserveButton}
                onClick={() => reserveSpot(spot.id)}
              >
                Reserve
              </button>
            ) : (
              <button style={styles.disabledButton} disabled>
                Not Available
              </button>
            )}
          </div>
        ))}
      </div>

      {spots.length === 0 && <p>No parking spots found.</p>}
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
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "white",
    padding: "22px",
    borderRadius: "12px",
    boxShadow: "0 5px 15px rgba(0,0,0,0.08)",
  },
  reserveButton: {
    padding: "10px 16px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  disabledButton: {
    padding: "10px 16px",
    background: "#9ca3af",
    color: "white",
    border: "none",
    borderRadius: "8px",
  },
  message: {
    background: "#dbeafe",
    color: "#1e40af",
    padding: "12px",
    borderRadius: "8px",
    marginBottom: "20px",
  },
};

export default TeacherParkingPage;