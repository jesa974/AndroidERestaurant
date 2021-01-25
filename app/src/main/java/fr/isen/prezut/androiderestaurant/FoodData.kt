package fr.isen.prezut.androiderestaurant

class FoodData {


    var itemName: String? = null
        private set
    var itemDescription: String? = null
        private set
    var itemCategory: String? = null
        private set
    var itemPrice: String? = null
        private set
    var itemImage: String? = null
        private set
    var key: String? = null
        private set

    fun setKey(key: String?) {
        this.key = key
    }

    @JvmName("getItemName1")
    fun getItemName(): String? {
        return this.itemName
    }

    constructor() {}
    constructor(itemName: String?, itemDescription: String?, itemPrice: String?, itemImage: String?) {
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.itemPrice = itemPrice
        this.itemImage = itemImage
    }
}