package leo15.lambda.runtime

import leo.base.ifNotNull
import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.top
import leo15.string

typealias Scope<T> = Stak<Closure<T>>
typealias ApplyFn<T> = T.(T) -> Atom<T>

const val tailOptimization = true

data class Closure<out T>(val scope: Scope<T>, val atom: Atom<T>) {
	override fun toString() = anyScriptLine.string
}

infix fun <T> Scope<T>.closure(atom: Atom<T>) = Closure(this, atom)

fun <T> emptyScope(): Scope<T> = emptyStak()

val Term<Nothing>.eval: Closure<Nothing>
	get() =
		eval { null!! }

fun <T> Term<T>.eval(applyFn: ApplyFn<T>): Closure<T> =
	eval(emptyScope(), applyFn)

fun <T> Term<T>.eval(scope: Scope<T>, applyFn: ApplyFn<T>): Closure<T> =
	atom.apply(scope).ifNotNull(applicationOrNull) { apply(scope, it, applyFn) }

fun <T> Atom<T>.apply(scope: Scope<T>): Closure<T> =
	when (this) {
		is IndexAtom -> scope.top(index)!!
		is ValueAtom -> Closure(emptyScope(), this)
		is LambdaAtom -> Closure(scope, this)
	}

fun <T> Closure<T>.apply(termScope: Scope<T>, term: Term<T>, applyFn: ApplyFn<T>): Closure<T> {
	val rhs = term.eval(termScope, applyFn)
	return when (atom) {
		is IndexAtom -> null!!
		is ValueAtom -> Closure(emptyScope(), atom.value.applyFn(rhs.atom.value))
		is LambdaAtom -> atom.body.eval(scope.push(rhs), applyFn)
	}
}

tailrec fun <T> Closure<T>.apply(applicationScope: Scope<T>, applicationOrNull: Application<T>, applyFn: ApplyFn<T>): Closure<T> {
	val rhs = applicationOrNull.term.eval(applicationScope, applyFn)
	val innerScope = scope.push(rhs)
	return if (applicationOrNull.applicationOrNull == null) {
		when (atom) {
			is IndexAtom -> null!!
			is ValueAtom -> Closure(emptyScope(), atom.value.applyFn(rhs.atom.value))
			is LambdaAtom ->
				if (tailOptimization) {
					val lhs = atom.body.atom.apply(innerScope)
					if (atom.body.applicationOrNull == null) lhs
					else lhs.apply(innerScope, atom.body.applicationOrNull, applyFn)
				} else {
					atom.body.eval(innerScope, applyFn)
				}
		}
	} else if (tailOptimization && atom is LambdaAtom && atom.body.applicationOrNull == null) {
		val lhs = atom.body.atom.apply(innerScope)
		lhs.apply(applicationScope, applicationOrNull.applicationOrNull, applyFn)
	} else {
		val lhs = when (atom) {
			is IndexAtom -> null!!
			is ValueAtom -> Closure(emptyScope(), atom.value.applyFn(rhs.atom.value))
			is LambdaAtom -> atom.body.eval(innerScope, applyFn)
		}
		lhs.apply(applicationScope, applicationOrNull.applicationOrNull, applyFn)
	}
}

fun <T> Closure<T>.apply(rhs: Closure<T>, applyFn: ApplyFn<T>): Closure<T> =
	apply(rhs.scope, term(rhs.atom), applyFn)