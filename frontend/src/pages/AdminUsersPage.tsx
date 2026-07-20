import { useEffect, useState } from "react";

import api from "../api/api";
import AdminLayout from "../components/AdminLayout";
interface UserResponse {
  id: number;
  name: string;
  email: string;
  role: "ADMIN" | "TEACHER";
}

function AdminUsersPage() {
  

  const [users, setUsers] = useState<UserResponse[]>([]);
  const [teachers, setTeachers] = useState<UserResponse[]>([]);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("123456");

  const [message, setMessage] = useState("");

  const loadData = async () => {
    try {
      const usersResponse = await api.get<UserResponse[]>("/users");
      setUsers(usersResponse.data);

      const teachersResponse = await api.get<UserResponse[]>("/users/teachers");
      setTeachers(teachersResponse.data);
    } catch (err) {
      console.error(err);
      setMessage("Could not load users. Please login as admin.");
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const addTeacher = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      setMessage("");

      await api.post("/users/teachers", {
        name,
        email,
        password,
      });

      setMessage("Teacher added successfully.");

      setName("");
      setEmail("");
      setPassword("123456");

      loadData();
    } catch (err) {
      console.error(err);
      setMessage("Could not add teacher.");
    }
  };

  const deleteUser = async (id: number) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this user?");

    if (!confirmDelete) {
      return;
    }

    try {
      setMessage("");

      await api.delete(`/users/${id}`);

      setMessage("User deleted successfully.");

      loadData();
    } catch (err) {
      console.error(err);
      setMessage("Could not delete user.");
    }
  };

  return (
  <AdminLayout
    title="Manage Users"
    subtitle="Add teachers and view all system users."
  >
      {message && <p style={styles.message}>{message}</p>}

      <div style={styles.section}>
        <h2>Add Teacher</h2>

        <form onSubmit={addTeacher} style={styles.form}>
          <input
            style={styles.input}
            type="text"
            placeholder="Teacher name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />

          <input
            style={styles.input}
            type="email"
            placeholder="Teacher email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            style={styles.input}
            type="text"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button style={styles.addButton} type="submit">
            Add Teacher
          </button>
        </form>
      </div>

      <div style={styles.section}>
        <h2>Teachers</h2>

        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Name</th>
              <th style={styles.th}>Email</th>
              <th style={styles.th}>Role</th>
              <th style={styles.th}>Action</th>
            </tr>
          </thead>

          <tbody>
            {teachers.map((teacher) => (
              <tr key={teacher.id}>
                <td style={styles.td}>{teacher.id}</td>
                <td style={styles.td}>{teacher.name}</td>
                <td style={styles.td}>{teacher.email}</td>
                <td style={styles.td}>{teacher.role}</td>
                <td style={styles.td}>
                  <button
                    style={styles.deleteButton}
                    onClick={() => deleteUser(teacher.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div style={styles.section}>
        <h2>All Users</h2>

        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Name</th>
              <th style={styles.th}>Email</th>
              <th style={styles.th}>Role</th>
            </tr>
          </thead>

          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td style={styles.td}>{user.id}</td>
                <td style={styles.td}>{user.name}</td>
                <td style={styles.td}>{user.email}</td>
                <td style={styles.td}>{user.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      </AdminLayout>
);
}

const styles: Record<string, React.CSSProperties> = {
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
  addButton: {
    padding: "10px 16px",
    background: "#2563eb",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
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

export default AdminUsersPage;