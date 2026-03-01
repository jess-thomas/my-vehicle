package com.example.myvehicles

import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApiService {
    @GET("api/vehicles/getmodelsformake/{make}?format=json")
    suspend fun getModelsForMake(@Path("make") make: String): ModelsResponse
}
