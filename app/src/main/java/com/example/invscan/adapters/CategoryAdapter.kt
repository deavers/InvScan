package com.example.invscan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.invscan.databinding.CategoryItemBinding
import com.example.invscan.domain.enteties.CategoryWithCount
import com.example.invscan.utils.getImgIdByCategory

class CategoryAdapter:ListAdapter<CategoryWithCount,CategoryAdapter.CategoryViewHolder>(DiffCategoryCallback()) {

    class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = getItem(position)
        holder.binding.apply {
            imgCategory.setImageResource(getImgIdByCategory(categoryItem.id))
            tvCount.text = categoryItem.count.toString()
            tvNameCategory.text = categoryItem.name
        }
    }


}