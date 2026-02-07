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
    <div className="min-h-screen bg-[color:var(--background)]">
      <header className="bg-[color:var(--surface)] shadow-sm">
        <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="text-xl font-semibold text-[color:var(--foreground)]">MyApp</div>
            <div className="text-sm muted">Account</div>
          </div>
          <div>
            <button onClick={logout} className="px-3 py-2 rounded bg-red-500 text-white">Logout</button>
          </div>
        </div>
      </header>

      <main className="p-6">
        <div className="max-w-4xl mx-auto">
          <div className="card">
            {loading && <div className="text-center py-12">Loading profile…</div>}

            {!loading && user && (
+              <div className="flex items-start gap-6">
+                <div className="w-28 h-28 rounded-full bg-gray-100 flex items-center justify-center text-2xl font-semibold text-[color:var(--foreground)]">{(user.firstName?.[0]||'U')+(user.lastName?.[0]||'')}</div>
+                <div className="flex-1">
+                  <div className="flex items-center justify-between">
+                    <div>
+                      <h2 className="text-2xl heading">{user.firstName} {user.lastName}</h2>
+                      <p className="muted">Member since — <span className="font-medium">now</span></p>
+                    </div>
+                  </div>
+
+                  <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
+                    <div className="p-4 border rounded-lg">
+                      <div className="flex items-center gap-3">
+                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-[color:var(--primary)]" fill="none" viewBox="0 0 24 24" stroke="currentColor">
+                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 12a4 4 0 10-8 0v4h8v-4zM12 4a4 4 0 014 4v1H8V8a4 4 0 014-4z" />
+                        </svg>
+                        <div>
+                          <p className="text-sm muted">Email</p>
+                          <p className="font-medium">{user.email}</p>
+                        </div>
+                      </div>
+                    </div>
+
+                    <div className="p-4 border rounded-lg">
+                      <div className="flex items-center gap-3">
+                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-[color:var(--primary)]" viewBox="0 0 24 24" fill="none" stroke="currentColor">
+                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h14M12 5v14" />
+                        </svg>
+                        <div>
+                          <p className="text-sm muted">Gender</p>
+                          <p className="font-medium">{user.gender || 'Unspecified'}</p>
+                        </div>
+                      </div>
+                    </div>
+                  </div>
+                </div>
+              </div>
             )}
           </div>
         </div>
       </main>
     </div>
   );
}
