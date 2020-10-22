package com.example.authdappdemo

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
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        view.findViewById<Button>(R.id.button_test).setOnClickListener {
            TestJson.doTest()
        }
        view.findViewById<Button>(R.id.button_test_aes).setOnClickListener {
            TestAES.doTest()
        }
        view.findViewById<Button>(R.id.button_test_rsa).setOnClickListener {
            val intent = Intent(context, TestRSActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.button_test_sec_sp).setOnClickListener {
            context?.let { it1 -> TestSecSPActivity.start(it1) }
        }
        view.findViewById<Button>(R.id.button_test_keystore).setOnClickListener {
            showUUID()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showUUID() {
        val id = TestKeyStore.getUUID()
        Toast.makeText(context, "uuid:$id", Toast.LENGTH_LONG).show()
    }

}