package com.example.cuzzapp

class User {
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var Points:Int = 0
    var photoUrl: String = ""
    var state:String = ""
    var role: String = ""
    var learningPath:List<String> = emptyList()
    var achievements: HashMap<String, HashMap<String, achievementuriUSER>> = HashMap()

}