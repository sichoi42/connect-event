"use client";

import { useState, useEffect } from "react";
import { LocationDto, TagDto } from "../app/types";

export default function CreateEvent() {
  const [eventData, setEventData] = useState({
    title: "",
    description: "",
    startedAt: "",
    endedAt: "",
    locationId: "",
    tags: [] as number[],
  });
  const [locations, setLocations] = useState<LocationDto[]>([]);
  const [tags, setTags] = useState<TagDto[]>([]);
  const [newTag, setNewTag] = useState("");
  const [searchResults, setSearchResults] = useState<TagDto[]>([]);
  const [showNewLocationForm, setShowNewLocationForm] = useState(false);
  const [newLocation, setNewLocation] = useState({
    name: "",
    capacity: "",
    address: "",
  });

  useEffect(() => {
    fetchLocations();
    fetchTags();
  }, []);

  const fetchLocations = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/locations/`,
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
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  };

  const fetchTags = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/tags`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setTags(data);
    } catch (error) {
      console.error("Error fetching tags:", error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/new`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
          body: JSON.stringify(eventData),
        }
      );
      if (response.ok) {
        alert("Event created successfully!");
        setEventData({
          title: "",
          description: "",
          startedAt: "",
          endedAt: "",
          locationId: "",
          tags: [],
        });
      } else {
        try {
          const errorData = await response.json();
          alert(
            errorData.message || "Failed to create event. Please try again."
          );
        } catch (error) {
          alert("Failed to create event. Please try again.");
        }
      }
    } catch (error) {
      console.error("Error creating event:", error);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value } = e.target;
    setEventData({ ...eventData, [name]: value });
  };

  const handleTagChange = (tagId: number) => {
    const updatedTags = eventData.tags.includes(tagId)
      ? eventData.tags.filter((id) => id !== tagId)
      : [...eventData.tags, tagId];
    setEventData({ ...eventData, tags: updatedTags });
  };

  const handleTagSearch = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const searchTerm = e.target.value;
    setNewTag(searchTerm);

    if (searchTerm.length > 0) {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/tags?query=${searchTerm}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
          }
        );
        if (response.ok) {
          const data = await response.json();
          setSearchResults(data);
        }
      } catch (error) {
        console.error("Error searching tags:", error);
      }
    } else {
      setSearchResults([]);
    }
  };

  const handleTagSelect = (tag: TagDto) => {
    if (!eventData.tags.includes(tag.tagId)) {
      setEventData({ ...eventData, tags: [...eventData.tags, tag.tagId] });
    }
    setNewTag("");
    setSearchResults([]);
  };

  const handleTagKeyDown = async (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      if (searchResults.length > 0) {
        handleTagSelect(searchResults[0]);
      } else if (newTag.trim() !== "") {
        try {
          const response = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/tags/new`,
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
              },
              body: JSON.stringify({ tagName: newTag.trim() }),
            }
          );
          if (response.ok) {
            const createdTag = await response.json();
            setTags([...tags, createdTag]);
            setEventData({
              ...eventData,
              tags: [...eventData.tags, createdTag.tagId],
            });
            setNewTag("");
          } else {
            try {
              const errorData = await response.json();
              alert(
                errorData.message || "Failed to create event. Please try again."
              );
            } catch (error) {
              alert("Failed to create event. Please try again.");
            }
          }
        } catch (error) {
          console.error("Error creating tag:", error);
        }
      }
    }
  };

  const handleCreateLocation = async (e: React.FormEvent) => {
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
          body: JSON.stringify(newLocation),
        }
      );
      if (response.ok) {
        const createdLocation = await response.json();
        setLocations([...locations, createdLocation]);
        setEventData({
          ...eventData,
          locationId: createdLocation.locationId.toString(),
        });
        setNewLocation({ name: "", capacity: "", address: "" });
        setShowNewLocationForm(false);
        alert("Location created successfully!");
      } else {
        try {
          const errorData = await response.json();
          alert(
            errorData.message || "Failed to create event. Please try again."
          );
        } catch (error) {
          alert("Failed to create event. Please try again.");
        }
      }
    } catch (error) {
      console.error("Error creating location:", error);
    }
  };

  return (
    <div className="space-y-8">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="title" className="block mb-1">
            Title
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={eventData.title}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border rounded"
          />
        </div>
        <div>
          <label htmlFor="description" className="block mb-1">
            Description
          </label>
          <textarea
            id="description"
            name="description"
            value={eventData.description}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border rounded"
          />
        </div>
        <div>
          <label htmlFor="startedAt" className="block mb-1">
            Start Date
          </label>
          <input
            type="datetime-local"
            id="startedAt"
            name="startedAt"
            value={eventData.startedAt}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border rounded"
          />
        </div>
        <div>
          <label htmlFor="endedAt" className="block mb-1">
            End Date
          </label>
          <input
            type="datetime-local"
            id="endedAt"
            name="endedAt"
            value={eventData.endedAt}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border rounded"
          />
        </div>
        <div>
          <label htmlFor="locationId" className="block mb-1">
            Location
          </label>
          <div className="flex items-center space-x-2">
            <select
              id="locationId"
              name="locationId"
              value={eventData.locationId}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded"
            >
              <option value="">Select a location</option>
              {locations.map((location) => (
                <option key={location.locationId} value={location.locationId}>
                  {location.name}
                </option>
              ))}
            </select>
            <button
              type="button"
              onClick={() => setShowNewLocationForm(true)}
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
            >
              New
            </button>
          </div>
        </div>
        {showNewLocationForm && (
          <div className="border p-4 rounded mt-2">
            <h4 className="text-lg font-semibold mb-2">Create New Location</h4>
            <form onSubmit={handleCreateLocation} className="space-y-2">
              <input
                type="text"
                value={newLocation.name}
                onChange={(e) =>
                  setNewLocation({ ...newLocation, name: e.target.value })
                }
                placeholder="Location name"
                className="w-full px-3 py-2 border rounded"
                required
              />
              <input
                type="number"
                value={newLocation.capacity}
                onChange={(e) =>
                  setNewLocation({ ...newLocation, capacity: e.target.value })
                }
                placeholder="Capacity"
                className="w-full px-3 py-2 border rounded"
                required
              />
              <input
                type="text"
                value={newLocation.address}
                onChange={(e) =>
                  setNewLocation({ ...newLocation, address: e.target.value })
                }
                placeholder="Address"
                className="w-full px-3 py-2 border rounded"
                required
              />
              <div className="flex justify-end space-x-2">
                <button
                  type="button"
                  onClick={() => setShowNewLocationForm(false)}
                  className="bg-gray-300 text-gray-800 px-4 py-2 rounded hover:bg-gray-400"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  Create Location
                </button>
              </div>
            </form>
          </div>
        )}
        <div>
          <label className="block mb-1">Tags</label>
          <div className="flex flex-wrap gap-2 mb-2">
            {eventData.tags.map((tagId) => {
              const tag = tags.find((t) => t.tagId === tagId);
              return (
                <span
                  key={tagId}
                  className="bg-blue-100 text-blue-800 px-2 py-1 rounded"
                >
                  {tag?.tagName}
                  <button
                    type="button"
                    onClick={() => handleTagChange(tagId)}
                    className="ml-2 text-blue-500 hover:text-blue-700"
                  >
                    &times;
                  </button>
                </span>
              );
            })}
          </div>
          <div className="relative">
            <input
              type="text"
              value={newTag}
              onChange={handleTagSearch}
              onKeyDown={handleTagKeyDown}
              placeholder="Search or create new tag"
              className="w-full px-3 py-2 border rounded"
            />
            {searchResults.length > 0 && (
              <ul className="absolute z-10 w-full bg-white border rounded mt-1">
                {searchResults.map((tag) => (
                  <li
                    key={tag.tagId}
                    onClick={() => handleTagSelect(tag)}
                    className="px-3 py-2 hover:bg-gray-100 cursor-pointer"
                  >
                    {tag.tagName}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Create Event
        </button>
      </form>
    </div>
  );
}
