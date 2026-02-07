"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

export default function NavBar() {
  const path = usePathname();
    return (
      <nav className="bg-[color:var(--primary)] shadow">
        <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
          <Link href="/" className="text-xl font-semibold text-white">MyApp</Link>
          <div className="flex items-center gap-3">
            <Link href="/login" className={`px-3 py-2 rounded text-white/90 hover:bg-white/10 ${path === '/login' ? 'bg-white/20' : ''}`}>Login</Link>
            <Link href="/register" className={`px-3 py-2 rounded text-white/90 hover:bg-white/10 ${path === '/register' ? 'bg-white/20' : ''}`}>Register</Link>
            <Link href="/dashboard" className={`px-3 py-2 rounded text-white/90 hover:bg-white/10 ${path === '/dashboard' ? 'bg-white/20' : ''}`}>Dashboard</Link>
          </div>
        </div>
      </nav>
    );
}
