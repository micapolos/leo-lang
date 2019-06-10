package leo5

data class PatternApplication(val pattern: Pattern, val dictionary: PatternDictionary)

fun application(pattern: Pattern, dictionary: PatternDictionary) = PatternApplication(pattern, dictionary)

fun PatternApplication.contains(application: Application) =
	pattern.contains(application.value.script) && dictionary.contains(application.line)


//fun PatternApplication.coreValue(application: Application): leo5.core.Value = when (this) {
//	is EmptyPattern -> leo5.core.value(empty)
//	is ApplicationPattern -> application.coreValue(script.applicationOrNull!!)
//}
