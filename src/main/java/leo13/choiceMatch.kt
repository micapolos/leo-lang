package leo13

import leo9.Stack
import leo9.stack

data class ChoiceMatch(val eitherMatchStack: Stack<EitherMatch>)

val Stack<EitherMatch>.choiceMatch get() = ChoiceMatch(this)
fun choiceMatch(vararg eitherMatches: EitherMatch) = stack(*eitherMatches).choiceMatch
