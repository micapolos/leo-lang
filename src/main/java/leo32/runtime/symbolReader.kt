package leo32.runtime

import leo.base.Empty
import leo.base.notNullIf

data class SymbolReader(
	val symbolReaderParentOrNull: SymbolReaderParent?,
	val fieldReader: FieldReader,
	val isQuoted: Boolean)

data class SymbolReaderParent(
	val symbolReader: SymbolReader,
	val symbol: Symbol)

val FieldReader.symbolReader
	get() =
		SymbolReader(null, this, false)

val Empty.symbolReader
	get() =
		fieldReader.symbolReader

infix fun SymbolReader.to(symbol: Symbol) =
	SymbolReaderParent(this, symbol)

infix fun SymbolReaderParent.to(fieldReader: FieldReader) =
	SymbolReader(this, fieldReader, false)

// TODO: Resolve quoting here, and possibly more
fun SymbolReader.begin(symbol: Symbol) =
	if (!isQuoted && symbol == quoteSymbol) quote
	else beginDefault(symbol)

val SymbolReader.end
	get() =
		if (isQuoted) unquote
		else endDefault

fun SymbolReader.plus(symbolOrNull: Symbol?): SymbolReader? =
	if (symbolOrNull != null) begin(symbolOrNull)
	else end

val SymbolReader.quote
	get() =
		copy(isQuoted = true, fieldReader = fieldReader.quote)

fun SymbolReader.beginDefault(symbol: Symbol) =
	this to symbol to fieldReader.begin

val SymbolReader.unquote
	get() =
		copy(isQuoted = false, fieldReader = fieldReader.unquote)

val SymbolReader.endDefault
	get() =
		symbolReaderParentOrNull?.let { symbolReaderParent ->
			symbolReaderParent
				.symbolReader
				.copy(fieldReader = symbolReaderParent
					.symbolReader
					.fieldReader
					.plus(symbolReaderParent.symbol to fieldReader.term))
		}

val SymbolReader.termOrNull
	get() =
		notNullIf(symbolReaderParentOrNull == null && !isQuoted) {
			fieldReader.term
		}
