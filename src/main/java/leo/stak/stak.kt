package leo.stak

import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script

data class StakLink<out T : Any>(
	val stak: Stak<T>,
	val linkOrNull: StakLink<T>?) {
	override fun toString() = scriptLine { script(toString()) }.toString()
}

data class Stak<out T : Any>(
	val value: T,
	val linkOrNull: StakLink<T>?) {
	override fun toString() = scriptLine { script(toString()) }.toString()
}

fun <T : Any> link(stak: Stak<T>, linkOrNull: StakLink<T>?) = StakLink(stak, linkOrNull)
fun <T : Any> stak(value: T, linkOrNull: StakLink<T>?) = Stak(value, linkOrNull)

fun <T : Any> Stak<T>.get(index: Int): T? =
	pop(index)?.value

val <T : Any> Stak<T>.pop: Stak<T>?
	get() =
		linkOrNull?.stak

fun <T : Any> Stak<T>.pop(count: Int): Stak<T>? =
	if (count == 0) this
	else linkOrNull?.pop(count, 1)

fun <T : Any> StakLink<T>.pop(count: Int, depth: Int): Stak<T>? {
	val newCount = count - depth
	return if (newCount > 0)
		if (linkOrNull == null) stak.pop(newCount)
		else if (count - depth.shl(1) >= 0) linkOrNull.pop(count, depth.shl(1))
		else stak.pop(newCount)
	else
		if (newCount == 0) stak
		else null
}

tailrec fun <T : Any> StakLink<T>.leafStak(depth: Int): Stak<T>? =
	if (depth == 1 && linkOrNull == null) stak
	else linkOrNull?.leafStak(depth.ushr(1))

fun <T : Any> Stak<T>.pushLink(depth: Int): StakLink<T>? {
	return if (linkOrNull == null) link(this, null)
	else {
		val leafStak = linkOrNull.leafStak(depth)
		return if (leafStak == null) link(this, null)
		else link(this, leafStak.pushLink(depth.shl(1)))
	}
}

fun <T : Any> Stak<T>?.push(value: T): Stak<T> =
	if (this == null) stak(value, null)
	else stak(value, pushLink(1))

fun <T : Any> Stak<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"stak" lineTo script(
		"value" lineTo value.fn(),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))

fun <T : Any> StakLink<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"link" lineTo script(
		stak.scriptLine(fn),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))