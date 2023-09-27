package plznoanr.cardstack.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import plznoanr.cardstack.animation.AnimationType
import plznoanr.cardstack.animation.isRight

@ExperimentalMaterialApi
@Composable
internal fun ShowCard(
    coroutineScope: CoroutineScope,
    runAnimations: Boolean,
    selectedIndex: Int,
    index: Int,
    cardCount: Int,
    paddingBetweenCards: Dp,
    animationDuration: Int,
    orientation: Orientation,
    cardElevation: Dp,
    cardShape: Shape,
    cardBorder: BorderStroke?,
    onCardClick: (Int) -> Unit,
    newIndexBlock: (Int) -> Unit,
    composable: @Composable (Int) -> Unit,
) {
    var itemPxSize = 0
    var animationType by remember { mutableStateOf(AnimationType.None) }

    val padding = when {
        selectedIndex == index -> 0.dp
        selectedIndex < index -> ((index - selectedIndex) * paddingBetweenCards.value).dp
        selectedIndex > index -> ((cardCount - selectedIndex + index) * paddingBetweenCards.value).dp
        else -> throw IllegalStateException()
    }

    val bottomPaddingAnimation by animateDpAsState(
        padding,
        tween(animationDuration, easing = FastOutSlowInEasing), label = ""
    )

    val offsetAnimation = remember { Animatable(0f) }
    val rotateAnimation = remember { Animatable(0f) }

    val offsetValues = IntOffset(
        if (animationType.isRight())
            offsetAnimation.value.toInt()
        else
            -offsetAnimation.value.toInt(), 0
    )

    val paddingModifier = when {
        orientation is Orientation.Vertical && orientation.alignment == VerticalAlignment.TopToBottom -> PaddingValues(
            top = bottomPaddingAnimation
        )

        orientation is Orientation.Vertical && orientation.alignment == VerticalAlignment.BottomToTop -> PaddingValues(
            bottom = bottomPaddingAnimation,
        )

        orientation is Orientation.Horizontal && orientation.alignment == HorizontalAlignment.StartToEnd -> PaddingValues(
            start = bottomPaddingAnimation
        )

        else -> PaddingValues(end = bottomPaddingAnimation)
    }

    val modifier = Modifier
        .padding(paddingModifier)
        .zIndex(-padding.value)
        .offset { offsetValues }
        .rotate(rotateAnimation.value)
        .onSizeChanged {
            itemPxSize = if (orientation is Orientation.Vertical) {
                if (itemPxSize > it.width)
                    itemPxSize
                else
                    it.width
            } else {
                if (itemPxSize > it.height)
                    itemPxSize
                else
                    it.height
            }
        }

    var offsetX by remember { mutableFloatStateOf(0f) }

    Card(
        elevation = cardElevation,
        shape = cardShape,
        modifier = modifier
            .draggable(
                orientation = androidx.compose.foundation.gestures.Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                },
                onDragStopped = {
                    animationType = when {
                        offsetX > 0 -> AnimationType.Right
                        offsetX < 0 -> AnimationType.Left
                        else -> AnimationType.None
                    }
                    if (animationType != AnimationType.None) {
                        animateSlide(
                            coroutineScope = coroutineScope,
                            pxValue = itemPxSize,
                            runAnimations = runAnimations,
                            animationDuration = animationDuration,
                            index = index,
                            cardCount = cardCount,
                            offsetAnimation = offsetAnimation,
                            newIndexBlock = newIndexBlock,
                            animationType = animationType
                        )
                    }
                    offsetX = 0f
                    coroutineScope.launch {
                        delay(100)
                    }
                }

            ),
        border = cardBorder,
        onClick = {
            if (cardCount > 1 && selectedIndex == index) {
                onCardClick.invoke(index)
            }
        }
    ) {
        composable.invoke(index)
    }
}

private fun animateSlide(
    coroutineScope: CoroutineScope,
    pxValue: Int,
    runAnimations: Boolean,
    animationDuration: Int,
    index: Int,
    cardCount: Int,
    offsetAnimation: Animatable<Float, AnimationVector1D>,
    newIndexBlock: (Int) -> Unit,
    animationType: AnimationType
) {
    val spec: TweenSpec<Float> = tween(animationDuration, easing = FastOutLinearInEasing)

    coroutineScope.launch {
        if (runAnimations)
            offsetAnimation.animateTo(pxValue.toFloat(), spec)

        val newIndex = if (animationType.isRight()) {
            if (cardCount > index + 1) {
                index + 1
            } else {
                0
            }
        } else {
            if (0 <= index - 1) {
                index - 1
            } else {
                cardCount - 1
            }
        }

        newIndexBlock.invoke(newIndex)

        if (runAnimations) {
            launch { offsetAnimation.animateTo(0f, spec) }
        }
    }
}