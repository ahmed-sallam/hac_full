export interface CreateProduct {
    productNumber: string
    descriptionAr: string
    descriptionEn: string
    image: string
    partImage: string
    minQuantity: number
    isOriginal: boolean
    unit: string
    machinePart: number
    mainBrand: number
    subBrand: number
    country: number
    productSets?: any | ProductSet[]
    alternatives?: any,
    related?: any,
    machineryType: number,
    machineryModel: number,
    sellQuantity?: number,
    sellIndividual?: number,
    buyQuantity?: number,
    buyIndividual?: number,
}

export interface ProductSet {
    quantity: number
    productId: number
    productSetId: number
    isRestricted: boolean
}