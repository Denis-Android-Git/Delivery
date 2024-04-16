package com.example.delivery.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.delivery.R
import com.example.domain.model.Product

@Composable
fun DetailScreen(
    modifier: Modifier,
    item: Product,
    onButtonClick: () -> Unit,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.back), contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.TopStart)
                .clickable { onBackClick() }
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 70.dp, top = 50.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = item.image,
                    error = painterResource(id = R.drawable.baseline_terrain_24),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
                if (item.price_old != null) {
                    Image(
                        painter = painterResource(id = R.drawable.discount),
                        contentDescription = null,
                        alignment = Alignment.TopStart,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = item.name,
                    fontSize = 34.sp,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                )
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = item.description,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    color = Color.Gray
                )
                HorizontalDivider(thickness = 2.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.weight),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.measure.toString(), fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            modifier = Modifier.padding(end = 3.dp)
                        )
                        Text(
                            text = item.measure_unit, fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.enerdy),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.kkal, item.energy_per_100_grams),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            modifier = Modifier.padding(end = 3.dp)
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.protein),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.proteins_per_100_grams.toString(), fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            modifier = Modifier.padding(end = 3.dp)
                        )
                        Text(
                            text = item.measure_unit, fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.fat),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.fats_per_100_grams.toString(), fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            modifier = Modifier.padding(end = 3.dp)
                        )
                        Text(
                            text = item.measure_unit, fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.sugar),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.carbohydrates_per_100_grams.toString(), fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            modifier = Modifier.padding(end = 3.dp)
                        )
                        Text(
                            text = item.measure_unit, fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp)
            }
        }
        Button(
            onClick = onButtonClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(343.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF15412)
            )
        ) {
            Row {
                Text(
                    text = stringResource(R.string.buy, item.price_current / 100),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto))
                )
                if (item.price_old != null) {
                    Text(
                        text = stringResource(
                            R.string.twoprice,
                            item.price_old!! / 100
                        ),
                        modifier = Modifier.padding(start = 10.dp),
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    // DetailScreen(Modifier)
}