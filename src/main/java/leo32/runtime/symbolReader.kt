package leo32.runtime

import leo.base.Empty

data class SymbolReader(
	val symbolReaderParentOrNull: SymbolReaderParent?,
	val fieldReader: FieldReader)

data class SymbolReaderParent(
	val symbolReader: SymbolReader,
	val symbol: Symbol)

val FieldReader.symbolReader
	get() =
		SymbolReader(null, this)

val Empty.symbolReader
	get() =
		fieldReader.symbolReader

infix fun SymbolReader.to(symbol: Symbol) =
	SymbolReaderParent(this, symbol)

infix fun SymbolReaderParent.to(fieldReader: FieldReader) =
	SymbolReader(this, fieldReader)

// TODO: Resolve quoting here, and possibly more
fun SymbolReader.begin(symbol: Symbol) =
	this to symbol to fieldReader.term.begin.fieldReader

val SymbolReader.end
	get() =
		symbolReaderParentOrNull?.let { symbolReaderParent ->
			symbolReaderParent
				.symbolReader
				.copy(fieldReader =
				symbolReaderParent
					.symbolReader
					.fieldReader
					.plus(symbolReaderParent.symbol to fieldReader.term))
		}

fun SymbolReader.plus(symbolOrNull: Symbol?): SymbolReader? =
	if (symbolOrNull != null) begin(symbolOrNull)
	else end