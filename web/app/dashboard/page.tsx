"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import NavBar from "../components/NavBar";

type User = {
  userId: number;
  email: string;
  firstName?: string;
  lastName?: string;
  gender?: string;
};

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await api.get("/api/user/me");
        setUser(res.data);
      } catch (err: any) {
        // unauthorized or error -> redirect to login
        router.push("/login");
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [router]);

  const logout = () => {
    localStorage.removeItem("token");
    router.push("/login");
  };

  return (
    <div>
      <NavBar />
      <main className="min-h-screen bg-gray-50 p-6">
        <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-4 gap-6">
          <aside className="md:col-span-1 bg-white p-4 rounded shadow">
            <h3 className="font-semibold mb-4">Account</h3>
            <button onClick={logout} className="w-full bg-red-500 text-white py-2 rounded">Logout</button>
          </aside>

          <section className="md:col-span-3">
            <div className="bg-white p-6 rounded shadow">
              <h2 className="text-2xl font-semibold mb-4">Profile</h2>
              {loading && <div>Loading...</div>}
              {!loading && user && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="p-4 border rounded">
                    <p className="text-sm text-gray-500">Full Name</p>
                    <p className="text-lg font-medium">{user.firstName} {user.lastName}</p>
                  </div>
                  <div className="p-4 border rounded">
                    <p className="text-sm text-gray-500">Email</p>
                    <p className="text-lg font-medium">{user.email}</p>
                  </div>
                  <div className="p-4 border rounded">
                    <p className="text-sm text-gray-500">Gender</p>
                    <p className="text-lg font-medium">{user.gender || 'Unspecified'}</p>
                  </div>
                </div>
              )}
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}
