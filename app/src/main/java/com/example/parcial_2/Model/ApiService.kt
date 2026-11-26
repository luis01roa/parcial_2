package com.example.parcial_2.Model

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class TripDto(
    val id: Int,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val expenseCount: Int = 0,
    val totalExpenses: Double = 0.0
)

data class CreateTripRequest(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class ExpenseDto(
    val id: Int,
    val tripId: Int,
    val description: String,
    val amount: Double,
    val expenseDate: String,
    val categoryId: Int,
    val categoryName: String
)

data class CreateExpenseRequest(
    val tripId: Int,
    val description: String,
    val amount: Double,
    val categoryId: Int,
    val expenseDate: String
)

data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String? = null
)

interface ApiService {
    @GET("api/trips")
    suspend fun getTrips(): List<TripDto>

    @POST("api/trips")
    suspend fun createTrip(@Body request: CreateTripRequest): TripDto

    @GET("api/expenses")
    suspend fun getExpenses(@Query("tripId") tripId: Int): List<ExpenseDto>

    @POST("api/expenses")
    suspend fun createExpense(@Body request: CreateExpenseRequest): ExpenseDto

    @GET("api/categories")
    suspend fun getCategories(): List<CategoryDto>
}

