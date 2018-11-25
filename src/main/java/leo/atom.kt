package leo

import leo.base.appendableString
import leo.base.fail

sealed class Atom<out V>

data class MetaAtom<out V>(
	val meta: Meta<V>) : Atom<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class WordAtom<out V>(
	val word: Word) : Atom<V>() {
	override fun toString() = appendableString { it.append(this) }
}

val <V> Meta<V>.atom: Atom<V>
	get() =
		MetaAtom(this)

fun <V> Word.atom(): Atom<V> =
	WordAtom(this)

val Word.atom: Atom<Nothing>
	get() =
		atom()

// === casting

val <V> Atom<V>.wordAtomOrNull: WordAtom<V>?
	get() =
		this as? WordAtom

val <V> Atom<V>.metaAtomOrNull: MetaAtom<V>?
	get() =
		this as? MetaAtom

// === token stream

val <V> Atom<V>.token: Token<V>
  get() =
	  when (this) {
		  is MetaAtom -> meta.token
		  is WordAtom -> word.token()
	  }

// === appendable

fun <V> Appendable.append(atom: Atom<V>): Appendable =
	when (atom) {
		is MetaAtom -> append(atom.meta)
		is WordAtom -> append(atom.word).append(beginString).append(endString)
	}

// === reflect

val Atom<Nothing>.reflect: Field<Nothing>
	get() =
		reflectTerm { fail }

fun <V> Atom<V>.reflectTerm(valueReflect: V.() -> Term<Nothing>): Field<Nothing> =
	atomWord fieldTo reflectMeta(valueReflect)

fun <V> Atom<V>.reflect(valueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	atomWord fieldTo reflectMeta { valueReflect().onlyTerm }

fun <V> Atom<V>.reflectMeta(valueReflect: V.() -> Term<Nothing>): Term<Nothing> =
	when (this) {
		is MetaAtom -> meta.reflectMeta(valueReflect)
		is WordAtom -> word.term
	}

// === parse

val Field<Nothing>.parseAtom: Atom<Nothing>?
  get() =
	  matchKey(atomWord) {
		  matchWord {
			  atom
		  }
	  }
