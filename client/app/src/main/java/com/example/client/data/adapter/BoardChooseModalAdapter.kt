package com.example.client.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.data.AppDatabase
import com.example.client.data.Detail
import com.example.client.databinding.ItemRecordBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class BoardChooseModalAdapter(private val context: Context, private val recordList:List<Detail>): RecyclerView.Adapter<BoardChooseModalAdapter.ItemViewHolder>() {
    private var selectedItemPosition = 0
    private var selectedItem: View? = null

    val roomDb = AppDatabase.getInstance(context)

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(v: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mListener=listener
    }

    inner class ItemViewHolder(private val binding: ItemRecordBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Detail){
            binding.tvTime.text=data.time
            binding.tvMoney.text=data.price.toString()
            binding.tvMemo.text=data.memo
            binding.tvName.text=data.shop

            /*
            if (roomDb != null) {
                binding.icon.setImageResource(roomDb.CategoryDao().selectById(data.categoryId).image)
            } else{
                when(data.typeId){
                    1 -> binding.icon.setImageResource(R.drawable.ic_category_user)
                    else -> binding.icon.setImageResource(R.drawable.ic_category_income_user)
                }
            }

             */

            binding.highlight.visibility=View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(recordList[position])

        holder.itemView.isSelected = position == selectedItemPosition
        if(position==0){selectedItem=holder.itemView} //창이 처음 켜지면 첫번째 아이템이 클릭되도록 설정

        // 대표 내역 선택
        holder.itemView.setOnClickListener{
            mListener.onItemClick(it,position) //클릭한 아이템 다른 클래스로 넘겨주기

            val currentPosition = holder.adapterPosition

            //선택되었던 아이템 클릭 해제
            selectedItem?.isSelected=false
            selectedItemPosition = currentPosition
            //클릭한 아이템 클릭 설정
            selectedItem = holder.itemView
            selectedItem?.isSelected=true

        }

    }

    override fun getItemCount(): Int {
        return recordList.size
    }
}