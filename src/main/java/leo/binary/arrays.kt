package leo.binary

data class Array1<out T>(val at0: T, val at1: T)
data class Array2<out T>(val at0: Array1<T>, val at1: Array1<T>)
data class Array3<out T>(val at0: Array2<T>, val at1: Array2<T>)
data class Array4<out T>(val at0: Array3<T>, val at1: Array3<T>)
data class Array5<out T>(val at0: Array4<T>, val at1: Array4<T>)

fun <T> Array1<T>.at(index: Int1) = if (index.lo == Bit.ZERO) at0 else at1
fun <T> Array2<T>.at(index: Int2) = if (index.lo == Bit.ZERO) at0.at(index.hi) else at1.at(index.hi)
fun <T> Array3<T>.at(index: Int3) = if (index.lo == Bit.ZERO) at0.at(index.hi) else at1.at(index.hi)
fun <T> Array4<T>.at(index: Int4) = if (index.lo == Bit.ZERO) at0.at(index.hi) else at1.at(index.hi)
fun <T> Array5<T>.at(index: Int5) = if (index.lo == Bit.ZERO) at0.at(index.hi) else at1.at(index.hi)

fun <T> Array1<T>.put(index: Int1, value: T) =
	if (index.lo == Bit.ZERO) copy(at0 = value) else copy(at1 = value)

fun <T> Array2<T>.put(index: Int2, value: T) =
	if (index.lo == Bit.ZERO) copy(at0 = at0.put(index.hi, value)) else copy(at1 = at1.put(index.hi, value))

fun <T> Array3<T>.put(index: Int3, value: T) =
	if (index.lo == Bit.ZERO) copy(at0 = at0.put(index.hi, value)) else copy(at1 = at1.put(index.hi, value))

fun <T> Array4<T>.put(index: Int4, value: T) =
	if (index.lo == Bit.ZERO) copy(at0 = at0.put(index.hi, value)) else copy(at1 = at1.put(index.hi, value))

fun <T> Array5<T>.put(index: Int5, value: T) =
	if (index.lo == Bit.ZERO) copy(at0 = at0.put(index.hi, value)) else copy(at1 = at1.put(index.hi, value))
