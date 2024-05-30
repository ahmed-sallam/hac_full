import {
    History,
    Line,
    Store,
    UserDto
} from "../../material-request/interfaces/OneMaterialRequest";

export interface OneRFPQ {
    id: number
    number: string
    date: string
    status: string
    notes: string
    store: Store
    internalRef: number
    currentPhase: string
    userDto: UserDto
    lines: Line[]
    history: History[]
}



