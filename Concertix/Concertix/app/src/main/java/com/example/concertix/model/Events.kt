package com.example.concertix.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Events(
    val eventName: String? = null,
    val eventType: String? = null,
    val eventBanner: String? = null,
    val eventImage: String? = null,
    val date: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val placeName: String? = null,
    val placeLocation: String? = null,
    val placeLatitude: String? = null,
    val placeLongitude: String? = null,
    val tickets: List<Ticket>? = null,
    val genre: String? = null,
    val description: String? = null
): Parcelable {

}

@IgnoreExtraProperties
@Parcelize
data class Ticket(
    val type: String? = null,
    val description: String? = null,
    val price: Long? = null,
    val remaining: String? = null,
): Parcelable
