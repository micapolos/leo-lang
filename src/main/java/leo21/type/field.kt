package leo21.type

data class Field(val name: String, val rhs: Type) : AsLine {
	override val asLine get() = line(this)
}

infix fun String.fieldTo(rhs: AsType) = Field(this, rhs.asType)
