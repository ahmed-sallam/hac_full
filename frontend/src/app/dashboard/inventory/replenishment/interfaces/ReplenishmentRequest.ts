export interface ReplenishmentRequest {
    notes: string
    date: string
    store: number
    lines: Line[]
}

export interface Line {
    quantity: number
    notes: string
    product: number
}