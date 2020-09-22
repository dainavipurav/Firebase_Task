package com.android.firebasetask.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.firebasetask.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class SuccessActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mConstraintLayoutSuccess: ConstraintLayout
    private lateinit var mTextViewSuccessWelcomeUser: TextView
    private lateinit var mTextViewSuccessCurrentLocation: TextView
    private lateinit var mTextViewSuccessViewAllRegisteredUsersListTitle: TextView
    private lateinit var mRecyclerViewSuccessAllRegisteredUsers: RecyclerView
    private lateinit var mButtonSuccessSignOut: Button

    private var mAuthListener: AuthStateListener? = null
    private var mAuth: FirebaseAuth? = null

    private val REQUEST_CODE_LOCATION_PERMISSION = 1

    private lateinit var mGeoCoder : Geocoder
    private var addresses : List<Address> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        mAuth = FirebaseAuth.getInstance()
        var mUser = FirebaseAuth.getInstance().currentUser

        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@SuccessActivity, LoginActivity::class.java))
                finish()
            }
            else{ac
                mTextViewSuccessWelcomeUser.text = "Welcome, \n ${mUser?.email}"
            }
        }

        mConstraintLayoutSuccess = findViewById(R.id.constraintLayoutSuccess)
        mTextViewSuccessWelcomeUser = findViewById(R.id.textViewSuccessWelcomeUser)
        mTextViewSuccessCurrentLocation = findViewById(R.id.textViewSuccessCurrentLocation)
        mTextViewSuccessViewAllRegisteredUsersListTitle =
            findViewById(R.id.textViewSuccessViewAllRegisteredUsersListTitle)
        mRecyclerViewSuccessAllRegisteredUsers =
            findViewById(R.id.recyclerViewSuccessAllRegisteredUsers)
        mButtonSuccessSignOut = findViewById(R.id.buttonSuccessSignOut)

        mButtonSuccessSignOut.setOnClickListener(this)

        mGeoCoder = Geocoder(this@SuccessActivity, Locale.getDefault())

        getCurrentLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        val mLocationRequest: LocationRequest = LocationRequest()
        mLocationRequest.setInterval(10000)
        mLocationRequest.setFastestInterval(3000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@SuccessActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            LocationServices.getFusedLocationProviderClient(this@SuccessActivity)
                .requestLocationUpdates(mLocationRequest, object : LocationCallback() {
                    override fun onLocationResult(mLocationResult: LocationResult?) {
                        super.onLocationResult(mLocationResult)
                        LocationServices.getFusedLocationProviderClient(this@SuccessActivity)
                            .removeLocationUpdates(this)
                        if (mLocationResult != null && mLocationResult.locations.size > 0)
                        {
                            val mLatestLocationIndex: Int = mLocationResult.locations.size - 1
                            val latitude =
                                mLocationResult.locations.get(mLatestLocationIndex).latitude
                            val longitude =
                                mLocationResult.locations.get(mLatestLocationIndex).longitude

                            try {
                                addresses =  mGeoCoder.getFromLocation(latitude,longitude,1)

                                val address : String = addresses.get(0).getAddressLine(0)
                                val area : String = addresses.get(0).locality
                                val city : String = addresses.get(0).adminArea
                                val country : String = addresses.get(0).countryName
                                val postalCode : String = addresses.get(0).postalCode

                                val fullAddress : String = address+", " + area + ", " + city + ", " + country + ", " + postalCode

                                mTextViewSuccessCurrentLocation.text = address
                            }
                            catch (e: IOException){
                                e.printStackTrace()
                            }
                        }
                    }
                }, Looper.getMainLooper())

        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSuccessSignOut -> {
                mAuth?.signOut()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuthListener?.let { mAuth?.addAuthStateListener(it) }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!)
        }
    }
}