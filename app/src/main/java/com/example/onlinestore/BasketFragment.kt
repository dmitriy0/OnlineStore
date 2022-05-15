package com.example.onlinestore

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizationResult
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizeIntent
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import java.io.IOException
import java.math.BigDecimal
import java.util.*


class BasketFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basket, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val array = ArrayList<Product>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val firebaseAuth = FirebaseAuth.getInstance()
        var sum = 0.0
        myRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Toast.makeText(context, snapshot.child(firebaseAuth.uid.toString()).child("basket").child("count").value.toString().toInt(),Toast.LENGTH_LONG).show()
                val count = snapshot.child("users").child(firebaseAuth.uid.toString()).child("basket").child("count").value.toString().toInt()



                for (i in 0 until count) {
                    val productId = snapshot.child("users").child(firebaseAuth.uid.toString()).child("basket").child(i.toString()).value.toString()
                    val name = snapshot.child("products").child(productId).child("name").value.toString()

                    val price = snapshot.child("products").child(productId).child("price").value.toString()
                    sum += price.toDouble()

                    array.add(Product(name, "",
                        price, productId))
                    recyclerView.adapter = BasketAdapter(array, requireActivity(), requireContext())
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        val button = view.findViewById<Button>(R.id.buy)

        button.setOnClickListener{
            val paymentParameters = PaymentParameters(
                amount = Amount(BigDecimal.valueOf(sum), Currency.getInstance("RUB")),
                title = "Покупка",
                subtitle = "Покупка товаров на сумму $sum",
                clientApplicationKey = "test_OTA5MDk3KKCk0e25pQbeLLImFPE0ipQXJvlAaJ4Qv-Y", // key for client apps from the YooMoney Merchant Profile
                shopId = "909097", // ID of the store in the YooMoney system
                savePaymentMethod = SavePaymentMethod.OFF, // flag of the disabled option to save payment methods,
                paymentMethodTypes = setOf(PaymentMethodType.BANK_CARD), // the full list of available payment methods has been provided
            )
            val intent = createTokenizeIntent(requireContext(), paymentParameters)
            startActivityForResult(intent, 0)
        }
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            when (resultCode) {
                RESULT_OK -> {
                    val result = data?.let { createTokenizationResult(it) }
                    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                    val json = """{"payment_token" : "${result!!.paymentToken}", "amount" : { "value" : "10.0", "currency" : "RUB" } }"""
                    val client = OkHttpClient()
                    val body: RequestBody = json.toRequestBody(JSON)
                    val key = UUID.randomUUID().toString()

                    val request: Request = Request.Builder()
                        .url("https://api.yookassa.ru/v3/payments")
                        .addHeader("Authorization", Credentials.basic("909097","test_h_CqNhlNmpEoUUOkd7zKTPQX1g5N1vMEW6v-gFY_foU"))
                        .addHeader("Idempotence-Key", key)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                        }

                        override fun onResponse(call: Call, response: Response) {

                            val id = JSONObject(response.body!!.string()).getString("id")

                            val requestCapture: Request = Request.Builder()
                                .url("https://api.yookassa.ru/v3/payments/$id/capture")
                                .addHeader("Authorization", Credentials.basic("909097","test_h_CqNhlNmpEoUUOkd7zKTPQX1g5N1vMEW6v-gFY_foU"))
                                .addHeader("Idempotence-Key", key)
                                .addHeader("Content-Type", "application/json")
                                .post(body)
                                .build()

                            client.newCall(requestCapture).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {

                                }

                                override fun onResponse(call: Call, response: Response) {
                                    activity!!.runOnUiThread {
                                        Toast.makeText(context, JSONObject(response.body!!.string()).getString("status"),Toast.LENGTH_LONG).show()
                                    }

                                }
                            })
                        }
                    })

                }
                RESULT_CANCELED -> {
                    // user canceled tokenization
                }
            }
        }
    }
}