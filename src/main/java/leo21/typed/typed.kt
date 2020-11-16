package leo21.typed

import leo.base.fold
import leo13.Link
import leo13.linkTo
import leo21.type.Choice
import leo21.type.Line
import leo21.type.Struct
import leo21.type.Type
import leo21.type.isStatic
import leo21.type.linkOrNull
import leo21.type.plus
import leo21.type.resolve
import leo21.type.switch
import leo21.type.type

data class Typed<out V, out T>(val v: V, val t: T)

infix fun <V, T> V.of(t: T) = Typed(this, t)

fun <V, R> Typed<V, Type>.switch(
	structFn: (Typed<V, Struct>) -> R,
	choiceFn: (Typed<V, Choice>) -> R
): R =
	t.switch(
		{ struct -> structFn(v of struct) },
		{ choice -> choiceFn(v of choice) },
		{ recursive -> v.of(recursive.resolve).switch(structFn, choiceFn) },
		{ recurse -> null!! })

val <V> Typed<V, Type>.structOrNull: Typed<V, Struct>? get() = switch({ it }, { null })
val <V> Typed<V, Type>.choiceOrNull: Typed<V, Choice>? get() = switch({ null }, { it })
val <V> Typed<V, Struct>.typed: Typed<V, Type> @JvmName("structTyped") get() = v of type(t)
val <V> Typed<V, Choice>.typed: Typed<V, Type> @JvmName("choiceTyped") get() = v of type(t)

@JvmName("typePlus")
fun <V> Typed<V, Type>.plus(rhs: Typed<V, Line>, plusFn: V.(V) -> V): Typed<V, Type> =
	structOrNull!!.plus(rhs, plusFn).typed

@JvmName("structPlus")
fun <V> Typed<V, Struct>.plus(rhs: Typed<V, Line>, plusFn: V.(V) -> V): Typed<V, Struct> =
	(if (t.isStatic)
		if (rhs.t.isStatic) v
		else rhs.v
	else
		if (rhs.t.isStatic) v
		else v.plusFn(rhs.v)) of t.plus(rhs.t)

fun <V> Typed<V, Struct>.linkOrNull(pairFn: V.() -> Pair<V, V>): Link<Typed<V, Struct>, Typed<V, Line>>? =
	t.linkOrNull?.let { structLink ->
		(if (structLink.tail.isStatic)
			if (structLink.head.isStatic) v to v
			else v to v
		else
			if (structLink.head.isStatic) v to v
			else v.pairFn()).let { (lhs, rhs) ->
			lhs.of(structLink.tail) linkTo rhs.of(structLink.head)
		}
	}

fun <V> typed(nilValue: V, plusFn: V.(V) -> V, vararg lines: Typed<V, Line>): Typed<V, Type> =
	nilValue.of(type()).fold(lines) { plus(it, plusFn) }