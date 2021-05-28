package leo25

import leo13.StackLink
import leo13.linkOrNull
import leo13.reverse
import leo13.stackLink
import leo14.Script
import leo14.literal
import leo14.nameStackOrNull
import leo25.natives.loadString
import leo25.natives.path
import leo25.parser.scriptOrThrow

data class Use(val nameStackLink: StackLink<String>)

fun use(name: String, vararg names: String) = Use(stackLink(name, *names))

val Script.useOrNull: Use?
	get() =
		nameStackOrNull?.reverse?.linkOrNull?.let { nameStackLink ->
			Use(nameStackLink)
		}

val Use.dictionary: Dictionary
	get() =
		try {
			loadString.scriptOrThrow
		} catch (valueError: ValueError) {
			value(
				"path" fieldTo value(field(literal(path.toString()))),
				"location" fieldTo valueError.value
			).throwError<Script>()
		} catch (ioException: Exception) {
			value(field(literal(ioException.message ?: ioException.toString()))).throwError<Script>()
		}.dictionary
