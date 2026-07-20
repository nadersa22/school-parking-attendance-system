import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

interface AttendanceResponse {
  id: number;
  teacherName: string;
  teacherEmail: string;
  spotNumber: string;
  floor: string;
  checkInTime: string;
  checkOutTime: string | null;
  durationMinutes: number;
  status: "VALID" | "INVALID" | "IN_PROGRESS";
}

function TeacherAttendancePage() {
  const navigate = useNavigate();

  const [attendance, setAttendance] = useState<AttendanceResponse[]>([]);
  const [message, setMessage] = useState("");

  const loadAttendance = async () => {
    try {
      const response = await api.get<AttendanceResponse[]>("/attendance/me");
      setAttendance(response.data);
    } catch (err) {
      console.error(err);
      setMessage("Could not load attendance.");
    }
  };

  useEffect(() => {
    loadAttendance();
  }, []);

  const formatDateTime = (value: string | null) => {
    if (!value) {
      return "Still inside";
    }

    return new Date(value).toLocaleString();
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1>My Attendance</h1>
          <p>View your check-in and check-out history.</p>
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
              <th style={styles.th}>Check In</th>
              <th style={styles.th}>Check Out</th>
              <th style={styles.th}>Duration</th>
              <th style={styles.th}>Status</th>
            </tr>
          </thead>

          <tbody>
            {attendance.map((item) => (
              <tr key={item.id}>
                <td style={styles.td}>{item.id}</td>
                <td style={styles.td}>{item.spotNumber}</td>
                <td style={styles.td}>{item.floor}</td>
                <td style={styles.td}>{formatDateTime(item.checkInTime)}</td>
                <td style={styles.td}>{formatDateTime(item.checkOutTime)}</td>
                <td style={styles.td}>{item.durationMinutes} min</td>
                <td style={styles.td}>{item.status}</td>
              </tr>
            ))}
          </tbody>
        </table>

        {attendance.length === 0 && <p>No attendance records found.</p>}
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

export default TeacherAttendancePage;