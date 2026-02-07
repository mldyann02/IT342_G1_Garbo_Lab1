"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import NavBar from "../components/NavBar";
import { Mail, Lock, Loader2, Eye, EyeOff } from "lucide-react";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false); // Toggle state
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await api.post("/api/auth/login", { email, password });
      if (res.data?.token) {
        localStorage.setItem("token", res.data.token);
        router.push("/dashboard");
      }
    } catch (err) {
      setError("Authentication failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F8F9FA]">
      <NavBar />
      <main className="flex items-center justify-center px-4 py-24 relative overflow-hidden">
        {/* Geometric Decorative Shapes from the palette */}
        <div className="absolute top-10 left-10 w-32 h-32 bg-[#F0A202] rounded-full opacity-20" />
        <div className="absolute bottom-10 right-10 w-48 h-24 bg-[#D95D39] rounded-t-full opacity-20" />

        <div className="w-full max-w-md bg-white border-2 border-[#202C59] p-10 shadow-[8px_8px_0px_0px_#202C59]">
          <h2 className="text-4xl font-black uppercase text-[#202C59] mb-2 tracking-tighter">
            Login
          </h2>
          {/* Welcome Message */}
          <p className="text-[#202C59] font-bold text-sm mb-8 leading-tight">
            Welcome back! Please enter your credentials.
          </p>

          {error && (
            <div className="mb-6 p-3 bg-[#581F18] text-white font-bold text-xs uppercase text-center animate-shake">
              {error}
            </div>
          )}

          <form onSubmit={submit} className="space-y-6">
            <div className="space-y-2">
              <label className="text-xs font-black text-[#202C59] uppercase tracking-widest">
                Email
              </label>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="input-academy"
                placeholder="your@email.com"
              />
            </div>

            <div className="space-y-2">
              <label className="text-xs font-black text-[#202C59] uppercase tracking-widest">
                Password
              </label>
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"} // Dynamic type
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="input-academy pr-12" // Padding for the icon
                  placeholder="••••••••"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-[#202C59] hover:text-[#D95D39] transition-colors"
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full btn-enroll flex items-center justify-center gap-2"
            >
              {loading ? <Loader2 className="animate-spin" /> : "LOG IN"}
            </button>
          </form>
        </div>
      </main>
    </div>
  );
}
