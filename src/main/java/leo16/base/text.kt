package leo16.base

import leo.base.appendableString
import leo.base.charSeq
import leo.base.fold
import leo13.Stack
import leo13.fold
import leo13.push
import leo13.reverse
import leo13.stack

data class Text(val charStack: Stack<Char>)

val Stack<Char>.text get() = Text(this)
fun Text.plus(char: Char) = charStack.push(char).text
val emptyText get() = stack<Char>().text
val String.text get() = emptyText.fold(charSeq) { plus(it) }
val Text.string get() = appendableString { it.fold(charStack.reverse) { append(it) } }