export interface ReplenishmentRequest {
    notes: string
    date: string
    store: number
    lines: Line[]
    status?: string
}

export interface Line {
    quantity: number
    notes: string
    product: number
}