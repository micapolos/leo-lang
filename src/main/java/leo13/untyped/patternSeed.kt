package leo13.untyped

import leo.base.Empty

sealed class PatternSeed

data class EmptyPatternSeed(val empty: Empty) : PatternSeed()
data class ChoicePatternSeed(val choice: Choice) : PatternSeed()
data class AnythingPatternSeed(val anything: Anything) : PatternSeed()

fun patternSeed(empty: Empty): PatternSeed = EmptyPatternSeed(empty)
fun patternSeed(choice: Choice): PatternSeed = ChoicePatternSeed(choice)
fun patternSeed(anything: Anything): PatternSeed = AnythingPatternSeed(anything)

