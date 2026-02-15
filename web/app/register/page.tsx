"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import NavBar from "../components/NavBar";
import {
  Mail,
  Lock,
  Loader2,
  Sparkles,
  Eye,
  EyeOff,
  Check,
  X,
} from "lucide-react";

export default function RegisterPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    gender: "",
  });
  const [showPass, setShowPass] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Strict Validation Logic
  const validation = {
    length: formData.password.length >= 8,
    upper: /[A-Z]/.test(formData.password),
    number: /[0-9]/.test(formData.password),
    special: /[!@#$%^&*(),.?":{}|<>]/.test(formData.password),
    match:
      formData.password === formData.confirmPassword &&
      formData.confirmPassword !== "",
  };

  const isFormValid = Object.values(validation).every(Boolean);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isFormValid) {
      setError("Please ensure the password meets all security requirements.");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      // Don't send confirmPassword to the backend
      const { confirmPassword, ...submitData } = formData;
      const res = await api.post("/api/auth/register", submitData);
      if (res.data?.token) {
        router.push("/login");
      }
    } catch (err: any) {
      const resp = err?.response;
      let msg = "Registration failed. Check your details.";

      if (resp) {
        if (resp.status === 409) {
          msg = "Email already exists. Cannot register.";
        } else {
          const data = resp.data;
          if (typeof data === "string") {
            if (/email.*exist/i.test(data)) {
              msg = "Email already exists. Cannot register.";
            } else {
              msg = data;
            }
          } else if (data?.message) {
            if (/email.*exist/i.test(data.message)) {
              msg = "Email already exists. Cannot register.";
            } else {
              msg = data.message;
            }
          }
        }
      }

      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F8F9FA]">
      <NavBar />
      <main className="flex items-center justify-center px-4 py-16 relative overflow-hidden">
        <div className="absolute top-20 right-10 w-40 h-40 bg-[#F18805] rounded-full opacity-10" />
        <div className="absolute top-1/2 left-5 w-24 h-24 bg-[#202C59] rotate-45 opacity-5" />

        <div className="w-full max-w-2xl bg-white border-2 border-[#202C59] p-8 md:p-12 shadow-[10px_10px_0px_0px_#202C59]">
          <header className="mb-10">
            <div className="inline-flex items-center gap-2 px-3 py-1 bg-[#F0A202] text-[#202C59] font-black text-[10px] uppercase tracking-widest mb-4">
              <Sparkles size={12} /> New Account
            </div>
            <h2 className="text-4xl font-black text-[#202C59] tracking-tighter uppercase">
              Register Account
            </h2>
          </header>

          {error && (
            <div className="mb-8 p-4 bg-[#581F18] text-white font-bold text-xs uppercase tracking-widest animate-shake">
              {error}
            </div>
          )}

          <form onSubmit={submit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  First Name
                </label>
                <input
                  name="firstName"
                  required
                  value={formData.firstName}
                  onChange={handleChange}
                  className="input-academy"
                  placeholder="Juan"
                />
              </div>
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  Last Name
                </label>
                <input
                  name="lastName"
                  required
                  value={formData.lastName}
                  onChange={handleChange}
                  className="input-academy"
                  placeholder="Dela Cruz"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  Email Address
                </label>
                <input
                  name="email"
                  type="email"
                  required
                  value={formData.email}
                  onChange={handleChange}
                  className="input-academy"
                  placeholder="name@example.com"
                />
              </div>
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  Gender
                </label>
                <select
                  name="gender"
                  required
                  value={formData.gender}
                  onChange={handleChange}
                  className="input-academy cursor-pointer bg-white"
                >
                  <option value="">Select Identity</option>
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="other">Other</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Password Field */}
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  Password
                </label>
                <div className="relative">
                  <input
                    name="password"
                    type={showPass ? "text" : "password"}
                    required
                    value={formData.password}
                    onChange={handleChange}
                    className="input-academy pr-10"
                    placeholder="••••••••"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPass(!showPass)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-[#202C59]"
                  >
                    {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>

              {/* Confirm Password Field */}
              <div className="space-y-1">
                <label className="text-[11px] font-black text-[#202C59] uppercase">
                  Confirm Password
                </label>
                <div className="relative">
                  <input
                    name="confirmPassword"
                    type={showConfirm ? "text" : "password"}
                    required
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    className="input-academy pr-10"
                    placeholder="••••••••"
                  />
                  <button
                    type="button"
                    onClick={() => setShowConfirm(!showConfirm)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-[#202C59]"
                  >
                    {showConfirm ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>
            </div>

            {/* Validation Checklist UI */}
            <div className="bg-[#F8F9FA] border-2 border-[#202C59] p-4 grid grid-cols-2 gap-2">
              <ValidationItem
                isValid={validation.length}
                text="8+ Characters"
              />
              <ValidationItem
                isValid={validation.upper}
                text="Capital Letter"
              />
              <ValidationItem
                isValid={validation.number}
                text="Contains Number"
              />
              <ValidationItem
                isValid={validation.special}
                text="Special Character"
              />
              <div
                className={`col-span-2 text-[10px] font-black uppercase flex items-center gap-2 ${validation.match ? "text-green-600" : "text-[#581F18]"}`}
              >
                {validation.match ? <Check size={12} /> : <X size={12} />}{" "}
                Passwords Match
              </div>
            </div>

            <div className="pt-4">
              <button
                type="submit"
                disabled={loading || !isFormValid}
                className={`w-full font-black py-5 text-sm uppercase tracking-widest border-2 border-[#202C59] transition-all 
                  ${isFormValid ? "bg-[#D95D39] text-white shadow-[6px_6px_0px_0px_#202C59] hover:translate-y-[-2px] hover:shadow-[8px_8px_0px_0px_#202C59] active:translate-y-[2px] active:shadow-none" : "bg-gray-200 text-gray-400 cursor-not-allowed opacity-70"}`}
              >
                {loading ? (
                  <Loader2 className="animate-spin mx-auto" />
                ) : (
                  "Complete Registration"
                )}
              </button>
            </div>
          </form>

          <p className="mt-8 text-center text-xs font-bold text-[#202C59]">
            Already have an account?{" "}
            <a
              href="/login"
              className="text-[#F18805] underline decoration-2 underline-offset-4"
            >
              Log in here
            </a>
          </p>
        </div>
      </main>
    </div>
  );
}

function ValidationItem({ isValid, text }: { isValid: boolean; text: string }) {
  return (
    <div
      className={`flex items-center gap-2 text-[10px] font-black uppercase ${isValid ? "text-green-600" : "text-slate-400"}`}
    >
      {isValid ? <Check size={12} /> : <X size={12} />} {text}
    </div>
  );
}
