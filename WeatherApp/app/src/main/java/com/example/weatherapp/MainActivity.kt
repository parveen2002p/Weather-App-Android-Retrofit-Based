package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.round


// Tmp@123@Tmp
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var temperatureRepository: TemperatureRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val temperatureDatabase = Room.databaseBuilder(
                        applicationContext,
                        TemperatureDatabase::class.java, "item_database"
                    ).build()

                    temperatureRepository = TemperatureRepository(temperatureDatabase.temperatureDao())


                    Greeting("Android", viewModel = viewModel,temperatureRepository,applicationContext)
                }
            }
        }
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, viewModel: MainViewModel, temperatureRepository: TemperatureRepository,context: Context, modifier: Modifier = Modifier) {

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate: String by remember { mutableStateOf("") }
    val minTemp = remember { mutableStateOf<Double?>(null) }
    val maxTemp = remember { mutableStateOf<Double?>(null) }
    var minTempDisplay : Double by remember { mutableDoubleStateOf(0.0) }
    var maxTempDisplay : Double by remember { mutableDoubleStateOf(0.0) }

    var isInternetAvailable by remember { mutableStateOf(false) }
    var isTriggerOn by remember { mutableStateOf(false) }

    DisposableEffect(key1 = context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                isInternetAvailable = true
            }

            override fun onLost(network: android.net.Network) {
                isInternetAvailable = false
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)


        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            //.border(1.dp, Color.DarkGray)
            //.height(330.dp)
            //.width(270.dp)
            //.padding(17.dp, 14.dp, 14.dp, 14.dp)
                ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

//                    .background(
//                        Color.LightGray
//                    )
    ) {

        if (isInternetAvailable) {
            Text("Status: Online",
                modifier = Modifier
                    .padding(4.dp)
                    //.border(3.dp, Color.Black)
                    //.fillMaxWidth()
                    .background(Color.Green),
                fontSize = 15.sp,
                //lineHeight = 116.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,)
        } else {
            Text("Status: Offline",
                modifier = Modifier
                    .padding(4.dp)
                    //.border(3.dp, Color.Black)
                    //.fillMaxWidth()
                    .background(Color.Red),
                fontSize = 15.sp,
                //lineHeight = 116.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,)
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "Location: Jaipur, India",
                modifier = Modifier
                    .padding(4.dp)
                    .background(color = Color.Gray)
                    //.border(3.dp, Color.Black)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                fontSize = 20.sp,
                //lineHeight = 116.sp,
                textAlign = TextAlign.Left,
                color = Color.Blue,
                style = MaterialTheme.typography.titleLarge,
            )
        Spacer(
            modifier = Modifier
                .height(50.dp)
                //.border(2.dp, Color.Black)
                .width(25.dp)
        )




//        Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
//            modifier = Modifier
//                //.border(2.dp, Color.Black)
//                .fillMaxWidth()
//                .padding(4.dp))
//        {


            Text(

                if (selectedDate == "" || !isTriggerOn) "Min Temperature: - -" else "Min Temperature: ${round(((minTempDisplay - 32) * 5 / 9)*10.0)/10.0}°C",
                modifier = Modifier
                    .padding(4.dp)
                    .background(color = Color.Gray)
                    //.border(3.dp, Color.Black)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                fontSize = 28.sp,
                //lineHeight = 56.sp,
                textAlign = TextAlign.Left,
                color = Color.Blue,
                style = MaterialTheme.typography.titleLarge,
                //fontWeight = 45,
            )
        Text(
            if (selectedDate == "" || !isTriggerOn) "Max Temperature: - -" else "Max Temperature: ${round(((maxTempDisplay - 32) * 5 / 9)*10.0)/10.0}°C",
                modifier = Modifier
                    .padding(4.dp)
                    .background(color = Color.Gray)
                    //.border(3.dp, Color.Black)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                fontSize = 28.sp,
                //lineHeight = 116.sp,
                textAlign = TextAlign.Left,
                color = Color.Blue,
                style = MaterialTheme.typography.titleLarge,
            )


        Spacer(
            modifier = Modifier
                .height(100.dp)
                //.border(2.dp, Color.Black)
                .width(25.dp)
        )

        Text(text = "Selected Date: $selectedDate",
            modifier = Modifier
                .padding(4.dp)
                .background(color = Color.Gray)
                //.border(3.dp, Color.Black)
                .fillMaxWidth()
                .background(Color.LightGray),
            fontSize = 20.sp,
            //lineHeight = 116.sp,
            textAlign = TextAlign.Left,
            color = Color.Blue,
            style = MaterialTheme.typography.bodyLarge,)

        Spacer(
            modifier = Modifier
                .height(20.dp)
                //.border(2.dp, Color.Black)
                .width(25.dp)
        )



        Button(modifier = Modifier
            //.align(Alignment.Center)
            .fillMaxWidth()
            .padding(5.dp)
            .height(50.dp)
            .width(140.dp)
            //.border(2.dp, Color.Black)
        ,enabled = true,
            onClick = {
                showDatePicker = true
            }) {
            Text(text = "Pick a date")
            Modifier
                .align(alignment = Alignment.Bottom)
                //.border(2.dp, Color.Black)
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        Box(modifier = Modifier
            //.border(2.dp, Color.Black)
            .fillMaxWidth()) {
            //modifier=modifier.height(5.dp).width(40.dp).padding(start=160.dp,bottom=.160.dp, end = 20.dp)
            Button(modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp)
                .height(50.dp)
                .width(240.dp)
                //.border(2.dp, Color.Black)
                ,enabled = true
                ,onClick = {

                    if (selectedDate!=""){


                        GlobalScope.launch(Dispatchers.Main) {

                            try {
                                if(isFutureDate(selectedDate)){

                                    var minTempSum = 0.0
                                    var maxTempSum = 0.0
                                    var totalCount = 0
                                    var flag:Boolean=false
                                    for (count in 1..10) {

                                        val currentDate = Calendar.getInstance()
                                        currentDate.add(Calendar.YEAR, -count)
                                        val year = currentDate.get(Calendar.YEAR)

                                        val (yearStr, monthStr, dayStr) = selectedDate.split("-")

                                        val month = monthStr.toInt()
                                        val day = dayStr.toInt()
                                        val modifiedDate = "$year-$month-$day"
                                        Log.d("MainActivity", "Item: ${modifiedDate}")

                                        val (minTemp2, maxTemp2) = fetchWeatherForDate(
                                            "Jaipur",
                                            modifiedDate,
                                            //V6R7Z4EA5HKWDEBSW5BYTUEDA NEW KEY
                                            // "VE4LXMFCX6V78HLBDJX3P5QDS"
                                            "VE4LXMFCX6V78HLBDJX3P5QDS"
                                        ) ?: return@launch

                                        Log.d("MainActivity", "Item2: ${modifiedDate}")

                                        if (minTemp2 ==null || maxTemp2==null){
                                            Log.d("MainActivity", "Got null value ")
                                            flag=true
                                        }

                                        if (minTemp2 != null) {
                                            minTempSum += minTemp2
                                        }
                                        if (maxTemp2 != null) {
                                            maxTempSum += maxTemp2
                                        }
                                        totalCount++
                                    }
                                    if (flag==false) {
                                        minTemp.value = minTempSum / totalCount
                                        maxTemp.value = maxTempSum / totalCount
                                        minTempDisplay = minTemp.value!!
                                        maxTempDisplay = maxTemp.value!!
                                        isTriggerOn=true
                                    }
                                    else{
                                        showToast(context, "Unable to fetch enough data for forecasting, Try later")
                                    }
                                }
                                else {
                                    var flag=false
                                    val (minTemp2, maxTemp2) = fetchWeatherForDate(
                                        "Jaipur",
                                        selectedDate,
                                        "VE4LXMFCX6V78HLBDJX3P5QDS"
                                    ) ?: return@launch

                                    if (minTemp2 ==null || maxTemp2==null){
                                        flag=true
                                    }

                                    // Update the temperatures in the UI thread
                                    //GlobalScope.launch(Dispatchers.Main) {
                                    if (flag==false) {
                                        minTemp.value = minTemp2
                                        maxTemp.value = maxTemp2
                                        minTempDisplay = minTemp.value!!
                                        maxTempDisplay = maxTemp.value!!
                                        isTriggerOn=true
                                    }
                                    else{
                                        showToast(context, "Unable to fetch data, Try later")
                                    }
                                }
                                //}
                            } catch (e: Exception) {
                                var tp = e.message

                                Log.d("MainActivity", "$tp")

                                Log.d("MainActivity", "Exception throwed ")
                                showToast(context, "Some error occurred while requesting to server")
                                // Handle any errors
                            }
                        }
                    }
                    else{
                        showToast(context, "Please select a date")
                    }
                }) {
                Text("Fetch weather updates")
                Modifier
                    .align(alignment = Alignment.Bottom)
                    //.border(2.dp, Color.Black)
            }
        }

        LaunchedEffect(maxTemp,minTemp,maxTemp.value,minTemp.value) { // && !isFutureDate(selectedDate)
            if (minTemp.value != null && maxTemp.value != null && selectedDate!="" ) {

                val (yearStr, monthStr, dayStr) = selectedDate.split("-")
                val year = yearStr.toInt()
                val month = monthStr.toInt()
                val day = dayStr.toInt()

                val temperatureData = TemperatureData(
                    day = day,
                    month = month,
                    year = year,
                    maxTemp = maxTemp.value!!,
                    minTemp = minTemp.value!!
                )
                CoroutineScope(Dispatchers.Main).launch {
                    if (!temperatureRepository.hasTemperatureDataForDate(day, month, year)) {
                        temperatureRepository.insertTemperatureData(temperatureData)
                    }
                    //val items = temperatureRepository.getTemperatureForDate(day, month, year)
                    //if (items != null) {
                       // minTempDisplay = minTemp.value!!
                        //maxTempDisplay = maxTemp.value!!
                      //  Log.d("MainActivity", "ItemDP: ${items.minTemp}")
                   // }
                }

            }
            }

        LaunchedEffect(selectedDate) {
        if (selectedDate != "") {
            val (yearStr, monthStr, dayStr) = selectedDate.split("-")
            val year = yearStr.toInt()
            val month = monthStr.toInt()
            val day = dayStr.toInt()

            CoroutineScope(Dispatchers.Main).launch {
                if (isFutureDate(selectedDate)){
                    val isPresent= temperatureRepository.hasTemperatureDataForDate(day, month, year)
                    if (isPresent) {
                        val items = temperatureRepository.getTemperatureForDate(day, month, year)
                        if (items != null) {
                            minTempDisplay = items.minTemp
                            maxTempDisplay = items.maxTemp
                            isTriggerOn=true
                            //Log.d("MainActivity", "ItemDP: ${items.minTemp}")
                        }
                        else {
                            // Handle the case where there are not enough years present in the table
//                        println("Not enough years present in the table")
                            isTriggerOn=false
                            showToast(context, "Not enough offline data available to forecast, Try fetching from internet")
                        }
                    }
                    else {
                        val items =
                            temperatureRepository.getAverageTemperatureForLast10Years(day, month)
                        if (items.isNotEmpty() && items.count()==10) {
                            minTempDisplay = items.map { it.minTemp }.average()
                            maxTempDisplay =  items.map { it.maxTemp}.average()
                            isTriggerOn = true
                        } else {
                            // Handle the case where there are not enough years present in the table
//                        println("Not enough years present in the table")
                            isTriggerOn = false
                            showToast(
                                context,
                                "Not enough offline data available to forecast, Try fetching from internet"
                            )
                        }
                    }
                }
                else {
                    val items = temperatureRepository.getTemperatureForDate(day, month, year)
                    if (items != null) {
                        minTempDisplay = items.minTemp
                        maxTempDisplay = items.maxTemp
                        isTriggerOn=true
                        Log.d("MainActivity", "ItemDP: ${items.minTemp}")
                    }
                    else if(items==null && isInternetAvailable==false){
                        isTriggerOn=false
                        showToast(context, "No offline data available")
                    }
                }
            }
        }


        }
        Spacer(modifier = Modifier.height(16.dp))
//        Text("Min Temperature: ${minTemp.value} F")
//        Text("Max Temperature: ${maxTemp.value} F")



//        items.forEach { item ->
//            Log.d("MainActivity", "Item: ${item.id}, Name: ${item.name}, Quantity: ${item.quantity}")
//        }


//        Row (horizontalArrangement = Arrangement.spacedBy(1.dp),
//            modifier = Modifier
//                .border(2.dp, Color.Black)
//                .fillMaxWidth()
//                .padding(4.dp)){
        Image(
            painter = painterResource(R.drawable.set_weather_doodles_illustration_6997_1863),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(1.dp)
        )
        //}


        }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(
                    onClick = {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        }
                        // Formatting  the selected date as a string
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        selectedDate = dateFormat.format(calendar.time)
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) { Text("Cancel") }
            }
        )
        {
            DatePicker(state = datePickerState)
        }

    }


    // if future date & net on then fetch 10 entries & insert & return (only 1 insert)
    // show net on/ off availibility
    // directly fetch click -> toast date not selected
    // click on fetch -> toast may

    // calc farhenheit to cels
    // json file


}

fun isFutureDate(selectedDate: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = Calendar.getInstance().time // Get the current date

    // Parse the selected date string into a Date object
    val parsedDate = dateFormat.parse(selectedDate)
    return parsedDate.after(currentDate) // Check if the selected date is after the current date
}

data class WeatherResponse(
    val list: List<WeatherData>
)

data class WeatherData(
    val main: TemperatureInfo,
    val dt_txt: String
)

data class TemperatureInfo(
    val temp_min: Double,
    val temp_max: Double
)

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


// b1605b4f5580130789eb3ced19686ca0   API KEY


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    WeatherAppTheme {
//        Greeting("Android", viewModel = MainViewModel())
//    }
//}