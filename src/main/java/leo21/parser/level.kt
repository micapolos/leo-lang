package leo21.parser

import leo13.Link
import leo13.Stack
import leo13.linkOrNull
import leo13.push
import leo13.stack

inline class Level(val multiIndentStack: Stack<MultiIndent>)

val emptyLevel = Level(stack())
fun Level.plus(multiIndent: MultiIndent) = Level(multiIndentStack.push(multiIndent))
val Level.linkOrNull: Link<Level, MultiIndent>? get() = multiIndentStack.linkOrNull(::Level)