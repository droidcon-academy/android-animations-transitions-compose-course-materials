package com.droidcon.voices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Custom bottom app bar that supports navigation
 */
@Composable
fun MyBottomBar(
    modifier : Modifier = Modifier,
    navController: NavHostController,
    onNavigate: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    Surface(modifier = modifier) {
        BottomAppBar(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .padding(4.dp)
            //        .height(BottomBarHeight)
        ) {
            Destinations.forEach { dest ->
                val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                Column(
                    Modifier
                        .weight(1f)
                        .clickable {
                            onNavigate(dest.route)
                        }
                        .wrapContentHeight()
                        .alpha(
                            if (selected) 1f else 0.6f
                        )
                        .scale(
                            if (selected) 1.2f else 1f
                        )
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = stringResource(dest.name),
                        modifier = Modifier
                            .weight(1.5f)

                    )
                    Text(
                        text = stringResource(dest.name), modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                        ,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}

val Destinations = listOf(
    BottomNavDestination.VoicesDestination,
    BottomNavDestination.RecordDestination,
    BottomNavDestination.SettingsDestination
)

@Preview
@Composable
fun MyBottomBarPreview() {
    MyBottomBar(navController = rememberNavController(), onNavigate = {})
}