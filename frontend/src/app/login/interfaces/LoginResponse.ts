export interface LoginResponse {
    accessToken: string
    tokenType: string
    user: User
}

export interface User {
    id: number
    createdAt: string
    updatedAt: string
    isActive: boolean
    username: string
}