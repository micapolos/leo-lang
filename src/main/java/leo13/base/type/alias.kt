package leo13.base.type

fun <V : Any> type(name: String, type: Type<V>): Type<V> =
	type(name, field(type) { this }) { it }

fun <V : Any, A : Any> type(type: Type<A>, getFn: V.() -> A, newFn: A.() -> V): Type<V> =
	Type(
		type.name,
		{ type.bodyScript(getFn()) },
		{ type.unsafeValue(this).newFn() })
