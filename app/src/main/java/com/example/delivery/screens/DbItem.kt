package com.example.delivery.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.delivery.R
import com.example.delivery.viewmodel.DbViewModel
import com.example.domain.model.ProductFromDb

@Composable
fun DbItem(
    productFromDb: ProductFromDb,
    dbViewModel: DbViewModel,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        AsyncImage(
            model = productFromDb.image,
            error = painterResource(id = R.drawable.baseline_terrain_24),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(96.dp),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(135.dp)
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = productFromDb.name,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minus_gray),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            dbViewModel.minusCount(productFromDb)
                        },
                    contentDescription = null
                )
                Text(
                    text = productFromDb.count.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.plus_gray),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            dbViewModel.plusCount(productFromDb)
                        },
                    contentDescription = null
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "460 p",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "500 p",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                textDecoration = TextDecoration.LineThrough,
            )
        }
        HorizontalDivider(
            modifier = Modifier.align(Alignment.BottomCenter),
            thickness = 1.dp
        )
    }
}