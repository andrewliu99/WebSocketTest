package com.example.codingtest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codingtest.databinding.ActivityMainBinding
import com.example.codingtest.retrofit.IDataService
import com.example.codingtest.retrofit.RetrofitManager
import com.example.codingtest.ui.DataAdapter
import com.example.codingtest.util.SingleLiveEvent
import com.example.codingtest.websocket.MessageListener
import com.example.codingtest.websocket.WebSocketManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import java.lang.ref.WeakReference


var contextRef: WeakReference<Context>? = null
var dataService: IDataService? = null

class MainActivity : AppCompatActivity(), MessageListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var recyclerView: RecyclerView?= null
    var gson = Gson()

    val backgroundScope = CoroutineScope(Dispatchers.IO + Job() + errorHandler)
    val mainScope = CoroutineScope(Dispatchers.Main + Job() + errorHandler)

    private var websocketURL = "wss://stream.yshyqxx.com/ws/btcusdt@aggTrade"

    val parsingComplete = SingleLiveEvent<List<Payload>>()
    val updateDataComplete = SingleLiveEvent<List<Payload>>()
    var dataArrayList: MutableList<Payload> = arrayListOf()

    companion object {
        val errorHandler = CoroutineExceptionHandler { _, error ->
            error.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contextRef = WeakReference(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        recyclerView = binding.recyclerView

        dataService = RetrofitManager.instance.api

        backgroundScope.launch {
            retrievedBaseData()
        }

        WebSocketManager.init(websocketURL, this)
    }

    override fun onResume() {
        super.onResume()

        if (!WebSocketManager.isConnect()) {
            WebSocketManager.connect()
        }

        parsingComplete.observe(this) {
            updateUI(it)
        }

        updateDataComplete.observe(this) {
            refreshUI(it)
        }
    }

    override fun onPause() {
        super.onPause()
        WebSocketManager.close()
    }

    override fun onDestroy() {
        WebSocketManager.close()
        mainScope.cancel()
        backgroundScope.cancel()
        super.onDestroy()
    }

    private fun retrievedBaseData() {
        dataService?.getPayloads("BTCUSDT", 40)!!.enqueue(object :
            Callback<List<Payload>> {
            override fun onResponse(
                call: Call<List<Payload>>,
                response: retrofit2.Response<List<Payload>>
            ) {
                dataArrayList = response.body() as MutableList<Payload>
                parsingComplete.postValue(dataArrayList)
            }

            override fun onFailure(call: Call<List<Payload>>, t: Throwable?) {
                val int = 0
            }
        })
    }

    private fun updateUI(list: List<Payload>) {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = DataAdapter(list)
    }

    private fun refreshUI(list: List<Payload>) {
        val adapter = recyclerView!!.adapter as DataAdapter
        adapter.setData(dataArrayList)
        adapter.notifyDataSetChanged()
    }

    override fun onConnectSuccess() {
        Log.d("DEBUG", "Connected!!")
    }

    override fun onConnectFailed() {
        Log.d("DEBUG", "Connect failed")
    }

    override fun onClose() {
        Log.d("DEBUG", "Closed!!")
    }

    override fun onMessage(text: String?) {
        Log.d("DEBUG", text!!)
        val payload: JsonObject = gson.fromJson(text, JsonObject::class.java)
        dataArrayList.removeAt(dataArrayList.size - 1)
        dataArrayList.add(0, Payload(payload.get("a").asInt,
            payload.get("p").asString,
            payload.get("q").asString,
            payload.get("f").asInt,
            payload.get("l").asInt,
            payload.get("T").asLong,
            payload.get("m").asBoolean,
            payload.get("M").asBoolean))

        updateDataComplete.postValue(dataArrayList)
    }
}