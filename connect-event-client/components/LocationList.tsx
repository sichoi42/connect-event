"use client";

import { useState, useEffect } from "react";
import { LocationDto } from "../app/types";

export default function LocationList() {
  const [locations, setLocations] = useState<LocationDto[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchLocations();
  }, [page]);

  const fetchLocations = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/locations/?page=${page}&size=10`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setLocations(data.results);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Locations</h2>
      <ul className="space-y-4">
        {locations.map((location) => (
          <li key={location.locationId} className="border p-4 rounded-lg">
            <h3 className="text-xl font-semibold">{location.name}</h3>
            <p>Address: {location.address}</p>
            <p>Capacity: {location.capacity}</p>
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
          className="bg-blue-500 text-white px-4 py-2 rounded disabled:bg-gray-300"
          onClick={() => setPage(page + 1)}
          disabled={page === totalPages - 1}
        >
          Next
        </button>
      </div>
    </div>
  );
}
