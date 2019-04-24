package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.notNullIf
import leo.base.runIf

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

fun SymbolReader.begin(symbol: Symbol) =
	this
		.fieldReaderBegin(symbol)
		.applyQuote(symbol)
		.applyShortQuote(symbol)

val quotedSymbolSet = empty
	.symbolSet
	.add(quoteSymbol)
	.add(testSymbol)

fun SymbolReader.applyQuote(symbol: Symbol) =
	runIf(quotedSymbolSet.contains(symbol)) {
		copy(fieldReader = fieldReader.quote)
	}

fun SymbolReader.applyShortQuote(symbol: Symbol) =
	runIf(symbol == withSymbol) {
		copy(fieldReader = fieldReader.shortQuote)
	}

fun SymbolReader.plus(symbolOrNull: Symbol?): SymbolReader? =
	if (symbolOrNull != null) begin(symbolOrNull)
	else end

fun SymbolReader.fieldReaderBegin(symbol: Symbol) =
	this to symbol to fieldReader.begin

val SymbolReader.end
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
		notNullIf(symbolReaderParentOrNull == null) {
			fieldReader.term
		}
