package leo32.runtime

import leo.base.Empty

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
	if (!isQuoted && symbol == quoteSymbol) copy(
		isQuoted = true,
		fieldReader = fieldReader.quote)
	else this to symbol to fieldReader.begin

val SymbolReader.end
	get() =
		if (isQuoted) copy(
			isQuoted = false,
			fieldReader = fieldReader.unquote)
		else symbolReaderParentOrNull?.let { symbolReaderParent ->
			symbolReaderParent
				.symbolReader
				.copy(fieldReader = symbolReaderParent
					.symbolReader
					.fieldReader
					.plus(symbolReaderParent.symbol to fieldReader.term))
		}

fun SymbolReader.plus(symbolOrNull: Symbol?): SymbolReader? =
	if (symbolOrNull != null) begin(symbolOrNull)
	else end