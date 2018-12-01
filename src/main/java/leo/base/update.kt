package leo.base

data class Update<R, U>(
	val result: R,
	val updated: U)

fun <R, U> R.andUpdated(updated: U): Update<R, U> =
	Update(this, updated)
