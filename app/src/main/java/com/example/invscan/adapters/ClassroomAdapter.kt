package com.example.invscan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.invscan.databinding.ClassroomItemBinding
import com.example.invscan.domain.enteties.Classroom

class ClassroomAdapter:ListAdapter<Classroom,ClassroomAdapter.ClassroomViewHolder>(DiffClassroomCallback()) {

    class ClassroomViewHolder(val binding: ClassroomItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        val binding = ClassroomItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassroomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        val classroom = getItem(position)
        holder.binding.tvClassroom.text = classroom.name
    }


}