package com.kathayat.testWeather.core.ui

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.devdigitalassignment.databinding.HourlyForecastRowBinding
import com.kathayat.testWeather.core.domain.model.forecast.ListForecast
import com.kathayat.testWeather.core.utils.DateFormatter
import com.kathayat.testWeather.core.utils.ForecastSort
import com.kathayat.testWeather.core.utils.getLottieSrc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HourlyAdapter: RecyclerView.Adapter<HourlyAdapter.DailyViewHolder>() {

    private var listForecast = ArrayList<ListForecast>()

    fun setData(data: List<ListForecast>?) {
        if (data==null) return

        listForecast.clear()
        listForecast.addAll(ForecastSort.sortHourly(data))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = HourlyForecastRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val data = listForecast[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listForecast.size

    inner class DailyViewHolder(private val binding: HourlyForecastRowBinding): RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(forecast: ListForecast) {
            binding.apply {
                val date = forecast.dateText?.let { DateFormatter.getDay(it) }
                tvHour.text = date
                tvTemp.text = forecast.main?.temp?.toInt().toString() + "°"
                val img = forecast.weather?.get(0)?.icon
                CoroutineScope(Dispatchers.Default).launch {
                    val path = getLottieSrc(img!!)
                    withContext(Dispatchers.Main) {
                        lavWeather.setAnimation(path)
                    }
                }
            }
        }

    }
}