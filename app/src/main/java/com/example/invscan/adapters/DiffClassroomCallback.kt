package com.example.invscan.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.invscan.domain.enteties.Classroom

class DiffClassroomCallback:DiffUtil.ItemCallback<Classroom>() {
    override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem.num == newItem.num
    }

    override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem == newItem
    }
}