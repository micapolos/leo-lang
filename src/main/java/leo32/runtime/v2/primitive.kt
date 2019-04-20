package leo32.runtime.v2

import leo32.runtime.Symbol

sealed class Primitive
data class IntPrimitive(val int: Int) : Primitive()
data class StringPrimitive(val string: String) : Primitive()
data class SymbolPrimitive(val symbol: Symbol) : Primitive()
data class FieldPrimitive(val field: Field) : Primitive()

fun primitive(int: Int) =
	IntPrimitive(int) as Primitive

fun primitive(string: String) =
	StringPrimitive(string) as Primitive

fun primitive(symbol: Symbol) =
	SymbolPrimitive(symbol) as Primitive

fun primitive(field: Field) =
	FieldPrimitive(field) as Primitive

