package com.example.androiddevchallenge.ui.page

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.TimeViewModel

/**
 * @Author:       Chen
 * @Date:         2021/3/5 9:29
 * @Description:
 */

const val Second = 1000L
const val Minute = 60 * Second
const val Hour = 60 * Minute

sealed class InputType(val value: String) {
    //    Hour, Minute, Second, Auto
    object Hour : InputType("HH")
    object Minute : InputType("MM")
    object Second : InputType("SS")
    object Auto : InputType("A")
}

@Composable
fun CountDownTimerPage(viewModel: TimeViewModel = viewModel()) {
    val time by viewModel.time.collectAsState()
    val showSetTime by viewModel.showFloatButton.collectAsState()
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
    Crossfade(
        targetState = showSetTime,
        animationSpec = tween(1000)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (it) {
                TimePicker(viewModel = viewModel)
            } else {
                CountDown(viewModel = viewModel)
            }
        }
    }
//    }
}

@Composable
fun CountDown(viewModel: TimeViewModel) {
    var start by remember { mutableStateOf(false) }
    val currentTime by viewModel.currentTime.collectAsState()
    val animDuration by viewModel.time.collectAsState()
    var animTemp by mutableStateOf(0f)
    val scaleAnim by animateFloatAsState(
        targetValue = if (start) 1f else animTemp,
        animationSpec = tween(
            animDuration.toInt(),
            easing = LinearEasing
        )
    )

    val arcColor = MaterialTheme.colors.primary
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(320.dp).fillMaxHeight(0.8f)
                .drawWithContent {
                    animTemp = scaleAnim
                    drawCircle(
                        color = Color.LightGray,
                        style = Stroke(width = 8f)
                    )
                    drawArc(
                        color = arcColor,
                        startAngle = -90f,
                        sweepAngle = (360f * scaleAnim),
                        useCenter = false,
                        topLeft = Offset(0f, (size.height - size.width) / 2),
                        size = Size(size.width, size.width),
                        style = Stroke(width = 10f)
                    )
                    drawContent()
                }, contentAlignment = Alignment.Center
        ) {

            Text(text = timeFormat.format(currentTime), style = MaterialTheme.typography.h3)

        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                viewModel.resetTime()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = "")
            }
            FloatingActionButton(onClick = {
                start = if (!start) {
                    viewModel.startCountDownTime()
                    true
                } else {
                    viewModel.pauseCountDownTime()
                    false
                }
            }) {
                Icon(
                    painter = painterResource(id = if (!start) R.drawable.ic_play else R.drawable.ic_pause),
                    contentDescription = ""
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
//
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
val timeFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    SimpleDateFormat("HH : mm : ss")
} else {
    java.text.SimpleDateFormat("HH : mm : ss")
}

var inputType by mutableStateOf<InputType>(InputType.Auto)

@Composable
fun TimePicker(viewModel: TimeViewModel) {
    val hour by viewModel.hour.observeAsState()
    val minute by viewModel.minute.observeAsState()
    val second by viewModel.second.observeAsState()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textStyle = MaterialTheme.typography.h1.copy(fontSize = 72.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            TimeSelectItem(num = hour ?: 0, type = InputType.Hour, textStyle = textStyle)
            Text(
                text = ":",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
            TimeSelectItem(num = minute ?: 0, type = InputType.Minute, textStyle = textStyle)
            Text(
                text = ":",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
            TimeSelectItem(num = second ?: 0, type = InputType.Second, textStyle = textStyle)
        }
        Spacer(modifier = Modifier.height(16.dp))

        KeyBoard(
            modifier = Modifier
                .size(95.dp)
                .clip(CircleShape),
            textStyle = MaterialTheme.typography.h2.copy(
                fontSize = 56.sp
            )
        ) { number ->
            if (number >= 0) viewModel.addTime(number, inputType)
        }
    }
}

@Composable
fun TimeSelectItem(num: Int, type: InputType, textStyle: TextStyle) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (num / 10 == 0) "0$num" else num.toString(),
            style = if (inputType == type || inputType == InputType.Auto) {
                textStyle.copy(color = MaterialTheme.colors.primary)
            } else textStyle,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .clickable {
                    inputType = if (inputType == type) {
                        InputType.Auto
                    } else {
                        type
                    }
                }
        )
        Text(
            text = type.value,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun KeyBoard(
    modifier: Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h2,
    onClick: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            NumberButton(
                number = "1",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "2",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "3",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
        }
        Row {
            NumberButton(
                number = "4",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "5",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "6",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )

        }
        Row {
            NumberButton(
                number = "7",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "8",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )
            NumberButton(
                number = "9",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )

        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier)
            NumberButton(
                number = "0",
                onClick = onClick,
                modifier = modifier,
                textStyle = textStyle,
            )

            IconButton(onClick = { onClick(-1) }, modifier = modifier) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_backspace),
                    contentDescription = "delete"
                )
            }
        }
    }
}

@Composable
fun NumberButton(
    number: String,
    modifier: Modifier,
    textStyle: TextStyle,
    onClick: (Int) -> Unit
) {

    Text(
        text = number,
        style = textStyle,
        modifier = modifier.clickable { onClick(number.toInt()) },
        textAlign = TextAlign.Center
    )
}