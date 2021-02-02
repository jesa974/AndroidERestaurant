package fr.isen.prezut.androiderestaurant.domain

import org.json.JSONObject
import java.io.Serializable

data class UserData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val password: String
    ) : Serializable {
        fun toSignUpParams(params: JSONObject): JSONObject {
            params.put("firstname", firstName)
            params.put("lastname", lastName)
            params.put("address", address)
            params.put("email", email)
            params.put("password", password)
            return params
        }

        fun toSignInParams(params: JSONObject): JSONObject {
            params.put("email", email)
            params.put("password", password)
            return params
        }
}