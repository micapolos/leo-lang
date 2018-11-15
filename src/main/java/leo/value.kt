package leo

object Value

val Value.reflect
	get() =
		valueWord fieldTo metaTerm(this)
