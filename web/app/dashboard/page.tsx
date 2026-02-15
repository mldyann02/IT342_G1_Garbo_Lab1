"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import api from "../../api/axios";
import {
  LogOut,
  Mail,
  User as UserIcon,
  Loader2,
  AlertCircle,
} from "lucide-react";

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  useEffect(() => {
    api
      .get("/api/user/me")
      .then((res) => setUser(res.data))
      .catch(() => router.push("/login"))
      .finally(() => setLoading(false));
  }, [router]);

  const confirmLogout = () => {
    api.post("/api/auth/logout").finally(() => {
      router.replace("/login");
    });
  };

  if (loading)
    return (
      <div className="min-h-screen bg-[#F8F9FA] flex items-center justify-center font-bold text-[#202C59]">
        LOADING...
      </div>
    );

  return (
    <div className="min-h-screen bg-[#F8F9FA] text-[#202C59] relative">
      <main className="max-w-4xl mx-auto px-6 py-20">
        <div className="flex flex-col md:flex-row items-stretch gap-0 border-2 border-[#202C59] bg-white shadow-[12px_12px_0px_0px_#202C59]">
          {/* Side Identity Block */}
          <div className="bg-[#D95D39] p-12 flex flex-col items-center justify-center text-white border-b-2 md:border-b-0 md:border-r-2 border-[#202C59]">
            <div className="w-24 h-24 bg-white border-2 border-[#202C59] flex items-center justify-center text-4xl font-black text-[#202C59] mb-4">
              {user?.firstName?.[0]}
              {user?.lastName?.[0]}
            </div>
            <h2 className="text-2xl font-black uppercase tracking-tighter text-center">
              {user?.firstName} {user?.lastName}
            </h2>
          </div>

          {/* Info Block */}
          <div className="flex-1 p-12">
            {/* Emphasized Text: Bigger User Profile */}
            <h3 className="text-2xl font-black text-[#581F18] uppercase tracking-tight mb-8">
              User Profile
            </h3>

            <div className="space-y-6">
              <div className="flex items-center gap-4">
                <div className="w-10 h-10 bg-[#F0A202] flex items-center justify-center text-[#202C59]">
                  <Mail size={20} />
                </div>
                <div>
                  <p className="text-[10px] font-black uppercase text-[#64748B]">
                    Email
                  </p>
                  <p className="font-bold">{user?.email}</p>
                </div>
              </div>

              <div className="flex items-center gap-4">
                <div className="w-10 h-10 bg-[#202C59] flex items-center justify-center text-white">
                  <UserIcon size={20} />
                </div>
                <div>
                  <p className="text-[10px] font-black uppercase text-[#64748B]">
                    Gender
                  </p>
                  <p className="font-bold capitalize">
                    {user?.gender || "Not Specified"}
                  </p>
                </div>
              </div>
            </div>

            {/* Emphasized Style: Button Style Logout */}
            <button
              onClick={() => setShowLogoutModal(true)}
              className="mt-12 bg-[#581F18] text-white font-black px-6 py-3 border-2 border-[#202C59] hover:translate-y-[-2px] hover:shadow-[6px_6px_0px_0px_#202C59] transition-all active:translate-y-[2px] active:shadow-none flex items-center gap-2 uppercase text-xs"
            >
              <LogOut size={16} />
              Logout Session
            </button>
          </div>
        </div>
      </main>

      {/* Logout Confirmation Modal */}
      {showLogoutModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4 bg-[#202C59]/40 backdrop-blur-sm">
          <div className="bg-white border-4 border-[#202C59] p-8 max-w-sm w-full animate-in zoom-in-95 duration-200">
            <div className="flex items-center gap-3 mb-4 text-[#581F18]">
              <AlertCircle size={28} strokeWidth={3} />
              <h4 className="text-xl font-black uppercase tracking-tighter">
                Confirmation
              </h4>
            </div>

            <p className="text-[#202C59] font-bold mb-8 leading-tight">
              Are you sure you want to end your current session?
            </p>

            <div className="flex gap-4">
              <button
                onClick={confirmLogout}
                className="flex-1 bg-[#D95D39] text-white font-black py-3 border-2 border-[#202C59] shadow-[4px_4px_0px_0px_#202C59] active:shadow-none active:translate-y-1 transition-all uppercase text-xs"
              >
                Yes, Logout
              </button>
              <button
                onClick={() => setShowLogoutModal(false)}
                className="flex-1 bg-white text-[#202C59] font-black py-3 border-2 border-[#202C59] hover:bg-[#F8F9FA] transition-all uppercase text-xs"
              >
                No, Stay
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
