package com.example.bookmyhealth.ui.auth.user

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmyhealth.R
import com.example.bookmyhealth.databinding.ActivityAppointmentSlipBinding
import com.example.bookmyhealth.utils.QRUtils
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.OutputStream

class AppointmentSlipActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentSlipBinding
    private lateinit var database: DatabaseReference
    private val approvedAppointments = mutableListOf<DataSnapshot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentSlipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        database = FirebaseDatabase.getInstance().getReference("appointments")

        fetchAndShowSelectionDialog(uid)

        binding.btnDownloadSlip.setOnClickListener {
            val captureView = binding.slipLayoutContainer.slipContentLayout
            if (captureView.width > 0 && captureView.height > 0) {
                saveImageToGallery(screenShot(captureView))
            } else {
                Toast.makeText(this, R.string.select_appointment_first, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun fetchAndShowSelectionDialog(uid: String) {
        database.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    approvedAppointments.clear()
                    val options = mutableListOf<String>()

                    for (child in snapshot.children) {
                        if (child.child("status").value?.toString() == "Approved") {
                            val dr = child.child("doctorName").value?.toString() ?: "Unknown"
                            val date = child.child("date").value?.toString() ?: ""
                            options.add("Dr. $dr ($date)")
                            approvedAppointments.add(child)
                        }
                    }

                    if (options.isNotEmpty()) {
                        showDropdownDialog(options.toTypedArray())
                    } else {
                        Toast.makeText(
                            this@AppointmentSlipActivity,
                            R.string.no_approved_appointments,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showDropdownDialog(options: Array<String>) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_appointment_selection, null)

        val dialog = AlertDialog.Builder(this, R.style.TransparentDialog)
            .setView(dialogView)
            .create()

        dialog.setCancelable(false)

        val items = options.map { option ->
            val parts = option.removeSuffix(")").split(" (")
            Pair(parts.getOrElse(0) { option }, parts.getOrElse(1) { "" })
        }

        val rv = dialogView.findViewById<RecyclerView>(R.id.rvAppointments)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = AppointmentOptionAdapter(items) { position ->
            dialog.dismiss()
            updateSlipUI(approvedAppointments[position])
        }

        dialogView.findViewById<MaterialButton>(R.id.btnDialogBack)
            .setOnClickListener {
                dialog.dismiss()
                finish()
            }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun updateSlipUI(snapshot: DataSnapshot) {
        val docName  = snapshot.child("doctorName").value?.toString() ?: "N/A"
        val patName  = snapshot.child("userName").value?.toString()  ?: "User"
        val date     = snapshot.child("date").value?.toString()      ?: "N/A"
        val time     = snapshot.child("time").value?.toString()      ?: "N/A"
        val appId    = snapshot.key                                  ?: "N/A"
        val token    = snapshot.child("tokenNumber").value?.toString() ?: "01"

        binding.slipLayoutContainer.apply {
            tvDoctorName.text      = getString(R.string.doctor_name_format, docName)
            tvPatientName.text     = getString(R.string.patient_name_format, patName)
            tvDateTime.text        = getString(R.string.date_time_format, date, time)
            tvTokenNumber.text     = getString(R.string.token_format, token)
            tvAppointmentId.text   = getString(R.string.appointment_id_format, appId)
            tvStatus.text          = getString(R.string.status_approved)

            try {
                ivQRCode.setImageBitmap(QRUtils.generateQRCode(appId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun screenShot(view: View): Bitmap {
        val bitmap = createBitmap(view.width, view.height)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "BMH_Slip_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/BookMyHealth"
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val imageUri: Uri? = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            )

            fos = imageUri?.let { contentResolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(imageUri!!, contentValues, null, null)
            }

            Toast.makeText(this, R.string.slip_saved_success, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.save_failed, e.message), Toast.LENGTH_SHORT).show()
        } finally {
            fos?.close()
        }
    }
}

// Clean separate Adapter class — no unresolved references
class AppointmentOptionAdapter(
    private val items: List<Pair<String, String>>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AppointmentOptionAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvDoctor: TextView = view.findViewById(R.id.tvDoctorOption)
        val tvDate: TextView   = view.findViewById(R.id.tvDateOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_option, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (doctor, date) = items[position]
        holder.tvDoctor.text = doctor
        holder.tvDate.text   = date
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount() = items.size
}