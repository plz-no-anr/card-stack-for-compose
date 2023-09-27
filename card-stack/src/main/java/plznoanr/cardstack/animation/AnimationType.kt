package plznoanr.cardstack.animation

enum class AnimationType {
    None,
    Right,
    Left
}

internal fun AnimationType.isRight(): Boolean = this == AnimationType.Right