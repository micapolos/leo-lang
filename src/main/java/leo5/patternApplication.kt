package leo5

data class PatternApplication(val pattern: Pattern, val dictionary: PatternDictionary)

fun application(pattern: Pattern, dictionary: PatternDictionary) = PatternApplication(pattern, dictionary)

fun PatternApplication.contains(application: Application) =
	pattern.contains(application.value) && dictionary.contains(application.line)
