export interface OneMaterialRequest {
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

export interface Store {
    id: number
    nameAr: string
    nameEn: string
}

export interface UserDto {
    id: number
    username: string
}

export interface Line {
    quantity: number
    notes: string
    product: Product
    storeInventory: number
}

export interface Product {
    id: number
    productNumber: string
    descriptionAr: string
    descriptionEn: string
    mainBrandAr: string
    mainBrandEn: string
    subBrandAr: string
    subBrandEn: string
    totalInventory: number
}

export interface History {
    actionDetails: string
    tableName: string
    recordId: number
    dateTime: string
    user: User
    id: number
}

export interface User {
    id: number
    username: string
}
