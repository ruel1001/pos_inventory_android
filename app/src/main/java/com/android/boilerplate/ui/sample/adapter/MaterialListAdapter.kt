package com.android.boilerplate.ui.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse.MaterialListData
import com.android.boilerplate.databinding.AdapterPaymentHeaderLayoutBinding
import com.android.boilerplate.databinding.AdapterRowPaymentLayoutBinding


class MaterialListAdapter(val context: Context, val clickListener: ArticleCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ROW = 1
    private val adapterData = mutableListOf<MaterialListData>()

    fun clear() {
        adapterData.clear()
        notifyDataSetChanged()
    }

    fun appendData(newData: List<MaterialListData>) {
        val startAt = adapterData.size
        adapterData.addAll(newData)
        notifyItemRangeInserted(startAt, newData.size)
    }

    fun getData(): MutableList<MaterialListData> = adapterData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = AdapterPaymentHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            TYPE_ROW -> {
                val binding = AdapterRowPaymentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RowViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bindHeader()
            is RowViewHolder -> holder.bindRow(adapterData[position - 1]) // Adjust position to account for header
        }
    }

    override fun getItemCount(): Int = adapterData.size + 1 // Add 1 for the header

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_ROW
        }
    }

    inner class HeaderViewHolder(val binding: AdapterPaymentHeaderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindHeader() {
            binding.headerTextView.text="Name"
            binding.header2TextView.text="Quantity"
            binding.header3TextView.text="Amount"
            // Bind header data if needed
        }
    }

    inner class RowViewHolder(val binding: AdapterRowPaymentLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindRow(data: MaterialListData) {
            // Bind row data
            binding.paymentIdTextView.text = data.material_name
            binding.billingMonthTextView.text = data.amount
            binding.amountPaidMonthTextView.text = data.quantity
            binding.articleLinearLayout.setOnClickListener {
                clickListener.onItemClicked(data)
            }
        }
    }

    interface ArticleCallback {
        fun onItemClicked(data: MaterialListData)
    }
}