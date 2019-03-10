package leo.binary

import leo.base.appendableString
import leo.base.ifNotNull
import leo.base.indexed
import leo.base.or1Shl

data class Map1<out T>(val at0: T? = null, val at1: T? = null)
data class Map2<out T>(val at0: Map1<T>? = null, val at1: Map1<T>? = null)
data class Map3<out T>(val at0: Map2<T>? = null, val at1: Map2<T>? = null)
data class Map4<out T>(val at0: Map3<T>? = null, val at1: Map3<T>? = null)
data class Map5<out T>(val at0: Map4<T>? = null, val at1: Map4<T>? = null)
data class Map6<out T>(val at0: Map5<T>? = null, val at1: Map5<T>? = null)
data class Map7<out T>(val at0: Map6<T>? = null, val at1: Map6<T>? = null)
data class Map8<out T>(val at0: Map7<T>? = null, val at1: Map7<T>? = null)
data class Map9<out T>(val at0: Map8<T>? = null, val at1: Map8<T>? = null)
data class Map10<out T>(val at0: Map9<T>? = null, val at1: Map9<T>? = null)
data class Map11<out T>(val at0: Map10<T>? = null, val at1: Map10<T>? = null)
data class Map12<out T>(val at0: Map11<T>? = null, val at1: Map11<T>? = null)
data class Map13<out T>(val at0: Map12<T>? = null, val at1: Map12<T>? = null)
data class Map14<out T>(val at0: Map13<T>? = null, val at1: Map13<T>? = null)
data class Map15<out T>(val at0: Map14<T>? = null, val at1: Map14<T>? = null)
data class Map16<out T>(val at0: Map15<T>? = null, val at1: Map15<T>? = null)
data class Map17<out T>(val at0: Map16<T>? = null, val at1: Map16<T>? = null)
data class Map18<out T>(val at0: Map17<T>? = null, val at1: Map17<T>? = null)
data class Map19<out T>(val at0: Map18<T>? = null, val at1: Map18<T>? = null)
data class Map20<out T>(val at0: Map19<T>? = null, val at1: Map19<T>? = null)
data class Map21<out T>(val at0: Map20<T>? = null, val at1: Map20<T>? = null)
data class Map22<out T>(val at0: Map21<T>? = null, val at1: Map21<T>? = null)
data class Map23<out T>(val at0: Map22<T>? = null, val at1: Map22<T>? = null)
data class Map24<out T>(val at0: Map23<T>? = null, val at1: Map23<T>? = null)
data class Map25<out T>(val at0: Map24<T>? = null, val at1: Map24<T>? = null)
data class Map26<out T>(val at0: Map25<T>? = null, val at1: Map25<T>? = null)
data class Map27<out T>(val at0: Map26<T>? = null, val at1: Map26<T>? = null)
data class Map28<out T>(val at0: Map27<T>? = null, val at1: Map27<T>? = null)
data class Map29<out T>(val at0: Map28<T>? = null, val at1: Map28<T>? = null)
data class Map30<out T>(val at0: Map29<T>? = null, val at1: Map29<T>? = null)
data class Map31<out T>(val at0: Map30<T>? = null, val at1: Map30<T>? = null)
data class Map32<out T>(val at0: Map31<T>? = null, val at1: Map31<T>? = null) {
	override fun toString() = appendableString { it.append(this) }
}

fun <T> nullMap1() = Map1<T>()
fun <T> nullMap2() = Map2<T>()
fun <T> nullMap3() = Map3<T>()
fun <T> nullMap4() = Map4<T>()
fun <T> nullMap5() = Map5<T>()
fun <T> nullMap6() = Map6<T>()
fun <T> nullMap7() = Map7<T>()
fun <T> nullMap8() = Map8<T>()
fun <T> nullMap9() = Map9<T>()
fun <T> nullMap10() = Map10<T>()
fun <T> nullMap11() = Map11<T>()
fun <T> nullMap12() = Map12<T>()
fun <T> nullMap13() = Map13<T>()
fun <T> nullMap14() = Map14<T>()
fun <T> nullMap15() = Map15<T>()
fun <T> nullMap16() = Map16<T>()
fun <T> nullMap17() = Map17<T>()
fun <T> nullMap18() = Map18<T>()
fun <T> nullMap19() = Map19<T>()
fun <T> nullMap20() = Map20<T>()
fun <T> nullMap21() = Map21<T>()
fun <T> nullMap22() = Map22<T>()
fun <T> nullMap23() = Map23<T>()
fun <T> nullMap24() = Map24<T>()
fun <T> nullMap25() = Map25<T>()
fun <T> nullMap26() = Map26<T>()
fun <T> nullMap27() = Map27<T>()
fun <T> nullMap28() = Map28<T>()
fun <T> nullMap29() = Map29<T>()
fun <T> nullMap30() = Map30<T>()
fun <T> nullMap31() = Map31<T>()
fun <T> nullMap32() = Map32<T>()

fun <T> Map1<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map2<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map3<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map4<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map5<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map6<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map7<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map8<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map9<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map10<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map11<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map12<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map13<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map14<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map15<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map16<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map17<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map18<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map19<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map20<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map21<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map22<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map23<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map24<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map25<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map26<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map27<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map28<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map29<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map30<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map31<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Map32<T>.at(bit: Bit) = if (bit.isZero) at0 else at1

fun <T> Map1<T>.put(bit: Bit, value: T?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map2<T>.put(bit: Bit, value: Map1<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map3<T>.put(bit: Bit, value: Map2<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map4<T>.put(bit: Bit, value: Map3<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map5<T>.put(bit: Bit, value: Map4<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map6<T>.put(bit: Bit, value: Map5<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map7<T>.put(bit: Bit, value: Map6<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map8<T>.put(bit: Bit, value: Map7<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map9<T>.put(bit: Bit, value: Map8<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map10<T>.put(bit: Bit, value: Map9<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map11<T>.put(bit: Bit, value: Map10<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map12<T>.put(bit: Bit, value: Map11<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map13<T>.put(bit: Bit, value: Map12<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map14<T>.put(bit: Bit, value: Map13<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map15<T>.put(bit: Bit, value: Map14<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map16<T>.put(bit: Bit, value: Map15<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map17<T>.put(bit: Bit, value: Map16<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map18<T>.put(bit: Bit, value: Map17<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map19<T>.put(bit: Bit, value: Map18<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map20<T>.put(bit: Bit, value: Map19<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map21<T>.put(bit: Bit, value: Map20<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map22<T>.put(bit: Bit, value: Map21<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map23<T>.put(bit: Bit, value: Map22<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map24<T>.put(bit: Bit, value: Map23<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map25<T>.put(bit: Bit, value: Map24<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map26<T>.put(bit: Bit, value: Map25<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map27<T>.put(bit: Bit, value: Map26<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map28<T>.put(bit: Bit, value: Map27<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map29<T>.put(bit: Bit, value: Map28<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map30<T>.put(bit: Bit, value: Map29<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map31<T>.put(bit: Bit, value: Map30<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Map32<T>.put(bit: Bit, value: Map31<T>?) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)

fun <T> Map32<T>.at(index: Int): T? = this
	.at(index.and(1 shl 31).bit)
	?.at(index.and(1 shl 30).bit)
	?.at(index.and(1 shl 29).bit)
	?.at(index.and(1 shl 28).bit)
	?.at(index.and(1 shl 27).bit)
	?.at(index.and(1 shl 26).bit)
	?.at(index.and(1 shl 25).bit)
	?.at(index.and(1 shl 24).bit)
	?.at(index.and(1 shl 23).bit)
	?.at(index.and(1 shl 22).bit)
	?.at(index.and(1 shl 21).bit)
	?.at(index.and(1 shl 20).bit)
	?.at(index.and(1 shl 19).bit)
	?.at(index.and(1 shl 18).bit)
	?.at(index.and(1 shl 17).bit)
	?.at(index.and(1 shl 16).bit)
	?.at(index.and(1 shl 15).bit)
	?.at(index.and(1 shl 14).bit)
	?.at(index.and(1 shl 13).bit)
	?.at(index.and(1 shl 12).bit)
	?.at(index.and(1 shl 11).bit)
	?.at(index.and(1 shl 10).bit)
	?.at(index.and(1 shl 9).bit)
	?.at(index.and(1 shl 8).bit)
	?.at(index.and(1 shl 7).bit)
	?.at(index.and(1 shl 6).bit)
	?.at(index.and(1 shl 5).bit)
	?.at(index.and(1 shl 4).bit)
	?.at(index.and(1 shl 3).bit)
	?.at(index.and(1 shl 2).bit)
	?.at(index.and(1 shl 1).bit)
	?.at(index.and(1).bit)


fun <T> Map32<T>.put(index: Int, value: T?): Map32<T> {
	val int = index
	val bit31 = int.and(1 shl 31).bit
	val bit30 = int.and(1 shl 30).bit
	val bit29 = int.and(1 shl 29).bit
	val bit28 = int.and(1 shl 28).bit
	val bit27 = int.and(1 shl 27).bit
	val bit26 = int.and(1 shl 26).bit
	val bit25 = int.and(1 shl 25).bit
	val bit24 = int.and(1 shl 24).bit
	val bit23 = int.and(1 shl 23).bit
	val bit22 = int.and(1 shl 22).bit
	val bit21 = int.and(1 shl 21).bit
	val bit20 = int.and(1 shl 20).bit
	val bit19 = int.and(1 shl 19).bit
	val bit18 = int.and(1 shl 18).bit
	val bit17 = int.and(1 shl 17).bit
	val bit16 = int.and(1 shl 16).bit
	val bit15 = int.and(1 shl 15).bit
	val bit14 = int.and(1 shl 14).bit
	val bit13 = int.and(1 shl 13).bit
	val bit12 = int.and(1 shl 12).bit
	val bit11 = int.and(1 shl 11).bit
	val bit10 = int.and(1 shl 10).bit
	val bit9 = int.and(1 shl 9).bit
	val bit8 = int.and(1 shl 8).bit
	val bit7 = int.and(1 shl 7).bit
	val bit6 = int.and(1 shl 6).bit
	val bit5 = int.and(1 shl 5).bit
	val bit4 = int.and(1 shl 4).bit
	val bit3 = int.and(1 shl 3).bit
	val bit2 = int.and(1 shl 2).bit
	val bit1 = int.and(1 shl 1).bit
	val bit0 = int.and(1).bit

	val array31 = at(bit31) ?: nullMap31()
	val array30 = array31.at(bit30) ?: nullMap30()
	val array29 = array30.at(bit29) ?: nullMap29()
	val array28 = array29.at(bit28) ?: nullMap28()
	val array27 = array28.at(bit27) ?: nullMap27()
	val array26 = array27.at(bit26) ?: nullMap26()
	val array25 = array26.at(bit25) ?: nullMap25()
	val array24 = array25.at(bit24) ?: nullMap24()
	val array23 = array24.at(bit23) ?: nullMap23()
	val array22 = array23.at(bit22) ?: nullMap22()
	val array21 = array22.at(bit21) ?: nullMap21()
	val array20 = array21.at(bit20) ?: nullMap20()
	val array19 = array20.at(bit19) ?: nullMap19()
	val array18 = array19.at(bit18) ?: nullMap18()
	val array17 = array18.at(bit17) ?: nullMap17()
	val array16 = array17.at(bit16) ?: nullMap16()
	val array15 = array16.at(bit15) ?: nullMap15()
	val array14 = array15.at(bit14) ?: nullMap14()
	val array13 = array14.at(bit13) ?: nullMap13()
	val array12 = array13.at(bit12) ?: nullMap12()
	val array11 = array12.at(bit11) ?: nullMap11()
	val array10 = array11.at(bit10) ?: nullMap10()
	val array9 = array10.at(bit9) ?: nullMap9()
	val array8 = array9.at(bit8) ?: nullMap8()
	val array7 = array8.at(bit7) ?: nullMap7()
	val array6 = array7.at(bit6) ?: nullMap6()
	val array5 = array6.at(bit5) ?: nullMap5()
	val array4 = array5.at(bit4) ?: nullMap4()
	val array3 = array4.at(bit3) ?: nullMap3()
	val array2 = array3.at(bit2) ?: nullMap2()
	val array1 = array2.at(bit1) ?: nullMap1()

	val newMap1 = array1.put(bit0, value)
	val newMap2 = array2.put(bit1, newMap1)
	val newMap3 = array3.put(bit2, newMap2)
	val newMap4 = array4.put(bit3, newMap3)
	val newMap5 = array5.put(bit4, newMap4)
	val newMap6 = array6.put(bit5, newMap5)
	val newMap7 = array7.put(bit6, newMap6)
	val newMap8 = array8.put(bit7, newMap7)
	val newMap9 = array9.put(bit8, newMap8)
	val newMap10 = array10.put(bit9, newMap9)
	val newMap11 = array11.put(bit10, newMap10)
	val newMap12 = array12.put(bit11, newMap11)
	val newMap13 = array13.put(bit12, newMap12)
	val newMap14 = array14.put(bit13, newMap13)
	val newMap15 = array15.put(bit14, newMap14)
	val newMap16 = array16.put(bit15, newMap15)
	val newMap17 = array17.put(bit16, newMap16)
	val newMap18 = array18.put(bit17, newMap17)
	val newMap19 = array19.put(bit18, newMap18)
	val newMap20 = array20.put(bit19, newMap19)
	val newMap21 = array21.put(bit20, newMap20)
	val newMap22 = array22.put(bit21, newMap21)
	val newMap23 = array23.put(bit22, newMap22)
	val newMap24 = array24.put(bit23, newMap23)
	val newMap25 = array25.put(bit24, newMap24)
	val newMap26 = array26.put(bit25, newMap25)
	val newMap27 = array27.put(bit26, newMap26)
	val newMap28 = array28.put(bit27, newMap27)
	val newMap29 = array29.put(bit28, newMap28)
	val newMap30 = array30.put(bit29, newMap29)
	val newMap31 = array31.put(bit30, newMap30)
	return put(bit31, newMap31)
}

fun <T> Map1<T>.updateAt(bit: Bit, fn: T?.() -> T?): Map1<T> =
	put(bit, at(bit).fn())

fun <T> Map32<T>.updateAt(index: Int, fn: T?.() -> T?): Map32<T> =
	put(index, at(index).fn())

fun <T, R> R.foldIndexed(array: Map1<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { fn(0 indexed it) }
		.ifNotNull(array.at1) { fn(1 indexed it) }

fun <T, R> R.foldIndexed(array: Map2<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 1) } }

fun <T, R> R.foldIndexed(array: Map3<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 2) } }

fun <T, R> R.foldIndexed(array: Map4<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 3) } }

fun <T, R> R.foldIndexed(array: Map5<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 4) } }

fun <T, R> R.foldIndexed(array: Map6<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 5) } }

fun <T, R> R.foldIndexed(array: Map7<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 6) } }

fun <T, R> R.foldIndexed(array: Map8<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 7) } }

fun <T, R> R.foldIndexed(array: Map9<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 8) } }

fun <T, R> R.foldIndexed(array: Map10<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 9) } }

fun <T, R> R.foldIndexed(array: Map11<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 10) } }

fun <T, R> R.foldIndexed(array: Map12<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 11) } }

fun <T, R> R.foldIndexed(array: Map13<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 12) } }

fun <T, R> R.foldIndexed(array: Map14<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 13) } }

fun <T, R> R.foldIndexed(array: Map15<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 14) } }

fun <T, R> R.foldIndexed(array: Map16<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 15) } }

fun <T, R> R.foldIndexed(array: Map17<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 16) } }

fun <T, R> R.foldIndexed(array: Map18<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 17) } }

fun <T, R> R.foldIndexed(array: Map19<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 18) } }

fun <T, R> R.foldIndexed(array: Map20<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 19) } }

fun <T, R> R.foldIndexed(array: Map21<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 20) } }

fun <T, R> R.foldIndexed(array: Map22<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 21) } }

fun <T, R> R.foldIndexed(array: Map23<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 22) } }

fun <T, R> R.foldIndexed(array: Map24<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 23) } }

fun <T, R> R.foldIndexed(array: Map25<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 24) } }

fun <T, R> R.foldIndexed(array: Map26<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 25) } }

fun <T, R> R.foldIndexed(array: Map27<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 26) } }

fun <T, R> R.foldIndexed(array: Map28<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 27) } }

fun <T, R> R.foldIndexed(array: Map29<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 28) } }

fun <T, R> R.foldIndexed(array: Map30<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 29) } }

fun <T, R> R.foldIndexed(array: Map31<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 30) } }

fun <T, R> R.foldIndexed(array: Map32<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(array.at0) { foldIndexed(it, fn) }
		.ifNotNull(array.at1) { foldIndexed(it) { indexed -> fn(indexed or1Shl 31) } }

fun <T> Appendable.append(array: Map32<T>): Appendable =
	append("nullMap32()").appendIndexed(array)

fun <T> Appendable.appendIndexed(array: Map32<T>): Appendable =
	foldIndexed(array) { append(".put(${it.index}, ${it.value})") }