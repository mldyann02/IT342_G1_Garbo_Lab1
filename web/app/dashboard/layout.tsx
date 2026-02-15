"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();

  // Client-side guard removed: rely on server-side cookie validation
  // and the dashboard page's own fetch to redirect on 401/unauthorized.

  return <>{children}</>;
}
