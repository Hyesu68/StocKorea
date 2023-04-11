package com.susuryo.stockorea.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.susuryo.stockorea.databinding.SearchItemBinding
import com.susuryo.stockorea.model.StockPrice
import com.susuryo.stockorea.model.StockPriceResponse

class EnterpricesAdapter(var enterprices: ArrayList<StockPrice>): RecyclerView.Adapter<EnterpricesAdapter.ViewHolder>() {

        private var ids = mutableListOf<String>()

        fun setList(newItems: StockPriceResponse) {
            enterprices.clear()
            ids.clear()
            val items = newItems.response.body.items.item
            for (item in items) {
                if (!ids.contains(item.isinCd)) {
                    enterprices.add(item)
                    ids.add(item.isinCd)
                }
            }
            notifyDataSetChanged()
        }

        class ViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                with(enterprices[position]) {
                    binding.textview.text = this.itmsNm
                    binding.textview.setOnClickListener {
                        val intent = Intent(binding.root.context, DetailActivity::class.java)
                        intent.putExtra("isinCd", this.isinCd)
                        intent.putExtra("name", this.itmsNm)
                        binding.root.context.startActivity(intent)
                    }
                    binding.star.setOnCheckedChangeListener { buttonView, isChecked ->

                    }
                }
            }
        }

        override fun getItemCount() = enterprices.size
}