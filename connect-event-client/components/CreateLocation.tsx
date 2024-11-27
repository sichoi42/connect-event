"use client";

import { useState } from "react";

export default function CreateLocation() {
  const [locationData, setLocationData] = useState({
    name: "",
    capacity: "",
    address: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/locations/new`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
          body: JSON.stringify(locationData),
        }
      );
      if (response.ok) {
        alert("Location created successfully!");
        setLocationData({ name: "", capacity: "", address: "" });
      } else {
        alert("Failed to create location. Please try again.");
      }
    } catch (error) {
      console.error("Error creating location:", error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setLocationData({ ...locationData, [name]: value });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="name" className="block mb-1">
          Name
        </label>
        <input
          type="text"
          id="name"
          name="name"
          value={locationData.name}
          onChange={handleChange}
          required
          className="w-full px-3 py-2 border rounded"
        />
      </div>
      <div>
        <label htmlFor="capacity" className="block mb-1">
          Capacity
        </label>
        <input
          type="number"
          id="capacity"
          name="capacity"
          value={locationData.capacity}
          onChange={handleChange}
          required
          className="w-full px-3 py-2 border rounded"
        />
      </div>
      <div>
        <label htmlFor="address" className="block mb-1">
          Address
        </label>
        <input
          type="text"
          id="address"
          name="address"
          value={locationData.address}
          onChange={handleChange}
          required
          className="w-full px-3 py-2 border rounded"
        />
      </div>
      <button
        type="submit"
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Create Location
      </button>
    </form>
  );
}
