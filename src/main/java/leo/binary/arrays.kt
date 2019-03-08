package leo.binary

import leo.base.int

data class Array1<out T>(val at0: T, val at1: T)
data class Array2<out T>(val at0: Array1<T>, val at1: Array1<T>)
data class Array3<out T>(val at0: Array2<T>, val at1: Array2<T>)
data class Array4<out T>(val at0: Array3<T>, val at1: Array3<T>)
data class Array5<out T>(val at0: Array4<T>, val at1: Array4<T>)
data class Array6<out T>(val at0: Array5<T>, val at1: Array5<T>)
data class Array7<out T>(val at0: Array6<T>, val at1: Array6<T>)
data class Array8<out T>(val at0: Array7<T>, val at1: Array7<T>)
data class Array9<out T>(val at0: Array8<T>, val at1: Array8<T>)
data class Array10<out T>(val at0: Array9<T>, val at1: Array9<T>)
data class Array11<out T>(val at0: Array10<T>, val at1: Array10<T>)
data class Array12<out T>(val at0: Array11<T>, val at1: Array11<T>)
data class Array13<out T>(val at0: Array12<T>, val at1: Array12<T>)
data class Array14<out T>(val at0: Array13<T>, val at1: Array13<T>)
data class Array15<out T>(val at0: Array14<T>, val at1: Array14<T>)
data class Array16<out T>(val at0: Array15<T>, val at1: Array15<T>)
data class Array17<out T>(val at0: Array16<T>, val at1: Array16<T>)
data class Array18<out T>(val at0: Array17<T>, val at1: Array17<T>)
data class Array19<out T>(val at0: Array18<T>, val at1: Array18<T>)
data class Array20<out T>(val at0: Array19<T>, val at1: Array19<T>)
data class Array21<out T>(val at0: Array20<T>, val at1: Array20<T>)
data class Array22<out T>(val at0: Array21<T>, val at1: Array21<T>)
data class Array23<out T>(val at0: Array22<T>, val at1: Array22<T>)
data class Array24<out T>(val at0: Array23<T>, val at1: Array23<T>)
data class Array25<out T>(val at0: Array24<T>, val at1: Array24<T>)
data class Array26<out T>(val at0: Array25<T>, val at1: Array25<T>)
data class Array27<out T>(val at0: Array26<T>, val at1: Array26<T>)
data class Array28<out T>(val at0: Array27<T>, val at1: Array27<T>)
data class Array29<out T>(val at0: Array28<T>, val at1: Array28<T>)
data class Array30<out T>(val at0: Array29<T>, val at1: Array29<T>)
data class Array31<out T>(val at0: Array30<T>, val at1: Array30<T>)
data class Array32<out T>(val at0: Array31<T>, val at1: Array31<T>)

fun <T> Array1<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array2<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array3<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array4<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array5<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array6<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array7<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array8<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array9<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array10<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array11<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array12<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array13<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array14<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array15<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array16<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array17<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array18<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array19<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array20<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array21<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array22<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array23<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array24<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array25<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array26<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array27<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array28<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array29<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array30<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array31<T>.at(bit: Bit) = if (bit.isZero) at0 else at1
fun <T> Array32<T>.at(bit: Bit) = if (bit.isZero) at0 else at1

fun <T> Array1<T>.put(bit: Bit, value: T) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array2<T>.put(bit: Bit, value: Array1<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array3<T>.put(bit: Bit, value: Array2<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array4<T>.put(bit: Bit, value: Array3<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array5<T>.put(bit: Bit, value: Array4<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array6<T>.put(bit: Bit, value: Array5<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array7<T>.put(bit: Bit, value: Array6<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array8<T>.put(bit: Bit, value: Array7<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array9<T>.put(bit: Bit, value: Array8<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array10<T>.put(bit: Bit, value: Array9<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array11<T>.put(bit: Bit, value: Array10<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array12<T>.put(bit: Bit, value: Array11<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array13<T>.put(bit: Bit, value: Array12<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array14<T>.put(bit: Bit, value: Array13<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array15<T>.put(bit: Bit, value: Array14<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array16<T>.put(bit: Bit, value: Array15<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array17<T>.put(bit: Bit, value: Array16<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array18<T>.put(bit: Bit, value: Array17<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array19<T>.put(bit: Bit, value: Array18<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array20<T>.put(bit: Bit, value: Array19<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array21<T>.put(bit: Bit, value: Array20<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array22<T>.put(bit: Bit, value: Array21<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array23<T>.put(bit: Bit, value: Array22<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array24<T>.put(bit: Bit, value: Array23<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array25<T>.put(bit: Bit, value: Array24<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array26<T>.put(bit: Bit, value: Array25<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array27<T>.put(bit: Bit, value: Array26<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array28<T>.put(bit: Bit, value: Array27<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array29<T>.put(bit: Bit, value: Array28<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array30<T>.put(bit: Bit, value: Array29<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array31<T>.put(bit: Bit, value: Array30<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)
fun <T> Array32<T>.put(bit: Bit, value: Array31<T>) = if (bit.isZero) copy(at0 = value) else copy(at1 = value)

fun <T> Array8<T>.at(index: Byte): T = this
	.at(index.int.and(1 shl 7).bit)
	.at(index.int.and(1 shl 6).bit)
	.at(index.int.and(1 shl 5).bit)
	.at(index.int.and(1 shl 4).bit)
	.at(index.int.and(1 shl 3).bit)
	.at(index.int.and(1 shl 2).bit)
	.at(index.int.and(1 shl 1).bit)
	.at(index.int.and(1).bit)

fun <T> Array32<T>.at(index: Int): T = this
	.at(index.and(1 shl 31).bit)
	.at(index.and(1 shl 30).bit)
	.at(index.and(1 shl 29).bit)
	.at(index.and(1 shl 28).bit)
	.at(index.and(1 shl 27).bit)
	.at(index.and(1 shl 26).bit)
	.at(index.and(1 shl 25).bit)
	.at(index.and(1 shl 24).bit)
	.at(index.and(1 shl 23).bit)
	.at(index.and(1 shl 22).bit)
	.at(index.and(1 shl 21).bit)
	.at(index.and(1 shl 20).bit)
	.at(index.and(1 shl 19).bit)
	.at(index.and(1 shl 18).bit)
	.at(index.and(1 shl 17).bit)
	.at(index.and(1 shl 16).bit)
	.at(index.and(1 shl 15).bit)
	.at(index.and(1 shl 14).bit)
	.at(index.and(1 shl 13).bit)
	.at(index.and(1 shl 12).bit)
	.at(index.and(1 shl 11).bit)
	.at(index.and(1 shl 10).bit)
	.at(index.and(1 shl 9).bit)
	.at(index.and(1 shl 8).bit)
	.at(index.and(1 shl 7).bit)
	.at(index.and(1 shl 6).bit)
	.at(index.and(1 shl 5).bit)
	.at(index.and(1 shl 4).bit)
	.at(index.and(1 shl 3).bit)
	.at(index.and(1 shl 2).bit)
	.at(index.and(1 shl 1).bit)
	.at(index.and(1).bit)

fun <T> Array8<T>.put(index: Byte, value: T): Array8<T> {
	val int = index.int
	val bit7 = int.and(1 shl 7).bit
	val bit6 = int.and(1 shl 6).bit
	val bit5 = int.and(1 shl 5).bit
	val bit4 = int.and(1 shl 4).bit
	val bit3 = int.and(1 shl 3).bit
	val bit2 = int.and(1 shl 2).bit
	val bit1 = int.and(1 shl 1).bit
	val bit0 = int.and(1).bit

	val array7 = at(bit7)
	val array6 = array7.at(bit6)
	val array5 = array6.at(bit5)
	val array4 = array5.at(bit4)
	val array3 = array4.at(bit3)
	val array2 = array3.at(bit2)
	val array1 = array2.at(bit1)

	val newArray1 = array1.put(bit0, value)
	val newArray2 = array2.put(bit1, newArray1)
	val newArray3 = array3.put(bit2, newArray2)
	val newArray4 = array4.put(bit3, newArray3)
	val newArray5 = array5.put(bit4, newArray4)
	val newArray6 = array6.put(bit5, newArray5)
	val newArray7 = array7.put(bit6, newArray6)
	return put(bit7, newArray7)
}

fun <T> Array32<T>.put(index: Int, value: T): Array32<T> {
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

	val array31 = at(bit31)
	val array30 = array31.at(bit30)
	val array29 = array30.at(bit29)
	val array28 = array29.at(bit28)
	val array27 = array28.at(bit27)
	val array26 = array27.at(bit26)
	val array25 = array26.at(bit25)
	val array24 = array25.at(bit24)
	val array23 = array24.at(bit23)
	val array22 = array23.at(bit22)
	val array21 = array22.at(bit21)
	val array20 = array21.at(bit20)
	val array19 = array20.at(bit19)
	val array18 = array19.at(bit18)
	val array17 = array18.at(bit17)
	val array16 = array17.at(bit16)
	val array15 = array16.at(bit15)
	val array14 = array15.at(bit14)
	val array13 = array14.at(bit13)
	val array12 = array13.at(bit12)
	val array11 = array12.at(bit11)
	val array10 = array11.at(bit10)
	val array9 = array10.at(bit9)
	val array8 = array9.at(bit8)
	val array7 = array8.at(bit7)
	val array6 = array7.at(bit6)
	val array5 = array6.at(bit5)
	val array4 = array5.at(bit4)
	val array3 = array4.at(bit3)
	val array2 = array3.at(bit2)
	val array1 = array2.at(bit1)

	val newArray1 = array1.put(bit0, value)
	val newArray2 = array2.put(bit1, newArray1)
	val newArray3 = array3.put(bit2, newArray2)
	val newArray4 = array4.put(bit3, newArray3)
	val newArray5 = array5.put(bit4, newArray4)
	val newArray6 = array6.put(bit5, newArray5)
	val newArray7 = array7.put(bit6, newArray6)
	val newArray8 = array8.put(bit7, newArray7)
	val newArray9 = array9.put(bit8, newArray8)
	val newArray10 = array10.put(bit9, newArray9)
	val newArray11 = array11.put(bit10, newArray10)
	val newArray12 = array12.put(bit11, newArray11)
	val newArray13 = array13.put(bit12, newArray12)
	val newArray14 = array14.put(bit13, newArray13)
	val newArray15 = array15.put(bit14, newArray14)
	val newArray16 = array16.put(bit15, newArray15)
	val newArray17 = array17.put(bit16, newArray16)
	val newArray18 = array18.put(bit17, newArray17)
	val newArray19 = array19.put(bit18, newArray18)
	val newArray20 = array20.put(bit19, newArray19)
	val newArray21 = array21.put(bit20, newArray20)
	val newArray22 = array22.put(bit21, newArray21)
	val newArray23 = array23.put(bit22, newArray22)
	val newArray24 = array24.put(bit23, newArray23)
	val newArray25 = array25.put(bit24, newArray24)
	val newArray26 = array26.put(bit25, newArray25)
	val newArray27 = array27.put(bit26, newArray26)
	val newArray28 = array28.put(bit27, newArray27)
	val newArray29 = array29.put(bit28, newArray28)
	val newArray30 = array30.put(bit29, newArray29)
	val newArray31 = array31.put(bit30, newArray30)
	return put(bit31, newArray31)
}

val zeroBitArray1 = Array1(zeroBit, zeroBit)
val zeroBitArray2 = Array2(zeroBitArray1, zeroBitArray1)
val zeroBitArray3 = Array3(zeroBitArray2, zeroBitArray2)
val zeroBitArray4 = Array4(zeroBitArray3, zeroBitArray3)
val zeroBitArray5 = Array5(zeroBitArray4, zeroBitArray4)
val zeroBitArray6 = Array6(zeroBitArray5, zeroBitArray5)
val zeroBitArray7 = Array7(zeroBitArray6, zeroBitArray6)
val zeroBitArray8 = Array8(zeroBitArray7, zeroBitArray7)
val zeroBitArray9 = Array9(zeroBitArray8, zeroBitArray8)
val zeroBitArray10 = Array10(zeroBitArray9, zeroBitArray9)
val zeroBitArray11 = Array11(zeroBitArray10, zeroBitArray10)
val zeroBitArray12 = Array12(zeroBitArray11, zeroBitArray11)
val zeroBitArray13 = Array13(zeroBitArray12, zeroBitArray12)
val zeroBitArray14 = Array14(zeroBitArray13, zeroBitArray13)
val zeroBitArray15 = Array15(zeroBitArray14, zeroBitArray14)
val zeroBitArray16 = Array16(zeroBitArray15, zeroBitArray15)
val zeroBitArray17 = Array17(zeroBitArray16, zeroBitArray16)
val zeroBitArray18 = Array18(zeroBitArray17, zeroBitArray17)
val zeroBitArray19 = Array19(zeroBitArray18, zeroBitArray18)
val zeroBitArray20 = Array20(zeroBitArray19, zeroBitArray19)
val zeroBitArray21 = Array21(zeroBitArray20, zeroBitArray20)
val zeroBitArray22 = Array22(zeroBitArray21, zeroBitArray21)
val zeroBitArray23 = Array23(zeroBitArray22, zeroBitArray22)
val zeroBitArray24 = Array24(zeroBitArray23, zeroBitArray23)
val zeroBitArray25 = Array25(zeroBitArray24, zeroBitArray24)
val zeroBitArray26 = Array26(zeroBitArray25, zeroBitArray25)
val zeroBitArray27 = Array27(zeroBitArray26, zeroBitArray26)
val zeroBitArray28 = Array28(zeroBitArray27, zeroBitArray27)
val zeroBitArray29 = Array29(zeroBitArray28, zeroBitArray28)
val zeroBitArray30 = Array30(zeroBitArray29, zeroBitArray29)
val zeroBitArray31 = Array31(zeroBitArray30, zeroBitArray30)
val zeroBitArray32 = Array32(zeroBitArray31, zeroBitArray31)
