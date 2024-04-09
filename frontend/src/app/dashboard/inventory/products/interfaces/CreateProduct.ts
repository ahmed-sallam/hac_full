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
    productSets:any
    alternatives:any
}
