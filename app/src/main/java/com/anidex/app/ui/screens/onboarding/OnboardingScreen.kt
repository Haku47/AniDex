package com.anidex.app.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.R
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.navigation.Screen
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class OnboardingStep(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val icon: ImageVector,
    val accentColor: Color
)

val onboardingSteps = listOf(
    OnboardingStep(
        titleRes = R.string.onboarding_title_1,
        subtitleRes = R.string.onboarding_sub_1,
        icon = Icons.Default.AutoAwesome,
        accentColor = BrandOrange
    ),
    OnboardingStep(
        titleRes = R.string.onboarding_title_2,
        subtitleRes = R.string.onboarding_sub_2,
        icon = Icons.Default.HourglassTop,
        accentColor = BrandBlue
    ),
    OnboardingStep(
        titleRes = R.string.onboarding_title_3,
        subtitleRes = R.string.onboarding_sub_3,
        icon = Icons.Default.CalendarMonth,
        accentColor = BrandCyan
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    authRepository: AuthRepository,
    onFinish: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingSteps.size })
    val scope = rememberCoroutineScope()

    fun completeOnboarding() {
        authRepository.setOnboardingCompleted(true)
        onFinish(Screen.Login.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground)
            .padding(24.dp)
    ) {
        // Top Skip Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage < onboardingSteps.size - 1,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextButton(onClick = { completeOnboarding() }) {
                    Text(text = stringResource(R.string.onboarding_skip), color = TextSecondary, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Onboarding Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val step = onboardingSteps[page]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Graphic Container
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(SurfaceDark),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(step.accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = step.icon,
                            contentDescription = null,
                            tint = step.accentColor,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = stringResource(step.titleRes),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(step.subtitleRes),
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pager Indicator & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(onboardingSteps.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(if (isSelected) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) BrandOrange else SurfaceDark)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Next or Get Started Button
        Button(
            onClick = {
                if (pagerState.currentPage < onboardingSteps.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    completeOnboarding()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
        ) {
            Text(
                text = if (pagerState.currentPage == onboardingSteps.size - 1) {
                    stringResource(R.string.onboarding_start)
                } else {
                    stringResource(R.string.onboarding_continue)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

