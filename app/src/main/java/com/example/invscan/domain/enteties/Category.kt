package com.example.invscan.domain.enteties

data class Category(
    val id:Int,
    val name:String
)

data class CategoryWithCount(
    val id:Int,
    val name:String,
    var count:Int,
)

data class GetAllCategoriesResponse(val status:Boolean,val message:String,val categories:List<Category>?)

data class GetCategoryResponse(val status:Boolean,val message:String,val category: Category)
