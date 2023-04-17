package com.droidcon.composemotions.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.Screen

/**
 * A decision box with YES and NO buttons that result in relevant navigation when tapped
 * @param modifier Optional modifier to apply to the layout
 * @param yesScreen *Screen* instance to navigate to when the user taps **YES**
 * @param noScreen *Screen* instance to navigate to when the user taps **NO**
 * @param question Question to decide on
 * @param onYes Where to go when **YES** is selected
 * @param onNo Where to go when **NO** is selected
 */
@Composable
fun Decision(
    modifier: Modifier = Modifier,
    question: String,
    yesScreen: Screen,
    noScreen: Screen,
    onYes: (String) -> Unit,
    onNo: (String) -> Unit
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    MaterialTheme.shapes.medium
                )
        ) {
            Text(
                text = question, modifier = Modifier
                    .padding(16.dp)
                    .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.headlineLarge
            )
            Button(
                onClick = { onYes(yesScreen.route) }, modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.4f)
                    .align(Alignment.BottomEnd)
            ) {
                Text(text = stringResource(R.string.yes),
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Button(onClick = { onNo(noScreen.route) }, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.4f)
                .align(Alignment.BottomStart),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = stringResource(R.string.no),
                    style = MaterialTheme.typography.displaySmall
                    )
            }
        }
    }
}

@Preview
@Composable
fun DecisionPreview() {
    Decision(modifier = Modifier
        .fillMaxSize()
        ,
        question = "Animate layout changes?",
        yesScreen = Screen.AnimateAppearanceDecision,
        noScreen = Screen.StateBasedDecision,
        onYes = {}, onNo = {})
}