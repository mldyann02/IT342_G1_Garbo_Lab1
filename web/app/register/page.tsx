"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import NavBar from "../components/NavBar";

export default function RegisterPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [gender, setGender] = useState("");
  const [error, setError] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      const payload = { email, password, firstName, lastName, gender };
      const res = await api.post("/api/auth/register", payload);
      const token = res.data?.token;
      if (token) {
        localStorage.setItem("token", token);
        router.push("/dashboard");
      }
    } catch (err: any) {
      setError(err?.response?.data || err.message || "Registration failed");
    }
  };

  return (
    <div>
      <NavBar />
      <main className="min-h-screen flex items-center justify-center bg-gray-50">
        <form onSubmit={submit} className="w-full max-w-md bg-[color:var(--surface)] p-8 rounded-lg shadow-sm border border-gray-100">
          <h2 className="text-3xl heading mb-2">Create account</h2>
          <p className="muted text-sm mb-4">Start your free account — it only takes a minute.</p>
          {error && <div className="bg-red-50 border border-red-100 text-red-700 px-3 py-2 rounded mb-3">{String(error)}</div>}
          <div className="grid gap-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <label className="text-sm text-gray-700">First name
                <input placeholder="First" value={firstName} onChange={e=>setFirstName(e.target.value)} className="input mt-1 rounded-lg" />
              </label>

              <label className="text-sm text-gray-700">Last name
                <input placeholder="Last" value={lastName} onChange={e=>setLastName(e.target.value)} className="input mt-1 rounded-lg" />
              </label>
            </div>

            <label className="text-sm text-gray-700">Email
              <input placeholder="you@company.com" value={email} onChange={e=>setEmail(e.target.value)} className="input mt-1 rounded-lg" />
            </label>

            <label className="text-sm text-gray-700">Password
              <input placeholder="Create a password" type="password" value={password} onChange={e=>setPassword(e.target.value)} className="input mt-1 rounded-lg" />
            </label>

            <label className="text-sm text-gray-700">Gender
              <select value={gender} onChange={e=>setGender(e.target.value)} className="input mt-1 rounded-lg">
                <option value="">Select gender</option>
                <option value="male">Male</option>
                <option value="female">Female</option>
                <option value="other">Other</option>
              </select>
            </label>

            <button type="submit" className="btn-primary w-full">Create account</button>

            <div className="text-center text-sm muted">Already have an account? <a href="/login" className="text-[color:var(--primary)] font-medium">Sign in</a></div>
          </div>
        </form>
      </main>
    </div>
  );
}
