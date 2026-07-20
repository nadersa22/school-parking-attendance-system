import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import type { AuthResponse, MeResponse } from "../types/auth";

function LoginPage() {
  const navigate = useNavigate();
 useEffect(() => {
  const userData = localStorage.getItem("user");

  if (userData) {
    const user = JSON.parse(userData);

    if (user.role === "ADMIN") {
      navigate("/admin");
    } else if (user.role === "TEACHER") {
      navigate("/teacher");
    }
  }
}, [navigate]);
  const [email, setEmail] = useState("admin@school.com");
  const [password, setPassword] = useState("123456");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      setLoading(true);
      setError("");

      const loginResponse = await api.post<AuthResponse>("/auth/login", {
        email,
        password,
      });

      const token = loginResponse.data.token;

      localStorage.setItem("token", token);

      const meResponse = await api.get<MeResponse>("/auth/me");

      const user = meResponse.data;

      localStorage.setItem("user", JSON.stringify(user));

      if (user.role === "ADMIN") {
        navigate("/admin");
      } else if (user.role === "TEACHER") {
        navigate("/teacher");
      } else {
        setError("Unknown user role.");
      }
    } catch (err) {
      console.error(err);
      setError("Invalid email or password.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <h1 style={styles.title}>School Parking System</h1>
        <p style={styles.subtitle}>Login to continue</p>

        {error && <p style={styles.error}>{error}</p>}

        <form onSubmit={handleLogin} style={styles.form}>
          <div>
            <label style={styles.label}>Email</label>
            <input
              style={styles.input}
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter email"
            />
          </div>

          <div>
            <label style={styles.label}>Password</label>
            <input
              style={styles.input}
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
            />
          </div>

          <button style={styles.button} type="submit" disabled={loading}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "#f3f4f6",
    fontFamily: "Arial, sans-serif",
  },
  card: {
    width: "400px",
    background: "white",
    padding: "30px",
    borderRadius: "12px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
  },
  title: {
    marginBottom: "8px",
    textAlign: "center",
  },
  subtitle: {
    textAlign: "center",
    color: "#666",
    marginBottom: "20px",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "16px",
  },
  label: {
    display: "block",
    marginBottom: "6px",
    fontWeight: "bold",
  },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "8px",
    border: "1px solid #ccc",
    fontSize: "15px",
    boxSizing: "border-box",
  },
  button: {
    padding: "12px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    fontSize: "16px",
    cursor: "pointer",
  },
  error: {
    color: "red",
    textAlign: "center",
  },
};

export default LoginPage;