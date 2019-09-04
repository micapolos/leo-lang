package leo13.base

import leo.base.Seq
import leo.base.fold
import leo13.LeoObject
import leo13.Scriptable
import leo13.orNullAsScript

data class List<out V : Scriptable>(val linkOrNull: Link<V>?) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "list"
	override val scriptableBody get() = linkOrNull.orNullAsScript
}

fun <V : Scriptable> list(linkOrNull: Link<V>?) = List(linkOrNull)
fun <V : Scriptable> List<V>.plus(value: V) = List(linkOrNull.linkTo(value))
fun <V : Scriptable> list(vararg values: V): List<V> = list<V>(null).fold(values) { plus(it) }
val <V : Scriptable> List<V>.seq: Seq<V> get() = Seq { linkOrNull?.seqNode }
fun <V : Scriptable, R> List<V>.mapFirst(fn: V.() -> R?): R? = linkOrNull?.mapFirst(fn)
