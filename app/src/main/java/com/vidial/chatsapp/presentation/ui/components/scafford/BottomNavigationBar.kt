package com.vidial.chatsapp.presentation.ui.components.scafford

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MainNavigationBar(
    currentRouteHierarchyMap: List<String>,
    onButtonClick: (String) -> Unit = {},
) {
    val bottomNavItems = BottomNavigationItem.entries.toTypedArray()

    Column {
        Divider(color = MaterialTheme.colorScheme.surface, thickness = 0.65.dp)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = currentRouteHierarchyMap.contains(item.route),
                    onClick = {
                        onButtonClick(item.route)
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(id = item.navigationIconRes),
                            contentDescription = "${item.name} nav bar icon",
                            tint = if (currentRouteHierarchyMap.contains(item.route)) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                )
            }
        }
    }
}

