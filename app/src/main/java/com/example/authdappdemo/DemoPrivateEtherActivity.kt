package com.example.authdappdemo

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.wallet.LicenseSample
import com.example.authdappdemo.wallet.WalletConfigure
import com.example.authdappdemo.wallet.WalletManagerPrivateEthereumExtension


class DemoPrivateEtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_private_etheremu)
        initView()
        initCheckAuth()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_old_version).setOnClickListener {
            DemoMainActivity.start(this)
        }
        findViewById<Button>(R.id.btn_performance).setOnClickListener {
            PerformanceActivity.start(this)
        }
        findViewById<Button>(R.id.btn_import_license).setOnClickListener {
            importLicense()
        }
        findViewById<Button>(R.id.btn_check_balance).setOnClickListener {
            WalletManagerPrivateEthereumExtension.showBalancePrv(this)
        }
//        findViewById<Button>(R.id.btn_select_license).setOnClickListener {
//            selectFile()
//        }

        //
        var etLicense = findViewById<EditText>(R.id.et_license)
        etLicense.setText(LicenseSample.mLicense)
    }

    private fun importLicense() {
        var etLicense = findViewById<EditText>(R.id.et_license)
        val license = etLicense.text.toString()
//        if (TextUtils.isEmpty(license)) {
//            Toast.makeText(this, "license is null", Toast.LENGTH_LONG).show()
//            return
//        }

        WalletManagerPrivateEthereumExtension.importLicense(this, license)

        val etContractId = findViewById<TextView>(R.id.et_contract_id)
        etContractId.text = WalletManagerPrivateEthereumExtension.mContractId
    }

    private fun initCheckAuth() {
        val etContractId = findViewById<TextView>(R.id.et_contract_id)
        val etDeviceId = findViewById<TextView>(R.id.et_device_id)
        etDeviceId.text = WalletConfigure.mDeviceId

        findViewById<Button>(R.id.btn_auth_req).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)
            ) {
                return@setOnClickListener
            }

            try {
                WalletManagerPrivateEthereumExtension.reqAuth(this, deviceId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        findViewById<Button>(R.id.btn_auth_check).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)
            ) {
                return@setOnClickListener
            }

            try {
                WalletManagerPrivateEthereumExtension.checkAuth(this, deviceId)
            } catch (e: Exception) {
            }
        }
    }

//    private fun selectFile(){
//        showFileChooser()
//    }
//
//    private val FILE_SELECT_CODE = 0
//    private fun showFileChooser() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "*/*"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        try {
//            startActivityForResult(
//                Intent.createChooser(intent, "Select a File to Upload"),
//                FILE_SELECT_CODE
//            )
//        } catch (ex: ActivityNotFoundException) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private val TAG = "ChooseFile"
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        when (requestCode) {
//            FILE_SELECT_CODE -> if (resultCode == RESULT_OK) {
//                // Get the Uri of the selected file
//                val uri: Uri? = data.data
//                Log.d(TAG, "File Uri: " + uri.toString())
//                // Get the path
//                val path: String = getPath(this, uri)
//                Log.d(TAG, "File Path: $path")
//                // Get the file instance
//                // File file = new File(path);
//                // Initiate the upload
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    @Throws(URISyntaxException::class)
//    fun getPath(context: Context, uri: Uri): String? {
//        if ("content".equals(uri.scheme, ignoreCase = true)) {
//            val projection = arrayOf("_data")
//            var cursor: Cursor? = null
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null)
//                val column_index: Int? = cursor?.getColumnIndexOrThrow("_data")
//                if (cursor.moveToFirst()) {
//                    return column_index?.let { cursor.getString(it? })
//                }
//            } catch (e: java.lang.Exception) {
//                // Eat it  Or Log it.
//            }
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            return uri.path
//        }
//        return null
//    }
}