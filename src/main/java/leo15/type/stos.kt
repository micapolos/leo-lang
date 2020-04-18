package leo15.type

object Empty : ThunkObject() {
	override val thunk: Thunk<TypeLine> = TODO()
}

data class Stos<T : ThunkObject>(val linkOrNull: Link<T>?) : ThunkObject() {
	override fun toString() = super.toString()
	override val thunk = TODO()
}

data class Link<T : ThunkObject>(val pop: Stos<T>, val top: T) : ThunkObject() {
	override fun toString() = super.toString()
	override val thunk = TODO()
}
