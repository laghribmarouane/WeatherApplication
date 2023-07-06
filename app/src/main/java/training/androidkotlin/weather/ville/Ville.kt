package training.androidkotlin.weather.ville

data class Ville(var id: Long,
                 var name: String) {

    constructor(name: String) : this(-1, name)
}
