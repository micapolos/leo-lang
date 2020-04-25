package leo15.lambda.runtime

import leo.base.ifNotNull
import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.get
import leo.stak.push
import leo15.string

typealias Scope<T> = Stak<Thunk<T>>
typealias ApplyFn<T> = T.(T) -> T

data class Thunk<out T>(val scope: Scope<T>, val atom: Atom<T>) {
	override fun toString() = anyScriptLine.string
}

val anyEmptyScope: Scope<Any?> = emptyStak()

@Suppress("UNCHECKED_CAST")
fun <T> emptyScope(): Scope<T> = anyEmptyScope as Scope<T>

@Suppress("UNCHECKED_CAST")
val Term<Any?>.anyEval: Thunk<Any?>
	get() =
		eval {
			(this as ((Any?) -> Any?)).invoke(it)
		}

fun <T> Term<T>.eval(applyFn: ApplyFn<T>): Thunk<T> =
	eval(emptyScope(), applyFn)

fun <T> Term<T>.eval(scope: Scope<T>, applyFn: ApplyFn<T>): Thunk<T> =
	atom.apply(scope).ifNotNull(applicationOrNull) { apply(scope, it, applyFn) }

fun <T> Atom<T>.apply(scope: Scope<T>): Thunk<T> =
	when (this) {
		is IndexAtom -> scope[index]!!
		is ValueAtom -> Thunk(emptyScope(), this)
		is TermAtom -> Thunk(scope, this)
	}

fun <T> Thunk<T>.apply(termScope: Scope<T>, term: Term<T>, applyFn: ApplyFn<T>): Thunk<T> {
	val rhs = term.eval(termScope, applyFn)
	return when (atom) {
		is IndexAtom -> null!!
		is ValueAtom -> Thunk(emptyScope(), ValueAtom(atom.value.applyFn(rhs.atom.value)))
		is TermAtom -> atom.term.eval(scope.push(rhs), applyFn)
	}
}

tailrec fun <T> Thunk<T>.apply(scope: Scope<T>, applicationOrNull: Application<T>, applyFn: ApplyFn<T>): Thunk<T> {
	val rhs = applicationOrNull.term.eval(scope, applyFn)
	val innerScope = scope.push(rhs)
	return if (applicationOrNull.applicationOrNull == null) {
		when (atom) {
			is IndexAtom -> null!!
			is ValueAtom -> Thunk(emptyScope(), ValueAtom(atom.value.applyFn(rhs.atom.value)))
			is TermAtom -> {
				val lhs = atom.term.atom.apply(innerScope)
				if (atom.term.applicationOrNull == null) lhs
				else apply(innerScope, atom.term.applicationOrNull, applyFn)
			}
		}
	} else if (atom is TermAtom && atom.term.applicationOrNull == null) {
		val lhs = atom.term.atom.apply(innerScope)
		lhs.apply(innerScope, applicationOrNull, applyFn)
	} else {
		val lhs = when (atom) {
			is IndexAtom -> null!!
			is ValueAtom -> Thunk(emptyScope(), ValueAtom(atom.value.applyFn(rhs.atom.value)))
			is TermAtom -> apply(scope, applicationOrNull.term, applyFn)
		}
		lhs.apply(scope, applicationOrNull.applicationOrNull, applyFn)
	}
}
