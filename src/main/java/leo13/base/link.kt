package leo13.base

import leo.base.Seq
import leo.base.SeqNode
import leo.base.then
import leo13.LeoObject
import leo13.Scriptable
import leo13.linkName
import leo13.script.Script
import leo13.script.plus
import leo13.script.script

data class Link<out V : Scriptable>(val lhsOrNull: Link<V>?, val value: V) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = linkName
	override val scriptableBody: Script get() = (lhsOrNull?.scriptableBody ?: script()).plus(value.scriptableLine)
}

infix fun <V : Scriptable> Link<V>?.linkTo(value: V) = Link(this, value)
val <V : Scriptable> Link<V>.seqNode: SeqNode<V> get() = value then Seq { lhsOrNull?.seqNode }
fun <V : Scriptable, R> Link<V>.mapFirst(fn: V.() -> R?): R? = value.fn() ?: lhsOrNull?.mapFirst(fn)
