package leo.stak

typealias Node2 = Array<*>
typealias Stak2 = Node2?

val emptyStak2: Stak2 = null

fun stak2(value: Any, vararg links: Stak2): Stak2 =
	arrayOf(value, *links)

val Stak2.stakSize: Int
	get() {
		if (this == null) return 0
		var node: Node2 = this
		var size = 1
		while (node.size != 1) {
			size += 1.shl(node.size - 2)
			node = node[node.size - 1] as Node2
		}
		return size
	}

val Stak2.isEmpty: Boolean
	get() =
		this == null

val Stak2.stakTop: Any?
	get() =
		this!![0]

val Stak2.stakPop: Stak2
	get() =
		this!![1] as Stak2
