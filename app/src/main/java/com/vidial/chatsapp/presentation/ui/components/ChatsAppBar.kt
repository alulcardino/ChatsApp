package com.vidial.chatsapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidial.chatsapp.presentation.ui.theme.LightPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsAppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: ImageVector? = null,
    actionIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    dividerColor: Color = LightPurple
) {
    Column {
        CenterAlignedTopAppBar(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
            ,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                if (navigationIcon != null) {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = "Navigation",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            actions = {
                if (actionIcon != null) {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = "Action",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = dividerColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChatsAppBarPreview() {
    MaterialTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
            ChatsAppBar(
                title = "Chat App",
                navigationIcon = Icons.Filled.ArrowBack,
                actionIcon = Icons.Filled.Edit,
                onNavigationClick = { /* TODO: Handle navigation click */ },
                onActionClick = { /* TODO: Handle action click */ }
            )
        }
    }
}

