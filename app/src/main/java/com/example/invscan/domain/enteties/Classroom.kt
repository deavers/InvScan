package com.example.invscan.domain.enteties

data class Classroom(
    val num:String,
    val name:String,
)

data class ClassroomWithItems(
    val num:String,
    val name:String,
    val items: List<InventoryItem>? = null
)

data class GetAllClassroomsResponse(val status:Boolean,val message:String,val classrooms:List<Classroom>?)

data class GetClassroomResponse(val status:Boolean,val message:String,val classroom: ClassroomWithItems)
