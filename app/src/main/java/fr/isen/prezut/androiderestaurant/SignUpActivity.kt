package fr.isen.prezut.androiderestaurant


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.wajahatkarim3.easyvalidation.core.Validator
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import fr.isen.prezut.androiderestaurant.databinding.ActivitySignUpBinding
import fr.isen.prezut.androiderestaurant.domain.RegisterData
import fr.isen.prezut.androiderestaurant.domain.UserData
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var inputName: EditText
    private lateinit var inputLastName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputAddress: EditText
    private lateinit var inputPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        inputName = binding.inputName
        inputLastName = binding.inputLastname
        inputEmail = binding.inputEmail
        inputAddress = binding.inputAddress
        inputPassword = binding.inputPassword

        binding.submitSignUp.setOnClickListener {
            val emailValid = emailValidator().check()
            val passwordValid = passwordValidator().check()
            val nameValid = textValidator(inputName).check()
            val lastnameValid = textValidator(inputLastName).check()
            val addressValid = textValidator(inputAddress).check()

            if (
                emailValid &&
                passwordValid &&
                nameValid &&
                lastnameValid &&
                addressValid
            ) {
                val user = UserData(
                    inputName.text.toString(),
                    inputLastName.text.toString(),
                    inputEmail.text.toString(),
                    inputAddress.text.toString(),
                    inputPassword.text.toString()
                )
                Log.d(TAG, "new sign up : $user")
                val queue = Volley.newRequestQueue(this)
                val req = signUp(user, "1")
                queue.add(req)
            }
        }

        binding.alreadyHaveAccountLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            Toast.makeText(this.applicationContext, "TRYING !!!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getSupportParentActivityIntent(): Intent? {
        return getParentActivityIntentImpl()
    }

    override fun getParentActivityIntent(): Intent? {
        return getParentActivityIntentImpl()
    }

    private fun getParentActivityIntentImpl(): Intent {
        var parentIntent = Intent(this, HomeActivity::class.java)
        // give back the item to the parent
        intent.extras?.getSerializable(ITEM)?.let {
            parentIntent = Intent(this, DetailActivity::class.java)
            parentIntent.putExtra(ITEM, it)
        }
        return parentIntent
    }

    private fun emailValidator(): Validator {
        return inputEmail.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                inputEmail.error = when(it) {
                    ERROR_EMPTY -> getString(R.string.error_empty)
                    else -> getString(R.string.error_email_non_valid)
                }
                inputEmail.setTextColor(getColor(R.color.invalid))
            }
            .addSuccessCallback {
                inputEmail.setTextColor(getColor(R.color.valid))
            }
    }

    private fun passwordValidator(): Validator {
        return inputPassword.validator()
            .nonEmpty()
            .minLength(PASSWORD_LENGTH)
            .atleastOneNumber()
            .atleastOneUpperCase()
            .atleastOneSpecialCharacters()
            .addErrorCallback {
                inputPassword.error = it
                inputPassword.setTextColor(getColor(R.color.invalid))
            }
            .addSuccessCallback {
                inputPassword.setTextColor(getColor(R.color.valid))
            }
    }

    private fun textValidator(input: EditText): Validator {
        return input.validator()
            .nonEmpty()
            .addErrorCallback {
                input.error  = getString(R.string.error_empty)
                input.setTextColor(getColor(R.color.invalid))
            }
            .addSuccessCallback {
                input.setTextColor(getColor(R.color.valid))
            }
    }

    private fun signUp(user: UserData, id_shop: String): JsonObjectRequest {
        // params
        val params = JSONObject()
        params.put("id_shop", id_shop)
        user.toSignUpParams(params)
        Log.i(TAG, "with params $params")
        return JsonObjectRequest(
            Request.Method.POST, API_REGISTER_URL, params,
            Response.Listener { response ->
                Log.d(TAG, "Sign Up Response: $response")
                Gson().fromJson(response["data"].toString(), RegisterData::class.java).let {
                    val sharedPref = this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt(ID_CLIENT, it.id)
                        apply()
                    }
                    Toast.makeText(this.applicationContext, "Sign up successfully", Toast.LENGTH_SHORT).show()
                    redirectToParent()
                }
            },
            Response.ErrorListener { error ->
                invalidateInput(inputEmail)
                Toast.makeText(this.applicationContext, "Cannot create an account, The user might already exist", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error: $error")
            }
        )
    }

    private fun invalidateInput(input: EditText) {
        input.setTextColor(getColor(R.color.invalid))
        input.error = getString(R.string.error_exist)
    }

    private fun redirectToParent() {
        // redirect to dish list
        intent.extras?.getSerializable(ITEM)?.let {
            val parent = Intent(this, DetailActivity::class.java)
            // to return to the right activity, the basket activity need the category
            parent.putExtra(ITEM, it)
            startActivity(parent)
        } ?: run {
            // by default
            val parent = Intent(this, HomeActivity::class.java)
            startActivity(parent)
        }
    }

    companion object {
        val TAG = SignUpActivity::class.java.simpleName
        val ITEM = "item"
        val PASSWORD_LENGTH: Int = 12
        val ERROR_EMPTY = "Can't be empty!"
        val API_REGISTER_URL = "http://test.api.catering.bluecodegames.com/user/register"
        val ID_CLIENT = "id_client"
    }
}