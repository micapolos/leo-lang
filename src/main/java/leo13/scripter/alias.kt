package leo13.scripter

import leo13.base.Iso

fun <V : Any> scripter(name: String, scripter: Scripter<V>): Scripter<V> =
	scripter(name, field(scripter) { this }) { it }

fun <V : Any, A : Any> scripter(scripter: Scripter<A>, getFn: V.() -> A, newFn: A.() -> V): Scripter<V> =
	Scripter(
		scripter.name,
		{ scripter.bodyScript(getFn()) },
		{ scripter.unsafeValue(this).newFn() })

fun <V : Any, A : Any> scripter(scripter: Scripter<A>, iso: Iso<V, A>): Scripter<V> =
	scripter(scripter, iso.ab, iso.ba)
