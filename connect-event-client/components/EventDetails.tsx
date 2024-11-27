"use client";

import { useState, useEffect } from "react";
import { EventDetailDto, FeedbackDto, ParticipantDto } from "../app/types";

interface EventDetailsProps {
  eventId: number;
}

export default function EventDetails({ eventId }: EventDetailsProps) {
  const [event, setEvent] = useState<EventDetailDto | null>(null);
  const [feedbacks, setFeedbacks] = useState<FeedbackDto[]>([]);
  const [participants, setParticipants] = useState<ParticipantDto[]>([]);
  const [newFeedback, setNewFeedback] = useState({ comment: "", rating: 5 });

  useEffect(() => {
    fetchEventDetails();
    fetchFeedbacks();
    fetchParticipants();
  }, [eventId]);

  const fetchEventDetails = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/events/detail/${eventId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setEvent(data);
    } catch (error) {
      console.error("Error fetching event details:", error);
    }
  };

  const fetchFeedbacks = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/feedbacks/events/${eventId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setFeedbacks(data.results);
    } catch (error) {
      console.error("Error fetching feedbacks:", error);
    }
  };

  const fetchParticipants = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/participants/events/${eventId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      const data = await response.json();
      setParticipants(data.results);
    } catch (error) {
      console.error("Error fetching participants:", error);
    }
  };

  const handleParticipate = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/participants/events/${eventId}/request`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      if (response.ok) {
        alert("Successfully requested to participate in the event!");
        fetchParticipants();
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
      console.error("Error requesting participation:", error);
    }
  };

  const handleSubmitFeedback = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/feedbacks/events/${eventId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
          body: JSON.stringify(newFeedback),
        }
      );
      if (response.ok) {
        alert("Feedback submitted successfully!");
        setNewFeedback({ comment: "", rating: 5 });
        fetchFeedbacks();
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
      console.error("Error submitting feedback:", error);
    }
  };

  if (!event) {
    return <div>Loading...</div>;
  }

  return (
    <div className="border p-4 rounded-lg">
      <h2 className="text-2xl font-bold mb-4">{event.title}</h2>
      <p className="mb-2">{event.description}</p>
      <p className="text-sm text-gray-500">
        {new Date(event.startedAt).toLocaleString()} -{" "}
        {new Date(event.endedAt).toLocaleString()}
      </p>
      <div className="mt-4">
        <h3 className="text-xl font-semibold">Location</h3>
        <p>{event.location.name}</p>
        <p>{event.location.address}</p>
        <p>Capacity: {event.location.capacity}</p>
      </div>
      <div className="mt-4">
        <h3 className="text-xl font-semibold">Tags</h3>
        <div className="flex flex-wrap gap-2">
          {event.eventTags.map((tag) => (
            <span
              key={tag.eventTagId}
              className="bg-gray-200 px-2 py-1 rounded-full text-sm"
            >
              {tag.tagName}
            </span>
          ))}
        </div>
      </div>
      <button
        className="mt-4 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
        onClick={handleParticipate}
      >
        Request to Participate
      </button>

      <div className="mt-8">
        <h3 className="text-xl font-semibold mb-2">Participants</h3>
        <ul className="space-y-2">
          {participants.map((participant) => (
            <li
              key={participant.participantId}
              className="flex justify-between items-center"
            >
              <span>
                {participant.name} ({participant.email})
              </span>
              <span className="text-sm text-gray-500">{participant.role}</span>
            </li>
          ))}
        </ul>
      </div>

      <div className="mt-8">
        <h3 className="text-xl font-semibold mb-2">Feedbacks</h3>
        <ul className="space-y-4">
          {feedbacks.map((feedback) => (
            <li key={feedback.feedbackId} className="border-b pb-2">
              <p>{feedback.comment}</p>
              <div className="flex justify-between items-center mt-2">
                <span className="text-sm text-gray-500">
                  Rating: {feedback.rating}/5
                </span>
                <span className="text-sm text-gray-500">
                  By: {feedback.memberName}
                </span>
              </div>
            </li>
          ))}
        </ul>
      </div>

      <form onSubmit={handleSubmitFeedback} className="mt-8">
        <h3 className="text-xl font-semibold mb-2">Submit Feedback</h3>
        <div className="space-y-4">
          <div>
            <label htmlFor="comment" className="block mb-1">
              Comment
            </label>
            <textarea
              id="comment"
              value={newFeedback.comment}
              onChange={(e) =>
                setNewFeedback({ ...newFeedback, comment: e.target.value })
              }
              required
              className="w-full px-3 py-2 border rounded"
            />
          </div>
          <div>
            <label htmlFor="rating" className="block mb-1">
              Rating
            </label>
            <input
              type="number"
              id="rating"
              min="1"
              max="5"
              value={newFeedback.rating}
              onChange={(e) =>
                setNewFeedback({
                  ...newFeedback,
                  rating: parseInt(e.target.value),
                })
              }
              required
              className="w-full px-3 py-2 border rounded"
            />
          </div>
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Submit Feedback
          </button>
        </div>
      </form>
    </div>
  );
}
