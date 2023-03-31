package com.droidcon.composemotions.ui

import com.droidcon.composemotions.R


/**
 * Sealed class defining all screen items that will be used in the flowchart i.e. including *Start*,
 * *Decision* and *Terminal* screens
 */
sealed class Screen(val route: String, val description: Int) {
    object Start : Screen("start", R.string.start)

    //Decision Blocks
    object AnimateLayoutContentDecision :
        Screen(route = "animate_layout_content_dec", R.string.animate_content_change_in_layout)

    object AnimateAppearanceDecision :
        Screen(route = "animate_appearance_dec", R.string.animate_appearance_or_disappearance)

    object SwapContentDecision :
        Screen(route = "swap_content_dec", R.string.swapping_content_based_on_state)

    object StateBasedDecision :
        Screen(route = "state_based_dec", R.string.state_based_and_happens_during_composition)

    object IsAnimationInfiniteDecision :
        Screen(route = "infinite_anim_dec", R.string.animation_is_infinite)

    object AnimateMultipleValuesDecision : Screen(
        "animate_multiple_value_simultaneously",
        R.string.animate_multiple_values_simultaneously
    )

    object FineAnimationControlDecision : Screen(
        "need_fine_control_over_animation_time",
        R.string.need_fine_control_over_animation_time
    )

    object AnimationOnlyTruthSourceDecision :
        Screen("animation_is_only_source_of_truth", R.string.animation_is_only_source_of_truth)

    //Destination Blocks
    object AnimatedVisibility : Screen("animated_visibility", R.string.animated_visibility)
    object AnimatedContentOrCrossfade :
        Screen("animated_content_or_crossfade", R.string.animated_content_or_crossfade)

    object AnimateContentSize : Screen("animate_content_size", R.string.animate_content_size)
    object RememberInfiniteTransition :
        Screen("remember_infinite_transition", R.string.remember_infinite_transition)

    object UpdateTransition : Screen("update_transition", R.string.update_transition)
    object AnimateSthAsState :
        Screen("animate_something_as_state", R.string.animate_something_as_state)

    object Animation : Screen("animation", R.string.animation)
    object Animatable : Screen("animatable", R.string.animatable)
    object AnimationStateOrAnimate :
        Screen("animation_state_or_animate", R.string.animation_state_or_animate)


}