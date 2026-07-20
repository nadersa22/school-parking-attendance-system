export type Role = "ADMIN" | "TEACHER";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export interface MeResponse {
  id: number;
  name: string;
  email: string;
  role: Role;
}