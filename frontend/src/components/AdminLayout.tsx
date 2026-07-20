import { useNavigate } from "react-router-dom";
import type { ReactNode } from "react";

interface AdminLayoutProps {
  title: string;
  subtitle?: string;
  children: ReactNode;
}

function AdminLayout({ title, subtitle, children }: AdminLayoutProps) {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
  };

  return (
    <div style={styles.page}>
      <div style={styles.navbar}>
        <div>
          <h2 style={styles.logo}>School Parking Admin</h2>
        </div>

        <div style={styles.navLinks}>
          <button style={styles.navButton} onClick={() => navigate("/admin")}>
            Dashboard
          </button>

          <button style={styles.navButton} onClick={() => navigate("/admin/users")}>
            Users
          </button>

          <button
            style={styles.navButton}
            onClick={() => navigate("/admin/parking-spots")}
          >
            Parking Spots
          </button>

          <button
            style={styles.navButton}
            onClick={() => navigate("/admin/attendance")}
          >
            Attendance
          </button>

          <button style={styles.logoutButton} onClick={logout}>
            Logout
          </button>
        </div>
      </div>

      <div style={styles.header}>
        <div>
          <h1>{title}</h1>
          {subtitle && <p>{subtitle}</p>}
        </div>
      </div>

      {children}
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
  navbar: {
    background: "white",
    padding: "16px 20px",
    borderRadius: "12px",
    marginBottom: "20px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: "15px",
    flexWrap: "wrap",
  },
  logo: {
    margin: 0,
  },
  navLinks: {
    display: "flex",
    gap: "10px",
    flexWrap: "wrap",
  },
  navButton: {
    padding: "9px 13px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  logoutButton: {
    padding: "9px 13px",
    background: "#dc2626",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  header: {
    background: "white",
    padding: "20px",
    borderRadius: "12px",
    marginBottom: "25px",
  },
};

export default AdminLayout;