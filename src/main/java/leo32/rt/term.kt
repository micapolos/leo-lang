@file:JvmName("ValueKt")

package leo32.rt

import leo.base.Empty
import leo.base.Seq
import leo.base.seq
import leo32.base.I32

sealed class Term

data class EmptyTerm(val empty: Empty) : Term()

sealed class AtomTerm : Term()

sealed class NativeTerm : AtomTerm()
data class I32Term(val i32: I32) : NativeTerm()
data class SymbolTerm(val symbol: Symbol) : NativeTerm()
data class SwitchTerm(val switch: Switch) : NativeTerm()
data class EntryTerm(val entry: Entry) : Term()

data class FieldTerm(val field: Field) : AtomTerm()
data class VectorTerm(val vector: Vector) : Term()

data class ScriptTerm(val script: Script) : Term()

fun term(empty: Empty) = EmptyTerm(empty)
fun term(symbol: Symbol) = SymbolTerm(symbol)

fun Scope.field(atomTerm: AtomTerm): Field =
	when (atomTerm) {
		is SymbolTerm -> field(atomTerm.symbol)
		is I32Term -> field(atomTerm.i32)
		is SwitchTerm -> field(atomTerm.switch)
		is FieldTerm -> field(atomTerm.field)
	}

fun Scope.fieldSeq(term: Term): Seq<Field> =
	when (term) {
		is EmptyTerm -> fieldSeq(term.empty)
		is AtomTerm -> seq(field(term))
		is EntryTerm -> fieldSeq(term.entry)
		is VectorTerm -> fieldSeq(term.vector)
		is ScriptTerm -> fieldSeq(term.script)
	}

fun Scope.at(term: Term, symbol: Symbol): Value? =
	when (term) {
		is EmptyTerm -> at(term.empty, symbol)
		is SymbolTerm -> at(term.symbol, symbol)
		is I32Term -> at(term.i32, symbol)
		is EntryTerm -> at(term.entry, symbol)
		is SwitchTerm -> at(term.switch, symbol)
		is FieldTerm -> at(term.field, symbol)
		is VectorTerm -> at(term.vector, symbol)
		is ScriptTerm -> at(term.script, symbol)
	}
