package leo15.type

import leo14.invoke
import leo14.leo
import leo15.*

val scriptTypeScript = leo(
	recursiveName(scriptName(
		repeatingName(lineName(
			choiceName(
				numberName(),
				textName(),
				fieldName(
					nameName(textName()),
					valueName(recurseName()))))))))

val scriptType = scriptTypeScript.type