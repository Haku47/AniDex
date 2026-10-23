package com.anidex.app.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.R
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.components.UserAvatarBadge
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.CardBorder
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.SurfaceVariantDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

private data class AvatarPreset(
    val id: String,
    @StringRes val nameRes: Int
)

private val avatarPresets = listOf(
    AvatarPreset("kitsune", R.string.preset_kitsune),
    AvatarPreset("shinobi", R.string.preset_shinobi),
    AvatarPreset("otaku", R.string.preset_otaku),
    AvatarPreset("sorcerer", R.string.preset_sorcerer)
)

@Composable
fun EditProfileScreen(
    authRepository: AuthRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user by authRepository.currentUser.collectAsState()

    var name by remember(user) { mutableStateOf(user?.name ?: "Pengguna Tamu") }
    var bio by remember(user) { mutableStateOf(user?.bio ?: "Penjelajah & Pelacak Anime Baru") }
    var selectedAvatar by remember(user) { mutableStateOf(user?.avatarUrl ?: "kitsune") }

    var showCustomUrlDialog by remember { mutableStateOf(false) }
    var customUrlInput by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    // Native Photo Picker launcher for uploading avatar from device gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val avatarFile = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(avatarFile).use { output ->
                        input.copyTo(output)
                    }
                }
                selectedAvatar = avatarFile.absolutePath
                Toast.makeText(context, context.getString(R.string.msg_photo_selected), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.msg_photo_pick_failed, e.localizedMessage ?: ""), Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.edit_profile_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            TextButton(
                onClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, context.getString(R.string.msg_name_empty), Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    isSaving = true
                    scope.launch {
                        authRepository.updateProfile(
                            name = name.trim(),
                            bio = bio.trim(),
                            avatarUrl = selectedAvatar
                        )
                        isSaving = false
                        Toast.makeText(context, context.getString(R.string.msg_profile_saved), Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    }
                },
                enabled = !isSaving
            ) {
                Text(
                    text = if (isSaving) stringResource(R.string.btn_saving) else stringResource(R.string.btn_save),
                    color = BrandOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Profile Preview Card with Avatar Centered
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(116.dp),
                    contentAlignment = Alignment.Center
                ) {
                    UserAvatarBadge(
                        avatarId = selectedAvatar,
                        size = 104.dp,
                        ringWidth = 3.dp,
                        showRing = true
                    )

                    // Floating Camera badge to trigger gallery picker
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BrandOrange)
                            .border(2.dp, DeepBackground, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = stringResource(R.string.btn_change_photo),
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = name.ifBlank { stringResource(R.string.profile_guest_user) },
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimary
                )

                Text(
                    text = user?.email ?: "guest@anidex.app",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceVariantDark
                ) {
                    Text(
                        text = bio.ifBlank { stringResource(R.string.profile_enthusiast) },
                        fontSize = 11.sp,
                        color = BrandCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Presets & Gallery Upload
        Text(
            text = stringResource(R.string.section_profile_photo),
            color = BrandOrange,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Preset Avatars Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(avatarPresets) { preset ->
                val isSelected = selectedAvatar.equals(preset.id, ignoreCase = true)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedAvatar = preset.id }
                ) {
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        UserAvatarBadge(
                            avatarId = preset.id,
                            size = 64.dp,
                            ringWidth = if (isSelected) 2.5.dp else 1.dp,
                            showRing = true
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(BrandOrange)
                                    .border(1.5.dp, DeepBackground, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(preset.nameRes),
                        fontSize = 11.sp,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Button: Upload from Gallery
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceDark,
            border = BorderStroke(1.dp, BrandOrange.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BrandOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = BrandOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.btn_upload_gallery),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.subtitle_upload_gallery),
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom URL link button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    customUrlInput = if (selectedAvatar.startsWith("http")) selectedAvatar else ""
                    showCustomUrlDialog = true
                }
                .padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.avatar_use_custom_url_btn),
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Personal Information
        Text(
            text = stringResource(R.string.section_personal_info),
            color = BrandOrange,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Field 1: Username
        OutlinedTextField(
            value = name,
            onValueChange = { if (it.length <= 40) name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.label_username), fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "${name.length}/40", color = TextSecondary, fontSize = 11.sp)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = CardBorder,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = BrandOrange,
                unfocusedLabelColor = TextSecondary
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Field 2: Bio
        OutlinedTextField(
            value = bio,
            onValueChange = { if (it.length <= 160) bio = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.label_bio), fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = BrandBlue
                )
            },
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "${bio.length}/160", color = TextSecondary, fontSize = 11.sp)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = CardBorder,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = BrandBlue,
                unfocusedLabelColor = TextSecondary
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Registered Account Information
        Text(
            text = stringResource(R.string.section_account_info),
            color = BrandOrange,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AccountInfoRow(
                    icon = Icons.Default.Email,
                    label = stringResource(R.string.account_email),
                    value = user?.email ?: "guest@anidex.app",
                    tint = BrandOrange
                )

                HorizontalDivider(
                    color = CardBorder.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                AccountInfoRow(
                    icon = Icons.Default.VerifiedUser,
                    label = stringResource(R.string.account_status),
                    value = if (user?.email != null) stringResource(R.string.account_active) else stringResource(R.string.account_guest_offline),
                    tint = BrandCyan
                )

                HorizontalDivider(
                    color = CardBorder.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                AccountInfoRow(
                    icon = Icons.Default.CalendarMonth,
                    label = stringResource(R.string.account_role),
                    value = stringResource(R.string.account_role_val),
                    tint = BrandBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Custom URL Dialog
    if (showCustomUrlDialog) {
        AlertDialog(
            onDismissRequest = { showCustomUrlDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.dialog_custom_avatar_title),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.dialog_custom_avatar_desc),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customUrlInput,
                        onValueChange = { customUrlInput = it },
                        placeholder = { Text("https://contoh.com/avatar.jpg", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customUrlInput.isNotBlank()) {
                            selectedAvatar = customUrlInput.trim()
                            showCustomUrlDialog = false
                            Toast.makeText(context, context.getString(R.string.msg_avatar_url_selected), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                ) {
                    Text(stringResource(R.string.action_apply))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomUrlDialog = false }) {
                    Text(stringResource(R.string.action_cancel), color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun AccountInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
