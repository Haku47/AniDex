package com.anidex.app.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.R
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.navigation.Screen
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authRepository: AuthRepository,
    onNavigate: (String) -> Unit
) {
    val scale = remember { Animatable(0.4f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(600)
        )
        delay(1400)

        val destination = when {
            !authRepository.isOnboardingCompleted -> Screen.Onboarding.route
            authRepository.currentUser.value == null -> Screen.Login.route
            else -> Screen.Discover.route
        }
        onNavigate(destination)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_anidex),
                contentDescription = "AniDex Mascot Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(190.dp)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = BrandOrange, fontWeight = FontWeight.Black)) {
                        append("Ani")
                    }
                    withStyle(style = SpanStyle(color = BrandBlue, fontWeight = FontWeight.Black)) {
                        append("Dex")
                    }
                },
                fontSize = 36.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.splash_tagline),
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
