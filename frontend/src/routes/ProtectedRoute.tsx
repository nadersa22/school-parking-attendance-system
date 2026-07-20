import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";
import type { MeResponse, Role } from "../types/auth";

interface ProtectedRouteProps {
  children: ReactNode;
  allowedRoles: Role[];
}

function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const token = localStorage.getItem("token");
  const userData = localStorage.getItem("user");

  if (!token || !userData) {
    return <Navigate to="/" replace />;
  }

  const user: MeResponse = JSON.parse(userData);

  if (!allowedRoles.includes(user.role)) {
    if (user.role === "ADMIN") {
      return <Navigate to="/admin" replace />;
    }

    if (user.role === "TEACHER") {
      return <Navigate to="/teacher" replace />;
    }

    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}

export default ProtectedRoute;