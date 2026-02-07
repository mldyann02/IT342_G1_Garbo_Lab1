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
        <form onSubmit={submit} className="w-full max-w-md bg-[color:var(--surface)] p-8 rounded-lg shadow-sm border border-gray-100">
          <h2 className="text-3xl heading mb-2">Welcome back</h2>
          <p className="muted text-sm mb-4">Sign in to your account to continue</p>
          {error && <div className="bg-red-50 border border-red-100 text-red-700 px-3 py-2 rounded mb-3">{String(error)}</div>}
          <div className="grid gap-4">
            <label className="text-sm text-gray-700">Email
              <input placeholder="you@company.com" value={email} onChange={e=>setEmail(e.target.value)} className="input mt-1 rounded-lg" />
            </label>

            <label className="text-sm text-gray-700">Password
              <input placeholder="Your password" type="password" value={password} onChange={e=>setPassword(e.target.value)} className="input mt-1 rounded-lg" />
            </label>

            <button type="submit" className="btn-primary w-full">Sign in</button>

            <div className="text-center text-sm muted">Don\'t have an account? <a href="/register" className="text-[color:var(--primary)] font-medium">Create account</a></div>
          </div>
        </form>
      </main>
    </div>
  );
}
