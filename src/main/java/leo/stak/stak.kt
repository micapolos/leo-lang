package leo.stak

import leo.base.fold
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script

// push = O(log(n))
// pop = O(1)
// pop(n) = O(log(n))
// top = O(1)
// size = O(log(n))
// get(index) = O(log(n))
data class Stak<out T : Any>(
	val nodeOrNull: Node<T>?) {
	override fun toString() = scriptLine { script(toString()) }.toString()
}

data class Node<out T : Any>(
	val value: T,
	val linkOrNull: Link<T>?) {
	override fun toString() = scriptLine { script(toString()) }.toString()
}

data class Link<out T : Any>(
	val node: Node<T>,
	val linkOrNull: Link<T>?) {
	override fun toString() = scriptLine { script(toString()) }.toString()
}

fun <T : Any> stak(nodeOrNull: Node<T>?) = Stak(nodeOrNull)
fun <T : Any> link(node: Node<T>, linkOrNull: Link<T>?) = Link(node, linkOrNull)
fun <T : Any> node(value: T, linkOrNull: Link<T>?) = Node(value, linkOrNull)

fun <T : Any> emptyStak(): Stak<T> = stak(null)
fun <T : Any> stakOf(vararg values: T): Stak<T> =
	emptyStak<T>().fold(values) { push(it) }

val <T : Any> Stak<T>.top: T?
	get() =
		nodeOrNull?.value

fun <T : Any> Stak<T>.top(index: Int): T? =
	nodeOrNull?.top(index)

val <T : Any> Stak<T>.pop: Stak<T>?
	get() =
		nodeOrNull?.let { stak(it.pop) }

fun <T : Any> Stak<T>.pop(count: Int): Stak<T>? =
	nodeOrNull?.let { stak(it.pop(count)) }

fun <T : Any> Stak<T>.push(value: T): Stak<T> =
	stak(nodeOrNull.push(value))

val <T : Any> Stak<T>.size: Int
	get() =
		nodeOrNull?.size ?: 0

operator fun <T : Any> Stak<T>.get(index: Int): T? =
	top(size - index - 1)

fun <R, T : Any> R.fold(stak: Stak<T>, fn: R.(T) -> R): R =
	if (stak.nodeOrNull == null) this
	else fold(stak.nodeOrNull, fn)

fun <T : Any> Node<T>.top(index: Int): T? =
	pop(index)?.value

val <T : Any> Node<T>.pop: Node<T>?
	get() =
		linkOrNull?.node

fun <T : Any> Node<T>.pop(count: Int): Node<T>? =
	if (count == 0) this
	else linkOrNull?.pop(count, 1)

fun <T : Any> Link<T>.pop(count: Int, depth: Int): Node<T>? {
	val newCount = count - depth
	return if (newCount > 0)
		if (linkOrNull == null) node.pop(newCount)
		else if (count - depth.shl(1) >= 0) linkOrNull.pop(count, depth.shl(1))
		else node.pop(newCount)
	else
		if (newCount == 0) node
		else null
}

tailrec fun <T : Any> Link<T>.leafNode(depth: Int): Node<T>? =
	if (depth == 1 && linkOrNull == null) node
	else linkOrNull?.leafNode(depth.ushr(1))

fun <T : Any> Node<T>.pushLink(depth: Int): Link<T>? {
	return if (linkOrNull == null) link(this, null)
	else {
		val leafNode = linkOrNull.leafNode(depth)
		return if (leafNode == null) link(this, null)
		else link(this, leafNode.pushLink(depth.shl(1)))
	}
}

fun <T : Any> Node<T>?.push(value: T): Node<T> =
	if (this == null) node(value, null)
	else node(value, pushLink(1))

tailrec fun <R, T : Any> R.fold(node: Node<T>, fn: R.(T) -> R): R {
	val folded = fn(node.value)
	return if (node.linkOrNull == null) folded
	else folded.fold(node.linkOrNull.node, fn)
}

val <T : Any> Node<T>.size: Int
	get() =
		linkOrNull?.size ?: 1

val <T : Any> Link<T>.size: Int
	get() {
		var size = 1
		var depth = 1
		var link = this
		while (true) {
			val nextLink = link.linkOrNull
			if (nextLink != null) {
				depth = depth.shl(1)
				link = nextLink
			} else {
				size += depth
				val nextNodeLink = link.node.linkOrNull
				if (nextNodeLink == null) break
				else {
					depth = 1
					link = nextNodeLink
				}
			}
		}
		return size
	}

fun <T : Any> Stak<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"stak" lineTo script(
		nodeOrNull?.scriptLine(fn) ?: "node" lineTo script("null"))

fun <T : Any> Node<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"node" lineTo script(
		"value" lineTo value.fn(),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))

fun <T : Any> Link<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"link" lineTo script(
		node.scriptLine(fn),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))