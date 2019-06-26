package leo7

import leo.base.Stack
import leo.base.push
import leo.base.stackOrNull

data class Script(val lineStackOrNull: Stack<Line>?)

fun script(vararg lines: Line) = Script(stackOrNull(*lines))
operator fun Script.plus(line: Line) = Script(lineStackOrNull.push(line))
