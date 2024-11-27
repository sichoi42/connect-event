export interface EventPreviewDto {
  eventId: number
  title: string
  descriptionPreview: string
  startedAt: string
  endedAt: string
}

export interface EventDetailDto {
  eventId: number
  title: string
  description: string
  startedAt: string
  endedAt: string
  location: EventLocationDto
  eventTags: EventTagDto[]
}

export interface EventLocationDto {
  locationId: number
  name: string
  capacity: number
  address: string
}

export interface EventTagDto {
  eventTagId: number
  tagId: number
  tagName: string
}

export interface LocationDto {
  locationId: number
  name: string
  capacity: number
  address: string
}

export interface TagDto {
  tagId: number
  tagName: string
}

export interface FeedbackDto {
  feedbackId: number
  memberId: number
  memberName: string
  memberEmail: string
  comment: string
  rating: number
  createdAt: string
}

export interface ParticipantDto {
  participantId: number
  name: string
  email: string
  registeredAt: string
  role: 'MASTER' | 'MEMBER'
}

