package leo25.natives

import leo14.Number
import leo14.minus
import leo14.plus
import leo14.times
import leo25.*

val textAppendTextDefinition
	get() =
		nativeDefinition(
			textName, { nativeText },
			appendName,
			textName, { nativeText },
			{ nativeValue }, String::plus
		)

val textLengthDefinition
	get() =
		nativeDefinition(
			textName, { nativeText },
			lengthName, { nativeValue },
			lengthName, String::lengthNumber
		)

val numberPlusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			plusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::plus
		)

val numberMinusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			minusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::minus
		)

val numberTimesNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			timesName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::times
		)

