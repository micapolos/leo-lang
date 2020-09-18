package leo18

data class Parser<in In, out Out, out Error>(val read: (In) -> Result<In, Out, Error>) {
	sealed class Result<in In, out Out, out Error> {
		data class Partial<In, Out, Error>(val parser: Parser<In, Out, Error>) : Result<In, Out, Error>()
		data class Done<In, Out, Error>(val out: Out) : Result<In, Out, Error>()
		data class Error<In, Out, Error>(val error: Error) : Result<In, Out, Error>()
	}
}

data class Name(val string: String)
data class NotLetter(var char: Char)

fun nameBuilder(nameOrNull: Name? = null): Builder<Char, Name?, NotLetter> = Builder(nameOrNull) { char ->
	if (char.isLetter()) Builder.Result.Success(
		nameBuilder(
			if (nameOrNull == null) Name(char.toString())
			else Name(nameOrNull.string.plus(char))))
	else Builder.Result.Error(NotLetter(char))
}

val nameBuilder = nameBuilder()