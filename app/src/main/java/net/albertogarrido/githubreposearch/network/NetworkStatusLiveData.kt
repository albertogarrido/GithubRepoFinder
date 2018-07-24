package net.albertogarrido.githubreposearch.network

import android.arch.lifecycle.LiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkStatusLiveData(private val context: Context) : LiveData<Boolean>() {

    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onActive() {
        super.onActive()
        registerBroadCastReceiver()
    }

    override fun onInactive() {
        super.onInactive()
        cancelSubscription()
    }

    fun cancelSubscription() {
        unRegisterBroadCastReceiver()
    }

    private fun registerBroadCastReceiver() {
        if (broadcastReceiver == null) {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val extras = intent.extras
                    val info = extras!!.getParcelable<NetworkInfo>("networkInfo")
                    value = info != null && info.state == NetworkInfo.State.CONNECTED
                }
            }
            context.registerReceiver(broadcastReceiver, filter)
        }
    }

    private fun unRegisterBroadCastReceiver() {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }

    companion object {

        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}