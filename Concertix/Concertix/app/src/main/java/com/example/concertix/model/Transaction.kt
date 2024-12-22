package com.example.concertix.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Transaction(
    val id: String? = null,
    val amount: String? = null,
    val wallet: String? = null,
    val ticketType: String? = null,
    val eventName: String? = null,
    val eventDate: String? = null,
    val eventTime: String? = null,
    val eventPlace: String? = null,
    val eventLocation: String? = null,
    val eventType: String? = null
) : Parcelable