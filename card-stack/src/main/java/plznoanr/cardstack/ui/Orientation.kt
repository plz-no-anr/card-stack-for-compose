package plznoanr.cardstack.ui

import plznoanr.cardstack.animation.HorizontalAnimationStyle
import plznoanr.cardstack.animation.VerticalAnimationStyle

sealed class Orientation {
    data class Vertical(
        val alignment: VerticalAlignment = VerticalAlignment.TopToBottom,
        val animationStyle: VerticalAnimationStyle = VerticalAnimationStyle.ToRight
    ) : Orientation()

    data class Horizontal(
        val alignment: HorizontalAlignment = HorizontalAlignment.StartToEnd,
        val animationStyle: HorizontalAnimationStyle = HorizontalAnimationStyle.FromTop
    ) : Orientation()
}