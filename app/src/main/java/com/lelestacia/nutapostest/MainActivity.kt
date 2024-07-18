package com.lelestacia.nutapostest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lelestacia.nutapostest.domain.FinanceData
import com.lelestacia.nutapostest.domain.FinanceIn
import com.lelestacia.nutapostest.domain.FinanceOut
import com.lelestacia.nutapostest.domain.FinanceStatement
import com.lelestacia.nutapostest.domain.calculateNetSalesAndTax
import com.lelestacia.nutapostest.domain.calculateShareRevenue
import com.lelestacia.nutapostest.domain.calculateTotalDiscount
import com.lelestacia.nutapostest.domain.createReport
import com.lelestacia.nutapostest.ui.UiSelection
import com.lelestacia.nutapostest.ui.route.FunctionFour
import com.lelestacia.nutapostest.ui.route.FunctionOne
import com.lelestacia.nutapostest.ui.route.FunctionThree
import com.lelestacia.nutapostest.ui.route.FunctionTwo
import com.lelestacia.nutapostest.ui.route.Home
import com.lelestacia.nutapostest.ui.theme.NutaposTestTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutaposTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()

                    NavHost(
                        navController = navHostController,
                        startDestination = Home
                    ) {

                        composable<Home> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Button(onClick = { navHostController.navigate(FunctionOne) }) {
                                    Text(text = "Hitung total harga sebelum pajak")
                                }

                                Button(onClick = { navHostController.navigate(FunctionTwo) }) {
                                    Text(text = "Hitung total diskon")
                                }

                                Button(onClick = { navHostController.navigate(FunctionThree) }) {
                                    Text(text = "Hitung Share Revenue")
                                }

                                Button(onClick = { navHostController.navigate(FunctionFour) }) {
                                    Text(text = "Buat Report dari data")
                                }
                            }
                        }

                        composable<FunctionOne> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                var totalPrice by remember {
                                    mutableStateOf("")
                                }

                                var taxPercentage by remember {
                                    mutableStateOf("")
                                }

                                var netSales by remember {
                                    mutableStateOf("0")
                                }

                                var taxValue by remember {
                                    mutableStateOf("0")
                                }

                                TextField(
                                    label = {
                                        Text(text = "Total Harga")
                                    },
                                    value = totalPrice,
                                    onValueChange = {
                                        totalPrice = it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                TextField(
                                    label = {
                                        Text(text = "Total Pajak dalam Persen")
                                    },
                                    value = taxPercentage,
                                    onValueChange = {
                                        taxPercentage = it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                Button(
                                    onClick = {
                                        val result = calculateNetSalesAndTax(
                                            totalPrice.toDoubleOrNull() ?: 0.toDouble(),
                                            taxPercentage.toDoubleOrNull() ?: 0.toDouble()
                                        )

                                        netSales = result.netSales
                                        taxValue = result.taxValue
                                    }
                                ) {
                                    Text(text = "Hitung")
                                }

                                Text(text = "Net Sales: Rp $netSales")
                                Text(text = "Total Tax: Rp $taxValue")
                            }
                        }

                        composable<FunctionTwo> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val discountList = remember {
                                    mutableStateListOf<String>()
                                }

                                var priceBeforeDiscount by remember {
                                    mutableStateOf("")
                                }

                                var priceAfterDiscount by remember {
                                    mutableStateOf("0")
                                }

                                var totalDiscount by remember {
                                    mutableStateOf("0")
                                }

                                TextField(
                                    label = {
                                        Text(text = "Harga Sebelum Diskon")
                                    },
                                    value = priceBeforeDiscount,
                                    onValueChange = {
                                        priceBeforeDiscount = it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 48.dp)
                                )

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 24.dp, max = 96.dp)
                                        .padding(horizontal = 48.dp)
                                ) {
                                    items(count = discountList.size) {
                                        val data = discountList[it]
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Diskon $data%")
                                            IconButton(
                                                onClick = {
                                                    discountList.removeAt(it)
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }

                                var newDiscount by remember {
                                    mutableStateOf("")
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextField(
                                        label = {
                                            Text(text = "Nominal Diskon")
                                        },
                                        value = newDiscount,
                                        onValueChange = { newDiscount = it },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        )
                                    )

                                    IconButton(
                                        onClick = {
                                            if (newDiscount.isBlank()) {
                                                discountList.add("0")
                                            } else {
                                                discountList.add(newDiscount)
                                                newDiscount = ""
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddCircle,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val discountIntArray = discountList.map { value ->
                                            value.toIntOrNull() ?: 0
                                        }.toIntArray()

                                        val result = calculateTotalDiscount(
                                            priceBeforeDiscount = priceBeforeDiscount.toDoubleOrNull()
                                                ?: 0.toDouble(),
                                            discounts = discountIntArray
                                        )

                                        priceAfterDiscount = result.priceAfterDiscount
                                        totalDiscount = result.totalDiscount
                                    }
                                ) {
                                    Text(text = "Hitung Total diskon dan harga setelah diskon")
                                }

                                Text(text = "Total diskon: Rp $totalDiscount")
                                Text(text = "Harga setelah diskon: Rp $priceAfterDiscount")
                            }
                        }

                        composable<FunctionThree> {
                            var priceBeforeMarkup by remember {
                                mutableStateOf("")
                            }

                            var markupPercentage by remember {
                                mutableStateOf("")
                            }

                            var sharedPercentage by remember {
                                mutableStateOf("")
                            }

                            var netForRestaurant by remember {
                                mutableStateOf("")
                            }

                            var shareForDriver by remember {
                                mutableStateOf("")
                            }

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                TextField(
                                    label = {
                                        Text(text = "Harga Sebelum Markup")
                                    },
                                    value = priceBeforeMarkup,
                                    onValueChange = { priceBeforeMarkup = it },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                TextField(
                                    label = {
                                        Text(text = "Persentase Markup")
                                    },
                                    value = markupPercentage,
                                    onValueChange = { markupPercentage = it },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                TextField(
                                    label = {
                                        Text(text = "Persentase Share")
                                    },
                                    value = sharedPercentage,
                                    onValueChange = { sharedPercentage = it },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                Button(
                                    onClick = {
                                        val result = calculateShareRevenue(
                                            priceBeforeMarkup.toDoubleOrNull() ?: 0.toDouble(),
                                            markupPercentage.toDoubleOrNull() ?: 0.toDouble(),
                                            sharedPercentage.toDoubleOrNull()
                                                ?: 0.toDouble()
                                        )

                                        netForRestaurant = result.netForRestaurant
                                        shareForDriver = result.shareForDriver
                                    }
                                ) {
                                    Text(text = "Hitung Net Restaurant dan Share Driver")
                                }

                                Text(text = "Net untuk restaurant: RP $netForRestaurant")
                                Text(text = "Share untuk driver: RP $shareForDriver")
                            }
                        }

                        composable<FunctionFour> {
                            val financeData = remember {
                                mutableStateListOf<FinanceData>()
                            }

                            val financeStatement = remember {
                                mutableStateListOf<FinanceStatement>()
                            }

                            var amount by remember {
                                mutableStateOf("")
                            }

                            var date by remember {
                                mutableStateOf("")
                            }

                            var balance by remember {
                                mutableStateOf("0")
                            }

                            var isShowingReport by remember {
                                mutableStateOf(false)
                            }

                            var isOpened by remember {
                                mutableStateOf(false)
                            }

                            var financeType = remember {
                                mutableStateOf(UiSelection.FINANCE_IN)
                            }

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxHeight(0.35F)
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        contentPadding = PaddingValues(4.dp)
                                    ) {
                                        when (isShowingReport) {
                                            true -> {
                                                item {
                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 8.dp)
                                                    ) {
                                                        Text(
                                                            text = "Tanggal",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Uang Masuk",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Uang Keluar",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Saldo",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                    }
                                                }

                                                items(count = financeStatement.size) {
                                                    val data = financeStatement[it]
                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 8.dp)
                                                    ) {
                                                        Text(
                                                            text = data.date,
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Rp ${data.amount_in}",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Rp ${data.amount_out}",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                        Text(
                                                            text = "Rp ${data.balance}",
                                                            modifier = Modifier.fillMaxWidth(0.2f)
                                                        )
                                                    }
                                                }

                                                item {
                                                    Text(
                                                        text = "Saldo Akhir: Rp ${financeStatement.lastOrNull()?.balance ?: 0}",
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 8.dp)
                                                    )
                                                }
                                            }

                                            false -> {
                                                items(financeData.size) {
                                                    val data = financeData[it]
                                                    val text = when (data) {
                                                        is FinanceIn -> "Uang masuk sebesar Rp ${data.amount}"
                                                        is FinanceOut -> "Uang keluar sebesar Rp ${data.amount}"
                                                    }

                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {

                                                        Text(text = text)

                                                        IconButton(onClick = {
                                                            financeData.removeAt(
                                                                it
                                                            )
                                                        }) {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Tampilkan Laporan Akhir")
                                    Switch(
                                        checked = isShowingReport,
                                        onCheckedChange = { isShowingReport = it }
                                    )
                                }

                                TextField(
                                    label = {
                                        Text(text = "Tanggal dengan format yyyy-mm-dd")
                                    },
                                    placeholder = {
                                        Text(text = "2024-04-22")
                                    },
                                    value = date,
                                    onValueChange = { date = it }
                                )

                                TextField(
                                    label = {
                                        Text(
                                            text = "Jumlah Uang"
                                        )
                                    },
                                    value = amount,
                                    onValueChange = { amount = it },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                TextField(
                                    label = {
                                        Text(text = "Saldo Awal")
                                    },
                                    value = balance,
                                    onValueChange = { balance = it },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )



                                ExposedDropdownMenuBox(
                                    expanded = isOpened,
                                    onExpandedChange = {
                                        isOpened = it
                                    }
                                ) {
                                    TextField(
                                        label = {
                                            Text(text = "Jenis")
                                        },
                                        value = financeType.value.displayName,
                                        onValueChange = {

                                        },
                                        readOnly = true,
                                        trailingIcon = {
                                            IconButton(onClick = { isOpened = true }) {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOpened)
                                            }
                                        },
                                        modifier = Modifier.menuAnchor()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = isOpened,
                                        onDismissRequest = { isOpened = false }
                                    ) {
                                        UiSelection.entries.forEach {
                                            DropdownMenuItem(
                                                text = { Text(text = it.displayName) },
                                                onClick = { financeType.value = it }
                                            )
                                        }
                                    }
                                }


                                Button(
                                    onClick = {
                                        when (financeType.value) {
                                            UiSelection.FINANCE_IN -> financeData.add(
                                                FinanceIn(
                                                    id = financeData.size,
                                                    date = date,
                                                    amount = amount.toIntOrNull() ?: 0
                                                )
                                            )

                                            UiSelection.FINANCE_OUT -> {
                                                financeData.add(
                                                    FinanceOut(
                                                        id = financeData.size,
                                                        date = date,
                                                        amount = amount.toIntOrNull() ?: 0
                                                    )
                                                )
                                            }
                                        }

                                        date = ""
                                        amount = "0"
                                    }
                                ) {
                                    Text(text = "Tambah Data")
                                }

                                Button(
                                    onClick = {
                                        financeStatement.clear()
                                        financeStatement.addAll(
                                            createReport(
                                                financeData,
                                                balance.toIntOrNull() ?: 0
                                            )
                                        )
                                        isShowingReport = true
                                    }
                                ) {
                                    Text(text = "Buat laporan")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutaposTestTheme {
        Greeting("Android")
    }
}