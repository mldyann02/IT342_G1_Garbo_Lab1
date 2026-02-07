"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import NavBar from "../components/NavBar";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      const res = await api.post("/api/auth/login", { email, password });
      const token = res.data?.token;
      if (token) {
        localStorage.setItem("token", token);
        router.push("/dashboard");
      }
    } catch (err: any) {
      setError(err?.response?.data || err.message || "Login failed");
    }
  };

  return (
    <div>
      <NavBar />
      <main className="min-h-screen flex items-center justify-center bg-gray-50">
        <form onSubmit={submit} className="w-full max-w-md bg-white p-8 rounded shadow">
          <h2 className="text-2xl font-semibold mb-4">Login</h2>
          {error && <div className="text-red-600 mb-2">{String(error)}</div>}
          <div className="grid gap-3">
            <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} className="input" />
            <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} className="input" />
            <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded">Login</button>
          </div>
        </form>
      </main>
    </div>
  );
}
