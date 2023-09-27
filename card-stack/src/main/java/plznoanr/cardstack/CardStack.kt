package plznoanr.cardstack

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import plznoanr.cardstack.animation.VerticalAnimationStyle
import plznoanr.cardstack.ui.Orientation
import plznoanr.cardstack.ui.ShowCard
import plznoanr.cardstack.ui.VerticalAlignment

private const val DEFAULT_ANIM_DURATION = 130
private val defaultPaddingBetweenItems by lazy { 10.dp }
private val defaultCardElevation by lazy { 1.dp }
private val defaultOrientation by lazy {
    Orientation.Vertical(
        alignment = VerticalAlignment.BottomToTop,
        animationStyle = VerticalAnimationStyle.ToRight
    )
}

@ExperimentalMaterialApi
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    cardCount: Int,
    cardElevation: Dp = defaultCardElevation,
    paddingBetweenCards: Dp = defaultPaddingBetweenItems,
    animationDuration: Int = DEFAULT_ANIM_DURATION,
    cardShape: Shape = RoundedCornerShape(0.dp),
    cardBorder: BorderStroke? = null,
    onCardClick: (Int) -> Unit = {},
    orientation: Orientation = defaultOrientation,
    cardContent: @Composable (Int) -> Unit,
) {
    checkCardCount(cardCount)
    checkPadding(paddingBetweenCards)
    checkAnimationDuration(animationDuration)

    val runAnimations = animationDuration > 0
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .background(Color.Transparent)
    ) {
        (0 until cardCount).forEachIndexed { index, _ ->
            ShowCard(
                coroutineScope = coroutineScope,
                runAnimations = runAnimations,
                selectedIndex = selectedIndex,
                index = index,
                cardCount = cardCount,
                paddingBetweenCards = paddingBetweenCards,
                animationDuration = animationDuration,
                orientation = orientation,
                cardElevation = cardElevation,
                cardShape = cardShape,
                cardBorder = cardBorder,
                onCardClick = onCardClick,
                newIndexBlock = { selectedIndex = it },
                composable = { cardContent(index) }
            )
        }
    }
}

private fun checkCardCount(cardCount: Int) {
    if (cardCount < 2)
        throw IllegalArgumentException("Can't use 1 or less card count.")
}

private fun checkPadding(paddingBetweenCards: Dp) {
    if (paddingBetweenCards <= 0.dp)
        throw IllegalArgumentException("Can't use 0 or less for padding between cards.")
}

private fun checkAnimationDuration(animationDuration: Int) {
    if (animationDuration < 1)
        throw IllegalArgumentException("Can't use 0 or less for animation duration.")
}