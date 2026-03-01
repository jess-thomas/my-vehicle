package com.example.myvehicles

data class ModelsResponse(
    val Count: Int,
    val Message: String,
    val SearchCriteria: String,
    val Results: List<ModelResult>
)

data class ModelResult(
    val Make_ID: Int,
    val Make_Name: String,
    val Model_ID: Int,
    val Model_Name: String
)
