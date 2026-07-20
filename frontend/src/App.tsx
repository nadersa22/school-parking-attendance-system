import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage";

import AdminDashboard from "./pages/AdminDashboard";
import AdminUsersPage from "./pages/AdminUsersPage";
import AdminParkingSpotsPage from "./pages/AdminParkingSpotsPage";
import AdminAttendancePage from "./pages/AdminAttendancePage";

import TeacherDashboard from "./pages/TeacherDashboard";
import TeacherParkingPage from "./pages/TeacherParkingPage";
import TeacherReservationsPage from "./pages/TeacherReservationsPage";
import TeacherAttendancePage from "./pages/TeacherAttendancePage";

import ProtectedRoute from "./routes/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />

        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/users"
          element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminUsersPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/parking-spots"
          element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminParkingSpotsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/attendance"
          element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminAttendancePage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/teacher"
          element={
            <ProtectedRoute allowedRoles={["TEACHER"]}>
              <TeacherDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/teacher/parking"
          element={
            <ProtectedRoute allowedRoles={["TEACHER"]}>
              <TeacherParkingPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/teacher/reservations"
          element={
            <ProtectedRoute allowedRoles={["TEACHER"]}>
              <TeacherReservationsPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/teacher/attendance"
          element={
            <ProtectedRoute allowedRoles={["TEACHER"]}>
              <TeacherAttendancePage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;