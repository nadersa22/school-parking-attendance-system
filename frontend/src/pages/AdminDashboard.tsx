import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import type { MeResponse } from "../types/auth";
import AdminLayout from "../components/AdminLayout";

interface DailyReportResponse {
  totalAttendanceToday: number;
  validAttendanceToday: number;
  invalidAttendanceToday: number;
  teachersCurrentlyInside: number;
  availableSpots: number;
  reservedSpots: number;
  occupiedSpots: number;
}

function AdminDashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState<MeResponse | null>(null);
  const [report, setReport] = useState<DailyReportResponse | null>(null);
  const [error, setError] = useState("");

  const loadDashboard = async () => {
    try {
      const meResponse = await api.get<MeResponse>("/auth/me");

      if (meResponse.data.role !== "ADMIN") {
        navigate("/teacher");
        return;
      }

      setUser(meResponse.data);

      const reportResponse = await api.get<DailyReportResponse>("/reports/daily");
      setReport(reportResponse.data);
    } catch (err) {
      console.error(err);
      setError("You are not authorized. Please login again.");
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      navigate("/");
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  return (
    <AdminLayout
      title="Admin Dashboard"
      subtitle={`Welcome, ${user?.name || "Admin"}`}
    >
      {error && <p style={styles.error}>{error}</p>}

      <h2>Daily Report</h2>

      {report ? (
        <div style={styles.grid}>
          <div style={styles.card}>
            <h3>Total Attendance Today</h3>
            <p>{report.totalAttendanceToday}</p>
          </div>

          <div style={styles.card}>
            <h3>Valid Attendance</h3>
            <p>{report.validAttendanceToday}</p>
          </div>

          <div style={styles.card}>
            <h3>Invalid Attendance</h3>
            <p>{report.invalidAttendanceToday}</p>
          </div>

          <div style={styles.card}>
            <h3>Teachers Inside</h3>
            <p>{report.teachersCurrentlyInside}</p>
          </div>

          <div style={styles.card}>
            <h3>Available Spots</h3>
            <p>{report.availableSpots}</p>
          </div>

          <div style={styles.card}>
            <h3>Reserved Spots</h3>
            <p>{report.reservedSpots}</p>
          </div>

          <div style={styles.card}>
            <h3>Occupied Spots</h3>
            <p>{report.occupiedSpots}</p>
          </div>
        </div>
      ) : (
        <p>Loading report...</p>
      )}
    </AdminLayout>
  );
}

const styles: Record<string, React.CSSProperties> = {
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "white",
    padding: "20px",
    borderRadius: "12px",
    boxShadow: "0 5px 15px rgba(0,0,0,0.08)",
  },
  error: {
    color: "red",
  },
};

export default AdminDashboard;