package com.example.invscan.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.invscan.domain.enteties.CategoryWithCount

class DiffCategoryCallback:DiffUtil.ItemCallback<CategoryWithCount>() {
    override fun areItemsTheSame(oldItem: CategoryWithCount, newItem: CategoryWithCount): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryWithCount,
        newItem: CategoryWithCount
    ): Boolean {
        return oldItem == newItem
    }

}