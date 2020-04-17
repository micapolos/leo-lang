package leo15.type

import leo14.invoke
import leo14.leo
import leo15.*

val scriptType =
	leo(recursiveName(scriptName(
		repeatingName(lineName(
			choiceName(
				itName(numberName()),
				itName(textName()),
				itName(fieldName(
					nameName(textName()),
					valueName(recurseName())))))))))
