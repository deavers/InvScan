package com.example.invscan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.invscan.databinding.InvItemLayoutBinding
import com.example.invscan.domain.enteties.InvItemChecked
import com.example.invscan.interfaces.OnInvItemListener
import com.example.invscan.utils.getImgIdByCategory

class InvItemAdapter:RecyclerView.Adapter<InvItemAdapter.InvViewHolder>(){

    var onItemCheckListener:OnInvItemListener? = null
    var option = OPTION_DEFAULT;
        // see
    val listItems = mutableListOf<InvItemChecked>()

    class InvViewHolder(val binding: InvItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvViewHolder {
        val binding = InvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InvViewHolder(binding)
    }

/*    override fun onViewRecycled(holder: InvViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.apply {
            switchCheck.isChecked = false
            tvInvNum.text = ""
        }
    }*/

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<InvItemChecked>){
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: InvViewHolder, position: Int) {
        val invItem = listItems[position]
        holder.binding.apply {
            imgCategory.setImageResource(getImgIdByCategory(invItem.item.category_id))
            if (option == OPTION_DEFAULT){
                tvInvClassroomNum.visibility = View.INVISIBLE
                switchCheck.visibility = View.VISIBLE
                switchCheck.setOnClickListener {
                    onItemCheckListener?.onInvItemClick(invItem.item,switchCheck.isChecked)
                }
                switchCheck.isChecked = invItem.checked
            } else {
                tvInvClassroomNum.visibility = View.VISIBLE
                switchCheck.visibility = View.INVISIBLE
                tvInvClassroomNum.text = invItem.item.class_room_num
            }
            tvInvNum.text = invItem.item.inventory_num
        }
    }


    companion object {
         const val OPTION_DEFAULT = 1
         const val OPTION_WITHOUT_SWITCH = 2
    }

    override fun getItemCount(): Int {
        return listItems.size
    }


}