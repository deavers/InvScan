package com.example.invscan.interfaces

import com.example.invscan.domain.enteties.Classroom

interface OnClassroomListener {
    fun onClassroomClick(classroom: Classroom)
}