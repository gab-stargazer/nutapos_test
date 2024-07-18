package com.lelestacia.nutapostest.domain

import kotlin.math.roundToInt

fun calculateNetSalesAndTax(total: Double, taxPercentage: Double): NetSalesAndTaxValue {
    val totalTax: Double = 1 + (taxPercentage / 100)
    val netSales = (total / totalTax).roundToInt()

    return NetSalesAndTaxValue(
        netSales = netSales.toString(),
        taxValue = (netSales * (taxPercentage / 100)).roundToInt().toString()
    )
}

data class NetSalesAndTaxValue(
    val netSales: String,
    val taxValue: String
)

fun calculateTotalDiscount(
    priceBeforeDiscount: Double,
    discounts: IntArray
): TotalDiscountAndPriceAfterDiscount {
    var totalDiscount = 0.toDouble()
    var priceAfterDiscount = priceBeforeDiscount

    for (discount in discounts) {
        val discountInRupiah = priceAfterDiscount * (discount.toDouble() / 100)
        totalDiscount += discountInRupiah
        priceAfterDiscount -= discountInRupiah
    }


    return TotalDiscountAndPriceAfterDiscount(
        priceAfterDiscount.roundToInt().toString(),
        totalDiscount.roundToInt().toString()
    )
}

data class TotalDiscountAndPriceAfterDiscount(
    val priceAfterDiscount: String,
    val totalDiscount: String
)

fun calculateShareRevenue(
    priceBeforeMarkup: Double,
    markupPercentage: Double,
    sharePercentage: Double
): NetForRestaurantAndShareForDriver {

    val priceAfterMarkup =
        priceBeforeMarkup + (priceBeforeMarkup * (markupPercentage / 100))

    val netValueAfterShare =
        priceAfterMarkup - (priceAfterMarkup * (sharePercentage / 100))


    return NetForRestaurantAndShareForDriver(
        netValueAfterShare.roundToInt().toString(),
        (priceAfterMarkup - netValueAfterShare).roundToInt().toString()
    )
}

data class NetForRestaurantAndShareForDriver(
    val netForRestaurant: String,
    val shareForDriver: String
)

sealed interface FinanceData

data class FinanceIn(
    val id: Int,
    val date: String,
    val amount: Int
) : FinanceData

data class FinanceOut(
    val id: Int,
    val date: String,
    val amount: Int
) : FinanceData

data class FinanceStatement(
    val date: String,
    val amount_in: Int,
    val amount_out: Int,
    val balance: Int
)

fun createReport(
    inputData: List<FinanceData>,
    startingBalance: Int = 0
): List<FinanceStatement> {
    var currentBalance = startingBalance
    val temporaryData = mutableListOf<FinanceStatement>()
    for (data in inputData) {
        when (data) {
            is FinanceIn -> {
                temporaryData.add(
                    FinanceStatement(
                        date = data.date,
                        amount_in = data.amount,
                        balance = (currentBalance + data.amount),
                        amount_out = 0
                    )
                )

                currentBalance += data.amount
            }

            is FinanceOut -> {
                temporaryData.add(
                    FinanceStatement(
                        date = data.date,
                        amount_in = 0,
                        balance = (currentBalance - data.amount),
                        amount_out = data.amount
                    )
                )

                currentBalance -= data.amount
            }
        }
    }

    return temporaryData
}
