package leo14

import leo13.Stack
import leo13.linkOrNull
import leo13.push
import leo13.stack

data class Undoable<T>(val lastDone: T, val previouslyDoneStack: Stack<T>)

fun <T> undoable(value: T) = Undoable(value, stack())
fun <T> Undoable<T>.doIt(fn: T.() -> T) = Undoable(lastDone.fn(), previouslyDoneStack.push(lastDone))
val <T> Undoable<T>.undoOrNull: Undoable<T>? get() = previouslyDoneStack.linkOrNull?.run { Undoable(value, stack) }
val <T> Undoable<T>.undoIfPossible: Undoable<T> get() = undoOrNull ?: this
