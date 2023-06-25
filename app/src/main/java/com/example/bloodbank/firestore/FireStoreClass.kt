package com.example.bloodbank.firestore

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.bloodbank.models.*
import com.example.bloodbank.ui.activities.*
import com.example.bloodbank.ui.fragments.AchievementsFragment
import com.example.bloodbank.ui.fragments.HomeFragment
import com.example.bloodbank.ui.fragments.SearchDonorFragment
import com.example.bloodbank.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_search_donor.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun registerDonor(activity: Activity, donorInfo: Donor) {
        mFireStore.collection(Constants.DONORS)
            .document(donorInfo.id)
            .set(donorInfo, SetOptions.merge())
            .addOnSuccessListener {
                when(activity) {
                    is RegisterActivity -> {
                        activity.donorRegistrationSuccess()
                    }

                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when(activity) {
                    is RegisterActivity -> {
                        activity.hideProgressDialog()
                    }

                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the donor.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    /*fun getAdminDetails(activity: Activity) {
        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.ADMIN)
            // The documents id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the documents snapshot which is converted into the User Data model object.
                val admin = document.toObject(Admin::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.BLOODBANK_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor = sharedPreferences.edit()
                editor.putString(
                    // Key: Value example logged_in_username: fullname
                    Constants.LOGGED_IN_USERNAME,
                    admin.name
                )

                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.adminLoggedInSuccess(admin)
                    }
                }
            }

            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details",
                    e
                )
            }
    }*/

    fun getUserDetails(activity: Activity) {
        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
        // The documents id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the documents snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.BLOODBANK_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor = sharedPreferences.edit()
                editor.putString(
                    // Key: Value example logged_in_username: fullname
                    Constants.LOGGED_IN_USERNAME,
                    user.fullName
                )

                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                }
            }

            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details",
                    e
                )
            }
    }

    fun postBloodRequest(activity: PostActivity, postInfo: CustomUser) {
        mFireStore.collection(Constants.BLOOD_REQUEST)
            .document()
            .set(postInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.postBloodRequestSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                "Error while posting blood request.")
            }
    }

    fun getBloodRequestList(fragment: Fragment) {
        mFireStore.collection(Constants.BLOOD_REQUEST)
            .orderBy(Constants.DATE, Query.Direction.DESCENDING)
            .orderBy(Constants.TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                val bloodRequestList: ArrayList<CustomUser> = ArrayList()
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {


                        bloodRequestList.add(dc.document.toObject(CustomUser::class.java))
                    }

                }
                when (fragment) {
                    is HomeFragment -> {
                        fragment.successBloodRequestListFromStore(bloodRequestList)
                    }

                    else -> {
                        HomeFragment().hideProgressDialog()
                    }
                }


            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userHashMap, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->


                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun updateDonorProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.DONORS)
            .document(getCurrentUserID())
            .set(userHashMap, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the donor details.",
                    e
                )
            }
    }

    fun deleteDonorData(activity: Activity) {
        mFireStore.collection(Constants.DONORS)
            .document(getCurrentUserID())
            .delete()
            .addOnSuccessListener {
                when(activity) {

                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }

            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the donor details.",
                    e
                )
            }

    }

    fun donorAgreeAchievement(fragment: AchievementsFragment, donorHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.DONORS)
            .document(getCurrentUserID())
            .update(donorHashMap)
            .addOnSuccessListener {
                fragment.donorAchievementSuccess()
            }
            .addOnFailureListener { e ->


                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun getSearchDonorList(fragment: SearchDonorFragment) {
        mFireStore.collection(Constants.DONORS)
            .whereEqualTo(Constants.BLOOD_GROUP,fragment.autoCompleteTextView_get_blood_group.text.toString())
            .whereEqualTo(Constants.STATE, fragment.autoCompleteTextView_get_state.text.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                val searchDonorList: ArrayList<Donor> = ArrayList()
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        searchDonorList.add(dc.document.toObject(Donor::class.java))
                    }

                }
                when (fragment) {
                    is SearchDonorFragment -> {
                        fragment.successSearchDonorListFromStore(searchDonorList)
                    }

                    else -> {
                        SearchDonorFragment().hideProgressDialog()
                    }
                }


            }
    }

    fun getPatientsList(viewPatientsActivity: ViewPatientsActivity) {
        mFireStore.collection(Constants.USERS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                val userList: ArrayList<User> = ArrayList()
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {


                        userList.add(dc.document.toObject(User::class.java))
                    }

                }
                when (viewPatientsActivity) {
                    is ViewPatientsActivity -> {
                        viewPatientsActivity.successPatientsListFromStore(userList)
                    }

                    else -> {
                        viewPatientsActivity.hideProgressDialog()
                    }
                }


            }
    }

    fun getDonorsList(viewDonorsActivity: ViewDonorsActivity) {
        mFireStore.collection(Constants.DONORS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                val donorList: ArrayList<Donor> = ArrayList()
                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {

                        donorList.add(dc.document.toObject(Donor::class.java))
                    }

                }
                when (viewDonorsActivity) {
                    is ViewDonorsActivity -> {
                        viewDonorsActivity.successDonorListFromStore(donorList)
                    }

                    else -> {
                        SearchDonorFragment().hideProgressDialog()
                    }
                }


            }
    }


}