"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { LogIn, UserPlus } from "lucide-react";

export default function NavBar() {
  const path = usePathname();

  return (
    <nav className="sticky top-0 z-50 bg-white border-b-2 border-[#202C59]">
      <div className="max-w-7xl mx-auto px-6 py-5 flex items-center justify-end gap-8">
        <Link
          href="/login"
          className={`flex items-center gap-2 text-sm font-bold uppercase tracking-tight transition-all ${
            path === "/login"
              ? "text-[#D95D39] underline decoration-4 underline-offset-8"
              : "text-[#202C59] hover:text-[#F18805]"
          }`}
        >
          {" "}
          Login
        </Link>
        <Link
          href="/register"
          className="bg-[#F18805] text-white px-6 py-2 rounded-sm font-bold uppercase hover:bg-[#202C59] transition-colors"
        >
          Register
        </Link>
      </div>
    </nav>
  );
}
