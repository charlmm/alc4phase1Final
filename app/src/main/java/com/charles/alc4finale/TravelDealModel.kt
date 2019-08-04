package com.charles.alc4finale

import java.io.Serializable

class TravelDealModel : Serializable {
    /* ***** Gets the trip ID **** */
    /**
     * Sets the trip id
     * @param id of the trip
     */
    var id: String? = null
    /* ***** Gets the trip's title **** */
    /**
     * Gets the title of the trip
     * @param title of the trip
     */
    var title: String? = null
    /* ***** Gets the trip's description **** */
    /**
     * Sets the trip's description
     * @param description of the trip
     */
    var description: String? = null
    /* ***** Gets the trip's price **** */
    /**
     * Sets the trip's price
     * @param price of the trip
     */
    var price: String? = null
    /* ***** Gets the trip's image **** */
    /**
     * Sets the trips image
     * @param imageUrl of the trip
     */
    var imageUrl: String? = null

    /* ***** Creates an instance of the TravelDealModel class **** */
    constructor() {}

    /**
     * Constructor containing details of the travel dealModels
     * @param title of the trip
     * @param description of the trip
     * @param price of the trip
     * @param imageUrl of the trip
     */
    constructor(title: String, description: String, price: String, imageUrl: String) {
        this.title = title
        this.description = description
        this.price = price
        this.imageUrl = imageUrl
    }
}
