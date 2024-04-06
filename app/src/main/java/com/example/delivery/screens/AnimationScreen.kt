package com.example.delivery.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.delivery.R

@Composable
fun AnimationScreen(
    modifier: Modifier
) {
    val lottie by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation)
    )

    LottieAnimation(
        modifier = modifier.fillMaxSize(),
        composition = lottie, iterations = LottieConstants.IterateForever
    )
}

@Preview(showBackground = true)
@Composable
fun Anim() {
    AnimationScreen(modifier = Modifier)
}