package leo21.type

data class Field(val name: String, val rhs: Type) : TypeComponent {
	override val typeComponentLine get() = line(this)
}

infix fun String.fieldTo(rhs: Type) = Field(this, rhs)
