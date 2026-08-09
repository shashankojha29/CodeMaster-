package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag

sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Learn : NavItem("learn", "Learn", Icons.AutoMirrored.Filled.MenuBook)
    object Practice : NavItem("practice", "Practice", Icons.Default.Code)
    object CodeLab : NavItem("code_lab", "Code Lab", Icons.Default.Terminal)
    object Typing : NavItem("typing", "Typing", Icons.Default.Keyboard)
    object Roadmaps : NavItem("roadmaps", "Roadmaps", Icons.Default.Map)
    object Profile : NavItem("profile", "Profile", Icons.Default.Person)
}

val bottomNavItems = listOf(
    NavItem.Home,
    NavItem.Learn,
    NavItem.Practice,
    NavItem.CodeLab,
    NavItem.Typing,
    NavItem.Roadmaps,
    NavItem.Profile
)

@Composable
fun CodeMasterBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                modifier = Modifier.testTag("nav_${item.route}"),
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
