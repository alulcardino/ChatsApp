package com.vidial.chatsapp.presentation.ui.components.scafford

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

@Composable
fun Root(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRouteHierarchyMap = navBackStackEntry?.destination?.hierarchy?.map { it.route.toString() }?.toList()
        ?: emptyList()

    Scaffold(
        bottomBar = {
            if (currentRouteHierarchyMap.contains(ScreenRoute.ChatListScreen.route) ||
                currentRouteHierarchyMap.contains(ScreenRoute.ProfileScreen.route)) {
                MainNavigationBar(
                    currentRouteHierarchyMap = currentRouteHierarchyMap,
                    onButtonClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Передаем paddingValues = PaddingValues(0.dp) в ChatScreen
            content(PaddingValues(0.dp))
        }
    }
}
