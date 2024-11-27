"use client";

import { useState, useEffect } from "react";
import { EventPreviewDto } from "../app/types";

interface EventListProps {
  onSelectEvent: (event: EventPreviewDto) => void;
}

export default function EventList({ onSelectEvent }: EventListProps) {
  const [events, setEvents] = useState<EventPreviewDto[]>([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    fetchEvents();
  }, [page]);

  const fetchEvents = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/?page=${page}&size=10`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setEvents(data.results);
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Event List</h2>
      <ul className="space-y-4">
        {events.map((event) => (
          <li
            key={event.eventId}
            className="border p-4 rounded-lg cursor-pointer hover:bg-gray-100"
            onClick={() => onSelectEvent(event)}
          >
            <h3 className="text-xl font-semibold">{event.title}</h3>
            <p className="text-gray-600">{event.descriptionPreview}</p>
            <p className="text-sm text-gray-500">
              {new Date(event.startedAt).toLocaleString()} -{" "}
              {new Date(event.endedAt).toLocaleString()}
            </p>
          </li>
        ))}
      </ul>
      <div className="mt-4 flex justify-between">
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded disabled:bg-gray-300"
          onClick={() => setPage(page - 1)}
          disabled={page === 0}
        >
          Previous
        </button>
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded"
          onClick={() => setPage(page + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}
