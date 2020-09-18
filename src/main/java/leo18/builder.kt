package leo18

data class Builder<in In, out Out, out Error>(val built: Out, val read: (In) -> Result<In, Out, Error>) {
	sealed class Result<in In, out Out, out Error> {
		data class Success<In, Out, Error>(val builder: Builder<In, Out, Error>) : Result<In, Out, Error>()
		data class Error<In, Out, Error>(val error: Error) : Result<In, Out, Error>()
	}
}
