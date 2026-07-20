import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

interface ParkingSpotResponse {
  id: number;
  spotNumber: string;
  floor: string;
  status: "AVAILABLE" | "RESERVED" | "OCCUPIED";
}

function AdminParkingSpotsPage() {
  const navigate = useNavigate();

  const [spots, setSpots] = useState<ParkingSpotResponse[]>([]);

  const [spotNumber, setSpotNumber] = useState("");
  const [floor, setFloor] = useState("");

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editSpotNumber, setEditSpotNumber] = useState("");
  const [editFloor, setEditFloor] = useState("");

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

  const addSpot = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      setMessage("");

      await api.post("/parking-spots", {
        spotNumber,
        floor,
      });

      setMessage("Parking spot added successfully.");

      setSpotNumber("");
      setFloor("");

      loadSpots();
    } catch (err) {
      console.error(err);
      setMessage("Could not add parking spot.");
    }
  };

  const startEdit = (spot: ParkingSpotResponse) => {
    setEditingId(spot.id);
    setEditSpotNumber(spot.spotNumber);
    setEditFloor(spot.floor);
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditSpotNumber("");
    setEditFloor("");
  };

  const updateSpot = async (id: number) => {
    try {
      setMessage("");

      await api.put(`/parking-spots/${id}`, {
        spotNumber: editSpotNumber,
        floor: editFloor,
      });

      setMessage("Parking spot updated successfully.");

      cancelEdit();
      loadSpots();
    } catch (err) {
      console.error(err);
      setMessage("Could not update parking spot.");
    }
  };

  const deleteSpot = async (id: number) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this parking spot?"
    );

    if (!confirmDelete) {
      return;
    }

    try {
      setMessage("");

      await api.delete(`/parking-spots/${id}`);

      setMessage("Parking spot deleted successfully.");

      loadSpots();
    } catch (err) {
      console.error(err);
      setMessage("Could not delete parking spot.");
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1>Manage Parking Spots</h1>
          <p>Add, update, and delete school parking spots.</p>
        </div>

        <button style={styles.backButton} onClick={() => navigate("/admin")}>
          Back to Dashboard
        </button>
      </div>

      {message && <p style={styles.message}>{message}</p>}

      <div style={styles.section}>
        <h2>Add Parking Spot</h2>

        <form onSubmit={addSpot} style={styles.form}>
          <input
            style={styles.input}
            type="text"
            placeholder="Spot number, example: A1"
            value={spotNumber}
            onChange={(e) => setSpotNumber(e.target.value)}
            required
          />

          <input
            style={styles.input}
            type="text"
            placeholder="Floor, example: B1"
            value={floor}
            onChange={(e) => setFloor(e.target.value)}
            required
          />

          <button style={styles.addButton} type="submit">
            Add Spot
          </button>
        </form>
      </div>

      <div style={styles.section}>
        <h2>All Parking Spots</h2>

        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Spot Number</th>
              <th style={styles.th}>Floor</th>
              <th style={styles.th}>Status</th>
              <th style={styles.th}>Actions</th>
            </tr>
          </thead>

          <tbody>
            {spots.map((spot) => (
              <tr key={spot.id}>
                <td style={styles.td}>{spot.id}</td>

                <td style={styles.td}>
                  {editingId === spot.id ? (
                    <input
                      style={styles.smallInput}
                      value={editSpotNumber}
                      onChange={(e) => setEditSpotNumber(e.target.value)}
                    />
                  ) : (
                    spot.spotNumber
                  )}
                </td>

                <td style={styles.td}>
                  {editingId === spot.id ? (
                    <input
                      style={styles.smallInput}
                      value={editFloor}
                      onChange={(e) => setEditFloor(e.target.value)}
                    />
                  ) : (
                    spot.floor
                  )}
                </td>

                <td style={styles.td}>{spot.status}</td>

                <td style={styles.td}>
                  {editingId === spot.id ? (
                    <>
                      <button
                        style={styles.saveButton}
                        onClick={() => updateSpot(spot.id)}
                      >
                        Save
                      </button>

                      <button style={styles.cancelButton} onClick={cancelEdit}>
                        Cancel
                      </button>
                    </>
                  ) : (
                    <>
                      <button
                        style={styles.editButton}
                        onClick={() => startEdit(spot)}
                      >
                        Edit
                      </button>

                      <button
                        style={styles.deleteButton}
                        onClick={() => deleteSpot(spot.id)}
                      >
                        Delete
                      </button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {spots.length === 0 && <p>No parking spots found.</p>}
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
    marginBottom: "25px",
  },
  form: {
    display: "flex",
    gap: "12px",
    flexWrap: "wrap",
  },
  input: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "8px",
    minWidth: "220px",
  },
  smallInput: {
    padding: "8px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    width: "120px",
  },
  addButton: {
    padding: "10px 16px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  editButton: {
    padding: "8px 12px",
    background: "#f59e0b",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    marginRight: "8px",
  },
  saveButton: {
    padding: "8px 12px",
    background: "#16a34a",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    marginRight: "8px",
  },
  cancelButton: {
    padding: "8px 12px",
    background: "#6b7280",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    marginRight: "8px",
  },
  deleteButton: {
    padding: "8px 12px",
    background: "#dc2626",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
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
    background: "#dbeafe",
    color: "#1e40af",
    padding: "12px",
    borderRadius: "8px",
    marginBottom: "20px",
  },
};

export default AdminParkingSpotsPage;