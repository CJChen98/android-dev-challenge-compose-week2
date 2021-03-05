package com.example.androiddevchallenge.ui.page

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.TimeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R

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
    val showSetTime by mutableStateOf(time <= 0)
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
                SetTime(viewModel = viewModel)
            } else {
                CountDown(viewModel = viewModel)
            }
        }
    }
//    }
}

@Composable
fun CountDown(viewModel: TimeViewModel) {

    Box(modifier = Modifier
        .size(320.dp)
        .drawWithContent {
            drawCircle(
                color = Color.LightGray,
                style = Stroke(width = 8f)
            )
        }) {

    }
}

@Composable
fun SetTime(viewModel: TimeViewModel) {
    Box(modifier = Modifier.size(400.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TimePicker(viewModel)
        }
    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeSelectItem(num = hour ?: 0, type = InputType.Hour, textStyle = textStyle)
            Text(text = ":", style = MaterialTheme.typography.h2)
            TimeSelectItem(num = minute ?: 0, type = InputType.Minute, textStyle = textStyle)
            Text(text = ":", style = MaterialTheme.typography.h2)
            TimeSelectItem(num = second ?: 0, type = InputType.Second, textStyle = textStyle)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Log.d("H2 Color", "TimePicker: ${MaterialTheme.typography.h2.color}")
        Log.d("H1 Color", "TimePicker: ${MaterialTheme.typography.h1.color}")
        KeyBoard(
            modifier = Modifier
                .size(90.dp)
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