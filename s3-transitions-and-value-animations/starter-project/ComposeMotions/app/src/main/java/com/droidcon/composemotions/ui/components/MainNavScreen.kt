@file:OptIn(ExperimentalAnimationApi::class)

package com.droidcon.composemotions.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.droidcon.composemotions.ui.Screen
import com.droidcon.composemotions.ui.components.animations.AnimatableSheet
import com.droidcon.composemotions.ui.components.animations.AnimateAsStateSheet
import com.droidcon.composemotions.ui.components.animations.AnimateContentSizeSheet
import com.droidcon.composemotions.ui.components.animations.AnimatedContentCrossfadeSheet
import com.droidcon.composemotions.ui.components.animations.AnimatedVisibilitySheet
import com.droidcon.composemotions.ui.components.animations.AnimationSheet
import com.droidcon.composemotions.ui.components.animations.InfiniteTransitionSheet
import com.droidcon.composemotions.ui.components.animations.AnimationStateOrAnimateSheet
import com.droidcon.composemotions.ui.components.animations.UpdateTransitionSheet
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

/**
 * Main navigation entry for the application
 * @param navController NavHostController passed to the navigation graph
 */
@Composable
fun MainNavScreen(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = Screen.Start.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -it })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        }
    ) {
        composable(Screen.Start.route) {
            Box(Modifier.fillMaxSize()) {
                Start(
                    onNext = {
                        navController.navigate(it)
                    })
            }
        }

        //Decision destinations
        composable(Screen.AnimateLayoutContentDecision.route) {
            Decision(
                question = stringResource(Screen.AnimateLayoutContentDecision.description),
                yesScreen = Screen.AnimateAppearanceDecision,
                noScreen = Screen.StateBasedDecision,
                onYes = {
                    navController.navigate(it)
                },
                onNo = {
                    navController.navigate(it)
                })
        }
        composable(Screen.AnimateAppearanceDecision.route) {
            Decision(
                question = stringResource(Screen.AnimateAppearanceDecision.description),
                yesScreen = Screen.AnimatedVisibility,
                noScreen = Screen.SwapContentDecision,
                onYes = {
                    navController.navigate(it)
                },
                onNo = {
                    navController.navigate(it)
                }
            )
        }
        composable(Screen.SwapContentDecision.route) {
            Decision(
                question = stringResource(Screen.SwapContentDecision.description),
                yesScreen = Screen.AnimatedContentOrCrossfade,
                noScreen = Screen.AnimateContentSize,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) }
            )
        }
        composable(Screen.StateBasedDecision.route) {
            Decision(
                question = stringResource(Screen.StateBasedDecision.description),
                yesScreen = Screen.IsAnimationInfiniteDecision,
                noScreen = Screen.FineAnimationControlDecision,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) }
            )
        }
        composable(Screen.IsAnimationInfiniteDecision.route) {
            Decision(
                question = stringResource(Screen.IsAnimationInfiniteDecision.description),
                yesScreen = Screen.RememberInfiniteTransition,
                noScreen = Screen.AnimateMultipleValuesDecision,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) },
            )
        }
        composable(Screen.AnimateMultipleValuesDecision.route) {
            Decision(
                question = stringResource(Screen.AnimateMultipleValuesDecision.description),
                yesScreen = Screen.UpdateTransition,
                noScreen = Screen.AnimateSthAsState,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) }
            )
        }
        composable(Screen.FineAnimationControlDecision.route) {
            Decision(
                question = stringResource(Screen.FineAnimationControlDecision.description),
                yesScreen = Screen.Animation,
                noScreen = Screen.AnimationOnlyTruthSourceDecision,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) }
            )
        }
        composable(Screen.AnimationOnlyTruthSourceDecision.route) {
            Decision(
                question = stringResource(Screen.AnimationOnlyTruthSourceDecision.description),
                yesScreen = Screen.Animatable,
                noScreen = Screen.AnimationStateOrAnimate,
                onYes = { navController.navigate(it) },
                onNo = { navController.navigate(it) }
            )
        }

        //Animation showcase destinations
        composable(Screen.AnimatedVisibility.route) {

            Terminal(
                header = { Header(stringResource(Screen.AnimatedVisibility.description)) },
                modifier = Modifier.fillMaxSize().background(Color.Red)
            ){
                AnimationContainer {
                    AnimatedVisibilitySheet()
                }
            }
        }
        composable(Screen.AnimatedContentOrCrossfade.route) {
            Terminal(
                header = { Header(stringResource(Screen.AnimatedContentOrCrossfade.description)) },
            ){
                AnimationContainer {
                    AnimatedContentCrossfadeSheet()
                }
            }
        }
        composable(Screen.AnimateContentSize.route) {
            Terminal(
                header = { Header(stringResource(Screen.AnimateContentSize.description)) },
            ){
                AnimationContainer {
                    AnimateContentSizeSheet()
                }
            }
        }
        composable(Screen.RememberInfiniteTransition.route) {
            Terminal(
                header = { Header(stringResource(Screen.RememberInfiniteTransition.description)) },
            ){
                AnimationContainer {
                    InfiniteTransitionSheet()
                }
            }
        }
        composable(Screen.UpdateTransition.route) {
            Terminal(
                header = { Header(stringResource(Screen.UpdateTransition.description)) },
            ){
                AnimationContainer {
                    UpdateTransitionSheet()
                }
            }
        }
        composable(Screen.AnimateSthAsState.route) {
            Terminal(
                header = { Header(stringResource(Screen.AnimateSthAsState.description)) },
            ){
                AnimationContainer {
                    AnimateAsStateSheet()
                }
            }
        }
        composable(Screen.Animation.route) {
            Terminal(
                header = { Header(stringResource(Screen.Animation.description)) },
            ){
                AnimationContainer {
                    AnimationSheet()
                }
            }
        }
        composable(Screen.Animatable.route) {
            Terminal(
                header = { Header(stringResource(Screen.Animatable.description)) },
            ){
                AnimationContainer {
                    AnimatableSheet()
                }
            }
        }
        composable(Screen.AnimationStateOrAnimate.route) {
            Terminal(
                header = { Header(stringResource(Screen.AnimationStateOrAnimate.description)) },
            ){
                AnimationContainer {
                    AnimationStateOrAnimateSheet()
                }
            }
        }


    }
}