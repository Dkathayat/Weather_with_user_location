package com.example.devdigitalassignment.core.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.devdigitalassignment.R
import com.example.devdigitalassignment.databinding.ItemHomeRecylerviewBinding
import com.kathayat.testWeather.core.data.source.local.entity.currentforecast.CurrentWeatherEntity
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather

class HomeRecylerView() :
    RecyclerView.Adapter<HomeRecylerView.HomeViewHolder>() {

    inner class HomeViewHolder(val binding: ItemHomeRecylerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(list: CurrentWeatherEntity){
                binding.recylerCityName.text = list.name
                binding.recylerTemp.text = list.main?.temp.toString()
            }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CurrentWeatherEntity>(){
        override fun areItemsTheSame(
            oldItem: CurrentWeatherEntity,
            newItem: CurrentWeatherEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CurrentWeatherEntity,
            newItem: CurrentWeatherEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@HomeRecylerView,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeRecylerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("CITY_NAME",differ.currentList[position].name)
            it.findNavController().navigate(R.id.weatherFragment)
        }
    }
}