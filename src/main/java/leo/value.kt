package leo

object Value

val Value.reflect
	get() =
		valueWord fieldTo term(this)
