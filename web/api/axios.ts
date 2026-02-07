const BASE = "http://localhost:8080";

async function request(method: string, url: string, body?: any, opts: RequestInit = {}) {
  const headers: Record<string, string> = {
    ...(opts.headers as Record<string, string> | undefined),
  };

  if (typeof window !== "undefined") {
    const token = localStorage.getItem("token");
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  if (body !== undefined && !(body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
    opts.body = JSON.stringify(body);
  } else if (body instanceof FormData) {
    opts.body = body as any;
  }

  const res = await fetch(BASE + url, { method, headers, ...opts });
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) {
    const err: any = new Error(data?.message || res.statusText || "Request failed");
    err.response = { status: res.status, data };
    throw err;
  }
  return { data };
}

const api = {
  get: (url: string, opts?: RequestInit) => request("GET", url, undefined, opts),
  post: (url: string, body?: any, opts?: RequestInit) => request("POST", url, body, opts),
  put: (url: string, body?: any, opts?: RequestInit) => request("PUT", url, body, opts),
  delete: (url: string, body?: any, opts?: RequestInit) => request("DELETE", url, body, opts),
  rawRequest: request,
};

export default api;
