package leo13.script

import leo.base.notNullIf

data class ReaderCase<V, A>(val reader: Reader<A>, val fn: A.() -> V)

fun <V, A> case(reader: Reader<A>, fn: A.() -> V) = ReaderCase(reader, fn)
fun <V : Any, A> ReaderCase<V, A>.unsafeValueOrNull(line: ScriptLine): V? =
	notNullIf(line.name == reader.name) {
		reader.unsafeBodyValue(line.rhs).fn()
	}