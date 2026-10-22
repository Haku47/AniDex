package com.anidex.app.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.ui.components.AvailableAvatarPresets
import com.anidex.app.ui.components.UserAvatarDisplay
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandRed
import com.anidex.app.ui.theme.CardBorder
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.SurfaceVariantDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentUser = state.user

    var name by remember(currentUser) { mutableStateOf(currentUser?.name.orEmpty()) }
    var bio by remember(currentUser) { mutableStateOf(currentUser?.bio.orEmpty()) }
    var selectedAvatarKey by remember(currentUser) {
        mutableStateOf(currentUser?.avatarUrl ?: "mascot_fox")
    }
    var customAvatarUrl by remember { mutableStateOf("") }
    var showCustomUrlField by remember { mutableStateOf(false) }

    LaunchedEffect(state.updateMessage) {
        state.updateMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(38.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            }

            TextButton(
                onClick = {
                    val finalAvatar = if (showCustomUrlField && customAvatarUrl.isNotBlank()) {
                        customAvatarUrl.trim()
                    } else {
                        selectedAvatarKey
                    }
                    viewModel.updateProfile(
                        name = name,
                        bio = bio,
                        avatarUrl = finalAvatar
                    )
                },
                enabled = name.isNotBlank() && !state.isUpdating
            ) {
                if (state.isUpdating) {
                    CircularProgressIndicator(
                        color = BrandOrange,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "Simpan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (name.isNotBlank()) BrandOrange else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Error Banner if present
        state.updateError?.let { err ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BrandRed.copy(alpha = 0.15f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = err,
                    color = BrandRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Live Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val previewAvatar = if (showCustomUrlField && customAvatarUrl.isNotBlank()) {
                    customAvatarUrl
                } else {
                    selectedAvatarKey
                }

                UserAvatarDisplay(
                    avatarKeyOrUrl = previewAvatar,
                    size = 96.dp,
                    borderWidth = 3.dp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = name.ifBlank { "Nama Pengguna" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceVariantDark
                ) {
                    Text(
                        text = bio.ifBlank { "Bio anime kamu..." },
                        fontSize = 11.sp,
                        color = BrandCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Avatar Selection Section
        Text(
            text = "Pilih Avatar Karakter",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = BrandOrange,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(AvailableAvatarPresets) { preset ->
                val isSelected = (!showCustomUrlField || customAvatarUrl.isBlank()) &&
                        selectedAvatarKey == preset.id

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedAvatarKey = preset.id
                            showCustomUrlField = false
                        }
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) preset.ringColor.copy(alpha = 0.2f) else SurfaceDark)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) preset.ringColor else CardBorder,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        UserAvatarDisplay(
                            avatarKeyOrUrl = preset.id,
                            size = 54.dp,
                            borderWidth = 1.5.dp
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(preset.ringColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = preset.name,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Toggle Custom Image URL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showCustomUrlField = !showCustomUrlField }
                .padding(vertical = 6.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = if (showCustomUrlField) BrandOrange else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (showCustomUrlField) "Gunakan Avatar Preset" else "Atau gunakan tautan URL gambar avatar kustom",
                fontSize = 12.sp,
                color = if (showCustomUrlField) BrandOrange else TextSecondary
            )
        }

        if (showCustomUrlField) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customAvatarUrl,
                onValueChange = { customAvatarUrl = it },
                label = { Text("URL Gambar Avatar (https://...)") },
                placeholder = { Text("https://example.com/foto.jpg") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Fields
        Text(
            text = "Informasi Diri",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = BrandOrange,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= 40) name = it
                viewModel.clearMessages()
            },
            label = { Text("Nama Lengkap / Username") },
            placeholder = { Text("Masukkan nama...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = BrandOrange)
            },
            supportingText = {
                Text(
                    text = "${name.length}/40",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            },
            isError = name.isBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Bio Field
        OutlinedTextField(
            value = bio,
            onValueChange = {
                if (it.length <= 160) bio = it
                viewModel.clearMessages()
            },
            label = { Text("Bio Anime / Status") },
            placeholder = { Text("Tuliskan anime favorit atau bio singkat...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = BrandBlue)
            },
            supportingText = {
                Text(
                    text = "${bio.length}/160",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            },
            minLines = 3,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Read-only Account Info Section
        Text(
            text = "Informasi Akun Terdaftar",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = BrandOrange,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Email Akun", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            text = currentUser?.email ?: "Belum login akun resmi",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BrandCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Bergabung Sejak", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            text = currentUser?.joinedDate ?: "Oktober 2026",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Bottom Actions
        Button(
            onClick = {
                val finalAvatar = if (showCustomUrlField && customAvatarUrl.isNotBlank()) {
                    customAvatarUrl.trim()
                } else {
                    selectedAvatarKey
                }
                viewModel.updateProfile(
                    name = name,
                    bio = bio,
                    avatarUrl = finalAvatar
                )
            },
            enabled = name.isNotBlank() && !state.isUpdating,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
        ) {
            if (state.isUpdating) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Simpan Perubahan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(CardBorder)
            )
        ) {
            Text(text = "Batal", fontSize = 14.sp, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}
