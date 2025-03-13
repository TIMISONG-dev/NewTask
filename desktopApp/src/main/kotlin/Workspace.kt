import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
//import app.cash.sqldelight.db.SqlDriver
//import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.awt.Toolkit

fun getScreenHeight(): Int {
    val screenHeightPx = Toolkit.getDefaultToolkit().screenSize.height
    val dpi = Toolkit.getDefaultToolkit().screenResolution
    return screenHeightPx * 160 / dpi - 1200
}

fun getScreenWidth(): Int {
    val screenWidthPx = Toolkit.getDefaultToolkit().screenSize.width
    val dpi = Toolkit.getDefaultToolkit().screenResolution
    return screenWidthPx * 160 / dpi
}

//fun createDatabaseDriver(): SqlDriver {
//    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")
//    WarehouseDB.Schema.create(driver)
//    return driver
//}

data class CourierBag (
    val id: Int,
    val name: String,
    val quantity: Int,
    val size: String,
    val weight: Double
)

@Composable
fun BagItem(
    bag: CourierBag,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(bag.name)
                Text(
                    text = "Размер: ${bag.size}, Вес: ${bag.weight} кг",
                    style = MaterialTheme.typography.caption
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { onQuantityChange(bag.quantity - 1) }) {
                    Text("-")
                }

                Text(
                    text = bag.quantity.toString(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Button(onClick = { onQuantityChange(bag.quantity + 1) }) {
                    Text("+")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onRemove,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Удалить")
                }
            }
        }
    }
}

@Composable
fun BagListScreen(bagManager: CourierBagViewModel) {
    LazyColumn (
        Modifier
            .width(500.dp)
            .height(500.dp)
    ){
        items(bagManager.bags) { bag ->
            BagItem(
                bag = bag,
                onRemove = { bagManager.removeBag(bag.id) },
                onQuantityChange = { newQuantity ->
                    bagManager.updateQuantity(bag.id, newQuantity)
                }
            )
        }
    }
}

@Composable
@Preview
fun Workspace(onNavigateTo: () -> Unit) {

    MaterialTheme {

        val title = mutableListOf(
//            "Payments",
//            "Donate",
//            "Stars",
//            "Steps",
//            "Map",
//            "Last order",
//            "Work",
//            "Slots",
//            "Support",
//            "Profile",
            "Платежи",
            if (!MainData.support) "Чаевые" else "",
            "Оценки",
            if (!MainData.support) "Шаги" else "",
            "Склад",
            if (!MainData.support) "Последний заказ" else "Последний чат",
            "Работа",
            "Слоты",
            "Поддержка",
            "Профиль"
        ).filter { it.isNotEmpty() }

        val cells = remember { mutableIntStateOf(2) }
        val openCell = remember { mutableIntStateOf(-1) }

        Column {
            Row (
                Modifier
                    .padding(16.dp)
                    .weight(1F),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateTo,
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    "Меню",
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(6F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(cells.intValue),
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    items(title.size) { index ->
                        GridItem(
                            title = title[index],
                            index = index,
                            cells = cells,
                            openCell = openCell
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridItem(title: String, index: Int, cells: MutableIntState, openCell: MutableIntState) {

    val expanded = remember { mutableStateOf(false) }

    val defaultOnColor = MaterialTheme.colors.surface

//  val currency = "$"
    val currency = "Руб."

    val pay = listOf(
        1000,
        2000,
        102,
        130,
        1000,
        2000,
        102,
        130,
        1000,
        2000,
        102,
        130
    )

    val donate = listOf(
        500,
        102,
        370
    )

    val stars = listOf(
        3,
        10,
        9
    )

    val steps = listOf(
        20000,
        10231,
        11984
    )

    val orders = listOf(
        "Москва, Проспект Буденного, 22к1",
        "Щербинка, Главная улица, 1к1"
    )

    val slots = listOf(
        "10:00 - 18:00",
        "13:32 - 19:58"
    )

    val icons = listOf(
        Component.loadDrawable("ic_payments.png"),
        Component.loadDrawable("ic_fastfood_near.png"),
        Component.loadDrawable("ic_star_half.png"),
        Component.loadDrawable("ic_steps.png"),
        Component.loadDrawable("ic_warehouse.png"),
        Component.loadDrawable("ic_last_order.png"),
        Component.loadDrawable("ic_start_work.png"),
        Component.loadDrawable("ic_slots.png"),
        Component.loadDrawable("ic_support_agent.png"),
        Component.loadDrawable("ic_account.png")
    )

    val filteredIcons = if (MainData.support) {
        icons.filterIndexed { index, _ ->
            index != 1 && index != 3
        }
    } else {
        icons
    }

    val indexToListMap = mapOf(
        0 to pay,
        1 to donate,
        2 to stars,
        3 to steps,
        5 to orders,
        7 to slots
    )

    val adjustedMap = indexToListMap.mapKeys { (key, _) ->
        if (MainData.support) {
            when (key) {
                2 -> 1
                5 -> 3
                7 -> 5
                else -> key
            }
        } else {
            key
        }
    }

    val exHeight by animateDpAsState(
        targetValue = if (expanded.value) getScreenHeight().dp else 200.dp,
        animationSpec = tween(300),
        label = "expanded height"
    )

    val exWidth by animateDpAsState(
        targetValue = if (expanded.value) getScreenWidth().dp else 200.dp,
        animationSpec = tween(300),
        label = "expanded width"
    )


    // Показываем либо один выбранный tile Grid или все tiles
    if (openCell.intValue == -1 || openCell.intValue == index) {
        val list = adjustedMap[index] ?: emptyList<Any>()
        Column(
            Modifier
                .padding(8.dp)
                .height(exHeight)
                .width(exWidth)
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    expanded.value = !expanded.value
                    if (expanded.value) {
                        cells.intValue = 1
                        openCell.intValue = index
                    } else {
                        cells.intValue = 2
                        openCell.intValue = -1
                    }
                }
                .animateContentSize()
                .background(Component.clickableColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$title $index",
                fontWeight = FontWeight.Bold,
                fontSize = if (expanded.value) 20.sp else 16.sp,
                color = defaultOnColor,
                modifier = Modifier
                    .padding(8.dp)
            )
            if (expanded.value) {
                Component.nextStep.value = false
                Column (
                    Modifier
                        .then(if (index != 8) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    // Support
                    if (if (!MainData.support) index == 8 else index == 6) {
                        /*
                        val viewModel: Chat.ChatViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return Chat.ChatViewModel(context) as T
                            }
                        })

                        Chat.ChatBlank(messages = viewModel.messages) { message ->
                            viewModel.sendMessage(message)
                        }
                        */
                        Text (
                            "Support"
                        )
                    }
                    if (if (!MainData.support) index == 9 else index == 7) {
                        Column (
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                            ,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text (
                                "YOUR INFO:",
                                fontWeight = FontWeight.Bold
                            )
                            Text (
                                MainData.name,
                                fontSize = 18.sp,
                            )
                            Text (
                                MainData.email,
                                fontSize = 18.sp,
                            )
                        }
                    }
                    if (if (!MainData.support) index == 4 else index == 2) {
                        if (!MainData.support) {
                            Text(
                                "Map"
                            )
                        } else {
                            val bagManager = remember { CourierBagViewModel() }
                            BagListScreen(bagManager)
                        }
                        // YandexMap.Mapa()
                    }
                    if (if (!MainData.support) index == 6 else index == 4) {
                        Button(onClick = {
                            /*
                            GlobalScope.launch(Dispatchers.Main) {
                                if (Working.compareLocations(workerLocation.value, orderLocation.value, App.GEO_API_KEY)) {
                                    expanded.value = !expanded.value
                                    openCell.intValue = 4
                                    cells.intValue = 1
                                    YandexMap.mapMode.value = "route"
                                    Workspace.nextStep.value = true
                                }
                            }
                            */
                        }) {
                            Text(
                                "Start work"
                            )
                        }
                    } else {
                        Spacer(Modifier.padding(8.dp))
                        list.forEach { item ->
                            Row(
                                Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .width(300.dp)
                                    .background(MaterialTheme.colors.surface)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    filteredIcons.getOrNull(index) ?: Component.loadDrawable("ic_visibility_off.png"),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSurface,
                                    modifier = Modifier.size(32.dp)
                                )
                                // Some lists
                                if (index < 4 || index == 5 || index == 7) {
                                    Text(
                                        text = "$item",
                                        Modifier
                                            .padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (index == 5) 18.sp else 24.sp,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                    if (!MainData.support) {
                                        if (index == 0 || index == 1) {
                                            Text(
                                                text = currency,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        }
                                    } else {
                                        if (index == 0) {
                                            Text(
                                                text = currency,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.padding(8.dp))
                        }
                    }
                }
            } else {
                Row(
                    Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        filteredIcons.getOrNull(index) ?: Component.loadDrawable("ic_visibility_off.png"),
                        contentDescription = null,
                        tint = defaultOnColor,
                        modifier = Modifier.size(64.dp)
                    )
                    if (!MainData.support) {
                        if (index in 0..3) {
                            Text(
                                text = when (index) {
                                    0 -> pay[pay.size - 1].toString()
                                    1 -> donate[donate.size - 1].toString()
                                    2 -> stars[stars.size - 1].toString()
                                    3 -> steps[steps.size - 1].toString()
                                    else -> "Error lol"
                                },
                                Modifier.padding(5.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = defaultOnColor
                            )
                            if (index == 0 || index == 1) {
                                Text(
                                    text = currency,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = defaultOnColor
                                )
                            }
                        }
                    } else {
                        if (index == 0 || index == 1) {
                            Text(
                                text = when (index) {
                                    0 -> pay[pay.size - 1].toString()
                                    1 -> stars[stars.size - 1].toString()
                                    else -> "Error lol"
                                },
                                Modifier.padding(5.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = defaultOnColor
                            )
                        }
                    }
                }
                if (Component.nextStep.value) {
                    Text (
                        "Tap to open tab!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = defaultOnColor
                    )
                }
            }
        }
    }
}

fun main() = application {
    Window(title="Workspace", onCloseRequest = ::exitApplication) {
        val cs = remember { mutableStateOf("main") }
        when (cs.value) {
            "main" -> Main(onNavigateTo = { cs.value = "workspace" })
            "workspace" -> Workspace(onNavigateTo = { cs.value = "main" })
        }
    }
}