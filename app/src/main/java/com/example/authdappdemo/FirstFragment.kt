package com.example.authdappdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.authdappdemo.wallet.WalletManager

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<Button>(R.id.btnBalance).setOnClickListener {
            WalletManager.showBalance(context)
        }

        view.findViewById<Button>(R.id.btnAddress).setOnClickListener {
            WalletManager.showAddress(context)
        }

        view.findViewById<Button>(R.id.btnTransfer).setOnClickListener {
            WalletManager.doTransfer(context)
        }

        view.findViewById<Button>(R.id.btnAuthRequest).setOnClickListener {
            WalletManager.requestAuth(context)
        }

        view.findViewById<Button>(R.id.btnAuthCheck).setOnClickListener {
            var authResult = WalletManager.checkAuth()
            if (authResult){
                Toast.makeText(context, "有权限", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "无权限", Toast.LENGTH_LONG).show()
            }
        }
    }
}