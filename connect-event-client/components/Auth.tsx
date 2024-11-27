"use client";

import { useState, useEffect } from "react";

interface AuthProps {
  onLogin: () => void;
}

export default function Auth({ onLogin }: AuthProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRegistering, setIsRegistering] = useState(false);
  const [name, setName] = useState("");
  const [redirectToLogin, setRedirectToLogin] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const endpoint = isRegistering
      ? "api/v1/auth/email-register"
      : "api/v1/auth/email-login";

    if (isRegistering) {
      formData.append("name", name);
    }

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/${endpoint}`,
        {
          method: "POST",
          body: formData,
        }
      );

      if (response.ok) {
        if (isRegistering) {
          alert("Registration successful! Please log in.");
          setRedirectToLogin(true);
        } else {
          const data = await response.json();
          localStorage.setItem("accessToken", data.accessToken);
          onLogin();
        }
      } else {
        const errorData = await response.json();
        alert(errorData.message || "Authentication failed. Please try again.");
      }
    } catch (error) {
      console.error("Error during authentication:", error);
    }
  };

  useEffect(() => {
    if (redirectToLogin) {
      setIsRegistering(false);
      setRedirectToLogin(false);
    }
  }, [redirectToLogin]);

  return (
    <div className="max-w-md mx-auto">
      {redirectToLogin ? (
        <p className="text-center text-lg font-semibold">
          Redirecting to login...
        </p>
      ) : (
        <>
          <h2 className="text-2xl font-bold mb-4">
            {isRegistering ? "Register" : "Login"}
          </h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label htmlFor="email" className="block mb-1">
                Email
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div>
              <label htmlFor="password" className="block mb-1">
                Password
              </label>
              <input
                type="password"
                id="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            {isRegistering && (
              <div>
                <label htmlFor="name" className="block mb-1">
                  Name
                </label>
                <input
                  type="text"
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                  className="w-full px-3 py-2 border rounded"
                />
              </div>
            )}
            <button
              type="submit"
              className="w-full bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              {isRegistering ? "Register" : "Login"}
            </button>
          </form>
          <button
            className="mt-4 text-blue-500 hover:underline"
            onClick={() => setIsRegistering(!isRegistering)}
          >
            {isRegistering
              ? "Already have an account? Login"
              : "Don't have an account? Register"}
          </button>
        </>
      )}
    </div>
  );
}
