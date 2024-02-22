package com.android.boilerplate.ui.sample.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.boilerplate.R
import com.android.boilerplate.data.repositories.article.response.ArticleData
import com.android.boilerplate.data.repositories.customers.response.SearchListNameData
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse.MaintenanceSearchData

import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse.ShowPaymentData
import com.android.boilerplate.databinding.AdapterArticleBinding
import com.android.boilerplate.databinding.AdapterCustomersHeaderLayoutBinding
import com.android.boilerplate.databinding.AdapterPaymentHeaderLayoutBinding
import com.android.boilerplate.databinding.AdapterPaymentListNoBinding
import com.android.boilerplate.databinding.AdapterRowCustomerListLayoutBinding
import com.android.boilerplate.databinding.AdapterRowPaymentLayoutBinding
import com.android.boilerplate.utils.loadImage




class CustomerNamesAdapter(val context: Context, val clickListener: ArticleCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ROW = 1
    private val adapterData = mutableListOf<SearchListNameData>()

    fun clear() {
        adapterData.clear()
        notifyDataSetChanged()
    }

    fun appendData(newData: List<SearchListNameData>) {
        val startAt = adapterData.size
        adapterData.addAll(newData)
        notifyItemRangeInserted(startAt, newData.size)
    }

    fun getData(): MutableList<SearchListNameData> = adapterData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = AdapterCustomersHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            TYPE_ROW -> {
                val binding = AdapterRowCustomerListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class HeaderViewHolder(val binding: AdapterCustomersHeaderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindHeader() {
          //  binding.headerTextView.text="Name"
          //  binding.header2TextView.text="Material Used"
          //  binding.header3TextView.text="Nature of Repair"
            // Bind header data if needed
        }
    }

    inner class RowViewHolder(val binding: AdapterRowCustomerListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindRow(data: SearchListNameData) {
            // Bind row data
            binding.paymentIdTextView.text = data.account_number.toString()
            binding.billingMonthTextView.text = data.address
            binding.amountPaidMonthTextView.text = data.account_name


            try {
                val balance = data.account_balance?.toInt() ?: 0
                when {
                    balance > 0 -> {
                        binding.articleLinearLayout.setBackgroundColor(Color.RED)
                    }
                    balance < 0 -> {
                        binding.articleLinearLayout.setBackgroundColor(Color.GREEN)
                    }
                    else -> {
                        // Handle case where balance is zero
                    }
                }
            } catch (e: NumberFormatException) {
                // Handle the case where account_balance is not a valid integer
                // For example, you might want to log the error or show a message to the user
                // e.printStackTrace() // Uncomment this line to print the stack trace
            }

            binding.articleLinearLayout.setOnClickListener {
                clickListener.onItemClicked(data)
            }
        }
    }

    interface ArticleCallback {
        fun onItemClicked(data: SearchListNameData)
    }
}