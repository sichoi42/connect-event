"use client";

import { useEffect, useState } from "react";
import EventList from "@/components/EventList";
import EventDetails from "@/components/EventDetails";
import Auth from "@/components/Auth";
import CreateEvent from "@/components/CreateEvent";
import LocationList from "@/components/LocationList";
import CreateLocation from "@/components/CreateLocation";
import { EventPreviewDto } from "./types";

export default function Home() {
  const [selectedEvent, setSelectedEvent] = useState<EventPreviewDto | null>(
    null
  );
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [activeTab, setActiveTab] = useState(() => {
    if (typeof window !== "undefined") {
      return localStorage.getItem("activeTab") || "events";
    }
    return "events";
  });

  useEffect(() => {
    const checkAuth = () => {
      if (typeof window !== "undefined") {
        const token = localStorage.getItem("accessToken");
        setIsAuthenticated(!!token);
      }
    };
    checkAuth();
  }, []);

  useEffect(() => {
    localStorage.setItem("isAuthenticated", isAuthenticated.toString());
  }, [isAuthenticated]);

  useEffect(() => {
    localStorage.setItem("activeTab", activeTab);
  }, [activeTab]);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setIsAuthenticated(false);
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">Connect Event App</h1>
      {!isAuthenticated ? (
        <Auth
          onLogin={() => {
            setIsAuthenticated(true);
            // Assuming the Auth component sets the token in localStorage
          }}
        />
      ) : (
        <div>
          <nav className="mb-4">
            <ul className="flex space-x-4">
              <li>
                <button
                  className={`px-4 py-2 rounded ${
                    activeTab === "events"
                      ? "bg-blue-500 text-white"
                      : "bg-gray-200"
                  }`}
                  onClick={() => setActiveTab("events")}
                >
                  Events
                </button>
              </li>
              <li>
                <button
                  className={`px-4 py-2 rounded ${
                    activeTab === "createEvent"
                      ? "bg-blue-500 text-white"
                      : "bg-gray-200"
                  }`}
                  onClick={() => setActiveTab("createEvent")}
                >
                  Create Event
                </button>
              </li>
              <li>
                <button
                  className={`px-4 py-2 rounded ${
                    activeTab === "locations"
                      ? "bg-blue-500 text-white"
                      : "bg-gray-200"
                  }`}
                  onClick={() => setActiveTab("locations")}
                >
                  Locations
                </button>
              </li>
              <li>
                <button
                  className={`px-4 py-2 rounded ${
                    activeTab === "createLocation"
                      ? "bg-blue-500 text-white"
                      : "bg-gray-200"
                  }`}
                  onClick={() => setActiveTab("createLocation")}
                >
                  Create Location
                </button>
              </li>
            </ul>
          </nav>
          {activeTab === "events" && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <EventList onSelectEvent={setSelectedEvent} />
              {selectedEvent && (
                <EventDetails eventId={selectedEvent.eventId} />
              )}
            </div>
          )}
          {activeTab === "createEvent" && <CreateEvent />}
          {activeTab === "locations" && <LocationList />}
          {activeTab === "createLocation" && <CreateLocation />}
          <button
            onClick={handleLogout}
            className="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
          >
            Logout
          </button>
        </div>
      )}
    </div>
  );
}
