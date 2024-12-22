package com.example.concertix.ui.payment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.concertix.R
import com.example.concertix.Util
import com.example.concertix.Util.formatDate
import com.example.concertix.databinding.ActivityPaymentBinding
import com.example.concertix.model.Ticket
import com.example.concertix.model.Transaction
import com.example.concertix.ui.transactiondetail.TransactionDetailActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.util.jar.Manifest

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var captureImageLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private var eventLocation: String = ""
    private var eventTime: String = ""
    private var eventType: String = ""
    private var total: Long = 0L
    private var eventDate: String = ""
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventName = intent.getStringExtra("event_name").toString()
        eventDate = intent.getStringExtra("event_date").toString()
        val eventPlace = intent.getStringExtra("event_place").toString()
        eventLocation = intent.getStringExtra("event_location").toString()
        eventTime = intent.getStringExtra("event_time").toString()
        eventType = intent.getStringExtra("event_type").toString()
        val ticket = intent.getParcelableExtra<Ticket>("ticket")

        if (ticket != null) {
            with(binding) {
                tvEventName.text = eventName
                tvDate.text = eventDate.formatDate()
                tvLocation.text = eventPlace
                tvType.text = ticket.type.toString()

                val ticketDetail = "${ticket.type.toString()} x 1 = ${Util.formatCurrency(ticket.price ?: 0)}"
                tvTicketDetail.text = ticketDetail

                val tax = 10 * (ticket.price ?:0) / 100
                val taxText = "VAT(10%) = ${Util.formatCurrency(tax)}"
                tvTax.text = taxText

                total = (ticket.price ?: 0L) + tax + 2000
                val totalText = "GRAND TOTAL = ${Util.formatCurrency(total)}"
                tvTotal.text = totalText

                btnBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

                btnNext.isEnabled = false
            }
        }

        captureImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Display the image in the ImageView
                binding.ivKtp.setImageURI(imageUri)
                binding.btnNext.apply {
                    isEnabled = true
                    setOnClickListener {
                        showPaymentOptions()
                    }
                }

            }
        }

        binding.btnUpload.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                requestPermissions()
            }
        }
    }

    private fun showPaymentOptions() {
        with(binding) {
            ivKtp.visibility = View.GONE
            btnUpload.visibility = View.GONE
            txtPaymentMethod.visibility = View.VISIBLE
            cardPaymentMethod.visibility = View.VISIBLE

            btnNext.text = "Buy Tickets"
            btnNext.isEnabled = false

            var selectedPaymentMethod = ""

            rbBca.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPaymentMethod = "BCA"
                    rbDana.isChecked = false
                    btnNext.isEnabled = true
                } else {
                    selectedPaymentMethod = ""
                    btnNext.isEnabled = false
                }
            }

            rbDana.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPaymentMethod = "DANA"
                    rbBca.isChecked = false
                    btnNext.isEnabled = true
                } else {
                    selectedPaymentMethod = ""
                    btnNext.isEnabled = false
                }
            }

            layoutBca.setOnClickListener {
                rbBca.isChecked = true
            }

            layoutDana.setOnClickListener {
                rbBca.isChecked = true
            }

            btnNext.setOnClickListener {
                val ref = Firebase.database.getReference()
                getCurrentUser()?.let { user ->
                    val transactionId = ref.child(user.uid).child("transaction").push().key
                    val transaction = Transaction(
                        id = transactionId,
                        amount = Util.formatCurrency(total),
                        wallet = selectedPaymentMethod,
                        ticketType = tvType.text.toString(),
                        eventName = tvEventName.text.toString(),
                        eventDate = eventDate,
                        eventTime = eventTime,
                        eventPlace = tvLocation.text.toString(),
                        eventLocation = eventLocation,
                        eventType = eventType
                    )
                    transactionId?.let { id ->
                        ref.child(user.uid).child("transaction").child(id).setValue(transaction)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@PaymentActivity, TransactionDetailActivity::class.java)
                                    intent.putExtra("transaction", transaction)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    }
                }
            }
        }
    }

    // Check if camera permission is granted
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Request camera permission if not granted
    private fun requestPermissions() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    // Open the camera and create a file to store the image
    private fun openCamera() {
        // Create a temporary file for the photo
        val photoFile = createImageFile()

        // Use FileProvider to get a content URI
        imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)

        // Launch the camera intent using the ActivityResultLauncher
        imageUri?.let {
            captureImageLauncher.launch(it)
        }
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    // Create a temporary image file
    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile("temp_image", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun getCurrentUser() : FirebaseUser? {
        val auth = Firebase.auth
        return auth.currentUser
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}