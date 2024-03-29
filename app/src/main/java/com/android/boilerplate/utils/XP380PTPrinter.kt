package com.android.boilerplate.utils
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.OutputStream
import java.util.*

class XP380PTPrinter(activity: Activity) {

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private val deviceAddress = "86:67:7A:E6:54:76" // Replace with your printer's Bluetooth address

    init {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            activity.finish()
        }
    }

    @SuppressLint("MissingPermission")
    fun connectPrinter() {
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        try {
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            bluetoothSocket?.connect()
            outputStream = bluetoothSocket?.outputStream
        } catch (e: IOException) {
            e.printStackTrace()
            closePrinterConnection()
        }
    }

    fun printText(text: String) {
        try {
            outputStream?.write(text.toByteArray())
            outputStream?.write("\n".toByteArray()) // Print new line after text
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun printTextHeader(text: String, bold: Boolean = false, textSize: Int = 18) {
        try {
            // Print bold text if specified
            if (bold) {
                outputStream?.write(byteArrayOf(27, 69, 1)) // Enable bold
            }

            // Set text size
            outputStream?.write(byteArrayOf(27, 33, textSize.toByte()))

            // Print the text
            outputStream?.write(text.toByteArray())
            outputStream?.write("\n".toByteArray()) // Print new line after text

            // Reset text attributes
            outputStream?.write(byteArrayOf(27, 33, 0)) // Reset text size
            outputStream?.write(byteArrayOf(27, 69, 0)) // Disable bold
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun printTextNormalHeader(text: String, bold: Boolean = false, textSize: Int = 15) {
        try {
            // Print bold text if specified
            if (bold) {
                outputStream?.write(byteArrayOf(27, 69, 1)) // Enable bold
                outputStream?.write("\n".toByteArray())
            }

            // Set text size
            outputStream?.write(byteArrayOf(27, 33, textSize.toByte()))

            // Print the text
            outputStream?.write(text.toByteArray())

            // Reset text attributes
            outputStream?.write(byteArrayOf(27, 33, 0)) // Reset text size
            outputStream?.write(byteArrayOf(27, 69, 0)) // Disable bold
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun closePrinterConnection() {
        try {
            outputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
