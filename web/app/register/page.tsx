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
        <form onSubmit={submit} className="w-full max-w-md bg-white p-8 rounded shadow">
          <h2 className="text-2xl font-semibold mb-4">Register</h2>
          {error && <div className="text-red-600 mb-2">{String(error)}</div>}
          <div className="grid gap-3">
            <input placeholder="First name" value={firstName} onChange={e=>setFirstName(e.target.value)} className="input" />
            <input placeholder="Last name" value={lastName} onChange={e=>setLastName(e.target.value)} className="input" />
            <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} className="input" />
            <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} className="input" />
            <select value={gender} onChange={e=>setGender(e.target.value)} className="input">
              <option value="">Select gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="other">Other</option>
            </select>
            <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded">Register</button>
          </div>
        </form>
      </main>
    </div>
  );
}
