package leo15.lambda.runtime

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.get
import leo.stak.push
import leo15.string

typealias Scope<T> = Stak<Thunk<T>>
typealias Apply<T> = T.(T) -> T

data class Thunk<out T>(val scope: Scope<T>, val atom: Atom<T>) {
	override fun toString() = anyScriptLine.string
}

val anyEmptyScope: Scope<Any?> = emptyStak()

@Suppress("UNCHECKED_CAST")
fun <T> emptyScope(): Scope<T> = anyEmptyScope as Scope<T>

tailrec fun <T> Thunk<T>.eval(applicationOrNull: Application<T>?, apply: Apply<T>): Thunk<T> =
	when (atom) {
		is IndexAtom ->
			scope[atom.index]!!.eval(applicationOrNull, apply)
		is ValueAtom ->
			if (applicationOrNull == null) Thunk(emptyScope(), atom)
			else Thunk(
				emptyScope(),
				ValueAtom(atom.value.apply(applicationOrNull.term.eval(scope, apply).atom.value)))
				.eval(applicationOrNull.applicationOrNull, apply)
		is TermAtom ->
			if (applicationOrNull == null) Thunk(scope, atom)
			else Thunk(
				scope.push(applicationOrNull.term.eval(scope, apply)),
				atom.term.atom)
				.eval(atom.term.applicationOrNull, apply)
	}

fun <T> Term<T>.eval(scope: Scope<T>, apply: Apply<T>): Thunk<T> =
	Thunk(scope, atom).eval(applicationOrNull, apply)

fun <T> Term<T>.eval(apply: Apply<T>): Thunk<T> =
	eval(emptyScope(), apply)

@Suppress("UNCHECKED_CAST")
val Term<Any?>.anyEval: Thunk<Any?>
	get() =
		eval {
			(this as ((Any?) -> Any?)).invoke(it)
		}

