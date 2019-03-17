package leo.binary

data class Arr1<T>(val at0: T, val at1: T) {
	override fun toString() = "array1"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: T.() -> T) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: T) = update(bit) { value }
}
fun <T> arr1(at0: T, at1: T) = Arr1(at0, at1)
fun <T : Any> nullArr1() = (null as T?).arr1
val <T> T.arr1 get() = arr1(this, this)

data class Arr2<T>(val at0: Arr1<T>, val at1: Arr1<T>) {
	override fun toString() = "array2"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr1<T>.() -> Arr1<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr1<T>) = update(bit) { value }
}
fun <T> arr2(at0: Arr1<T>, at1: Arr1<T>) = Arr2(at0, at1)
fun <T : Any> nullArr2() = (null as T?).arr2
val <T> T.arr2 get() = arr1.run { arr2(this, this) }

data class Arr3<T>(val at0: Arr2<T>, val at1: Arr2<T>) {
	override fun toString() = "array3"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr2<T>.() -> Arr2<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr2<T>) = update(bit) { value }
}
fun <T> arr3(at0: Arr2<T>, at1: Arr2<T>) = Arr3(at0, at1)
fun <T : Any> nullArr3() = (null as T?).arr3
val <T> T.arr3 get() = arr2.run { arr3(this, this) }

data class Arr4<T>(val at0: Arr3<T>, val at1: Arr3<T>) {
	override fun toString() = "array4"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr3<T>.() -> Arr3<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr3<T>) = update(bit) { value }
}
fun <T> arr4(at0: Arr3<T>, at1: Arr3<T>) = Arr4(at0, at1)
fun <T : Any> nullArr4() = (null as T?).arr4
val <T> T.arr4 get() = arr3.run { arr4(this, this) }

data class Arr5<T>(val at0: Arr4<T>, val at1: Arr4<T>) {
	override fun toString() = "array5"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr4<T>.() -> Arr4<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr4<T>) = update(bit) { value }
}
fun <T> arr5(at0: Arr4<T>, at1: Arr4<T>) = Arr5(at0, at1)
fun <T : Any> nullArr5() = (null as T?).arr5
val <T> T.arr5 get() = arr4.run { arr5(this, this) }

data class Arr6<T>(val at0: Arr5<T>, val at1: Arr5<T>) {
	override fun toString() = "array6"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr5<T>.() -> Arr5<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr5<T>) = update(bit) { value }
}
fun <T> arr6(at0: Arr5<T>, at1: Arr5<T>) = Arr6(at0, at1)
fun <T : Any> nullArr6() = (null as T?).arr6
val <T> T.arr6 get() = arr5.run { arr6(this, this) }

data class Arr7<T>(val at0: Arr6<T>, val at1: Arr6<T>) {
	override fun toString() = "array7"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr6<T>.() -> Arr6<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr6<T>) = update(bit) { value }
}
fun <T> arr7(at0: Arr6<T>, at1: Arr6<T>) = Arr7(at0, at1)
fun <T : Any> nullArr7() = (null as T?).arr7
val <T> T.arr7 get() = arr6.run { arr7(this, this) }

data class Arr8<T>(val at0: Arr7<T>, val at1: Arr7<T>) {
	override fun toString() = "array8"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr7<T>.() -> Arr7<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr7<T>) = update(bit) { value }
}
fun <T> arr8(at0: Arr7<T>, at1: Arr7<T>) = Arr8(at0, at1)
fun <T : Any> nullArr8() = (null as T?).arr8
val <T> T.arr8 get() = arr7.run { arr8(this, this) }

data class Arr9<T>(val at0: Arr8<T>, val at1: Arr8<T>) {
	override fun toString() = "array9"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr8<T>.() -> Arr8<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr8<T>) = update(bit) { value }
}
fun <T> arr9(at0: Arr8<T>, at1: Arr8<T>) = Arr9(at0, at1)
fun <T : Any> nullArr9() = (null as T?).arr9
val <T> T.arr9 get() = arr8.run { arr9(this, this) }

data class Arr10<T>(val at0: Arr9<T>, val at1: Arr9<T>) {
	override fun toString() = "array10"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr9<T>.() -> Arr9<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr9<T>) = update(bit) { value }
}
fun <T> arr10(at0: Arr9<T>, at1: Arr9<T>) = Arr10(at0, at1)
fun <T : Any> nullArr10() = (null as T?).arr10
val <T> T.arr10 get() = arr9.run { arr10(this, this) }

data class Arr11<T>(val at0: Arr10<T>, val at1: Arr10<T>) {
	override fun toString() = "array11"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr10<T>.() -> Arr10<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr10<T>) = update(bit) { value }
}
fun <T> arr11(at0: Arr10<T>, at1: Arr10<T>) = Arr11(at0, at1)
fun <T : Any> nullArr11() = (null as T?).arr11
val <T> T.arr11 get() = arr10.run { arr11(this, this) }

data class Arr12<T>(val at0: Arr11<T>, val at1: Arr11<T>) {
	override fun toString() = "array12"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr11<T>.() -> Arr11<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr11<T>) = update(bit) { value }
}
fun <T> arr12(at0: Arr11<T>, at1: Arr11<T>) = Arr12(at0, at1)
fun <T : Any> nullArr12() = (null as T?).arr12
val <T> T.arr12 get() = arr11.run { arr12(this, this) }

data class Arr13<T>(val at0: Arr12<T>, val at1: Arr12<T>) {
	override fun toString() = "array13"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr12<T>.() -> Arr12<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr12<T>) = update(bit) { value }
}
fun <T> arr13(at0: Arr12<T>, at1: Arr12<T>) = Arr13(at0, at1)
fun <T : Any> nullArr13() = (null as T?).arr13
val <T> T.arr13 get() = arr12.run { arr13(this, this) }

data class Arr14<T>(val at0: Arr13<T>, val at1: Arr13<T>) {
	override fun toString() = "array14"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr13<T>.() -> Arr13<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr13<T>) = update(bit) { value }
}
fun <T> arr14(at0: Arr13<T>, at1: Arr13<T>) = Arr14(at0, at1)
fun <T : Any> nullArr14() = (null as T?).arr14
val <T> T.arr14 get() = arr13.run { arr14(this, this) }

data class Arr15<T>(val at0: Arr14<T>, val at1: Arr14<T>) {
	override fun toString() = "array15"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr14<T>.() -> Arr14<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr14<T>) = update(bit) { value }
}
fun <T> arr15(at0: Arr14<T>, at1: Arr14<T>) = Arr15(at0, at1)
fun <T : Any> nullArr15() = (null as T?).arr15
val <T> T.arr15 get() = arr14.run { arr15(this, this) }

data class Arr16<T>(val at0: Arr15<T>, val at1: Arr15<T>) {
	override fun toString() = "array16"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr15<T>.() -> Arr15<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr15<T>) = update(bit) { value }
}
fun <T> arr16(at0: Arr15<T>, at1: Arr15<T>) = Arr16(at0, at1)
fun <T : Any> nullArr16() = (null as T?).arr16
val <T> T.arr16 get() = arr15.run { arr16(this, this) }

data class Arr17<T>(val at0: Arr16<T>, val at1: Arr16<T>) {
	override fun toString() = "array17"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr16<T>.() -> Arr16<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr16<T>) = update(bit) { value }
}
fun <T> arr17(at0: Arr16<T>, at1: Arr16<T>) = Arr17(at0, at1)
fun <T : Any> nullArr17() = (null as T?).arr17
val <T> T.arr17 get() = arr16.run { arr17(this, this) }

data class Arr18<T>(val at0: Arr17<T>, val at1: Arr17<T>) {
	override fun toString() = "array18"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr17<T>.() -> Arr17<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr17<T>) = update(bit) { value }
}
fun <T> arr18(at0: Arr17<T>, at1: Arr17<T>) = Arr18(at0, at1)
fun <T : Any> nullArr18() = (null as T?).arr18
val <T> T.arr18 get() = arr17.run { arr18(this, this) }

data class Arr19<T>(val at0: Arr18<T>, val at1: Arr18<T>) {
	override fun toString() = "array19"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr18<T>.() -> Arr18<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr18<T>) = update(bit) { value }
}
fun <T> arr19(at0: Arr18<T>, at1: Arr18<T>) = Arr19(at0, at1)
fun <T : Any> nullArr19() = (null as T?).arr19
val <T> T.arr19 get() = arr18.run { arr19(this, this) }

data class Arr20<T>(val at0: Arr19<T>, val at1: Arr19<T>) {
	override fun toString() = "array20"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr19<T>.() -> Arr19<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr19<T>) = update(bit) { value }
}
fun <T> arr20(at0: Arr19<T>, at1: Arr19<T>) = Arr20(at0, at1)
fun <T : Any> nullArr20() = (null as T?).arr20
val <T> T.arr20 get() = arr19.run { arr20(this, this) }

data class Arr21<T>(val at0: Arr20<T>, val at1: Arr20<T>) {
	override fun toString() = "array21"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr20<T>.() -> Arr20<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr20<T>) = update(bit) { value }
}
fun <T> arr21(at0: Arr20<T>, at1: Arr20<T>) = Arr21(at0, at1)
fun <T : Any> nullArr21() = (null as T?).arr21
val <T> T.arr21 get() = arr20.run { arr21(this, this) }

data class Arr22<T>(val at0: Arr21<T>, val at1: Arr21<T>) {
	override fun toString() = "array22"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr21<T>.() -> Arr21<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr21<T>) = update(bit) { value }
}
fun <T> arr22(at0: Arr21<T>, at1: Arr21<T>) = Arr22(at0, at1)
fun <T : Any> nullArr22() = (null as T?).arr22
val <T> T.arr22 get() = arr21.run { arr22(this, this) }

data class Arr23<T>(val at0: Arr22<T>, val at1: Arr22<T>) {
	override fun toString() = "array23"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr22<T>.() -> Arr22<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr22<T>) = update(bit) { value }
}
fun <T> arr23(at0: Arr22<T>, at1: Arr22<T>) = Arr23(at0, at1)
fun <T : Any> nullArr23() = (null as T?).arr23
val <T> T.arr23 get() = arr22.run { arr23(this, this) }

data class Arr24<T>(val at0: Arr23<T>, val at1: Arr23<T>) {
	override fun toString() = "array24"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr23<T>.() -> Arr23<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr23<T>) = update(bit) { value }
}
fun <T> arr24(at0: Arr23<T>, at1: Arr23<T>) = Arr24(at0, at1)
fun <T : Any> nullArr24() = (null as T?).arr24
val <T> T.arr24 get() = arr23.run { arr24(this, this) }

data class Arr25<T>(val at0: Arr24<T>, val at1: Arr24<T>) {
	override fun toString() = "array25"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr24<T>.() -> Arr24<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr24<T>) = update(bit) { value }
}
fun <T> arr25(at0: Arr24<T>, at1: Arr24<T>) = Arr25(at0, at1)
fun <T : Any> nullArr25() = (null as T?).arr25
val <T> T.arr25 get() = arr24.run { arr25(this, this) }

data class Arr26<T>(val at0: Arr25<T>, val at1: Arr25<T>) {
	override fun toString() = "array26"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr25<T>.() -> Arr25<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr25<T>) = update(bit) { value }
}
fun <T> arr26(at0: Arr25<T>, at1: Arr25<T>) = Arr26(at0, at1)
fun <T : Any> nullArr26() = (null as T?).arr26
val <T> T.arr26 get() = arr25.run { arr26(this, this) }

data class Arr27<T>(val at0: Arr26<T>, val at1: Arr26<T>) {
	override fun toString() = "array27"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr26<T>.() -> Arr26<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr26<T>) = update(bit) { value }
}
fun <T> arr27(at0: Arr26<T>, at1: Arr26<T>) = Arr27(at0, at1)
fun <T : Any> nullArr27() = (null as T?).arr27
val <T> T.arr27 get() = arr26.run { arr27(this, this) }

data class Arr28<T>(val at0: Arr27<T>, val at1: Arr27<T>) {
	override fun toString() = "array28"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr27<T>.() -> Arr27<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr27<T>) = update(bit) { value }
}
fun <T> arr28(at0: Arr27<T>, at1: Arr27<T>) = Arr28(at0, at1)
fun <T : Any> nullArr28() = (null as T?).arr28
val <T> T.arr28 get() = arr27.run { arr28(this, this) }

data class Arr29<T>(val at0: Arr28<T>, val at1: Arr28<T>) {
	override fun toString() = "array29"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr28<T>.() -> Arr28<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr28<T>) = update(bit) { value }
}
fun <T> arr29(at0: Arr28<T>, at1: Arr28<T>) = Arr29(at0, at1)
fun <T : Any> nullArr29() = (null as T?).arr29
val <T> T.arr29 get() = arr28.run { arr29(this, this) }

data class Arr30<T>(val at0: Arr29<T>, val at1: Arr29<T>) {
	override fun toString() = "array30"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr29<T>.() -> Arr29<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr29<T>) = update(bit) { value }
}
fun <T> arr30(at0: Arr29<T>, at1: Arr29<T>) = Arr30(at0, at1)
fun <T : Any> nullArr30() = (null as T?).arr30
val <T> T.arr30 get() = arr29.run { arr30(this, this) }

data class Arr31<T>(val at0: Arr30<T>, val at1: Arr30<T>) {
	override fun toString() = "array31"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr30<T>.() -> Arr30<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr30<T>) = update(bit) { value }
}
fun <T> arr31(at0: Arr30<T>, at1: Arr30<T>) = Arr31(at0, at1)
fun <T : Any> nullArr31() = (null as T?).arr31
val <T> T.arr31 get() = arr30.run { arr31(this, this) }

data class Arr32<T>(val at0: Arr31<T>, val at1: Arr31<T>) {
	override fun toString() = "array32"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr31<T>.() -> Arr31<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr31<T>) = update(bit) { value }
}
fun <T> arr32(at0: Arr31<T>, at1: Arr31<T>) = Arr32(at0, at1)
fun <T : Any> nullArr32() = (null as T?).arr32
val <T> T.arr32 get() = arr31.run { arr32(this, this) }

data class Arr33<T>(val at0: Arr32<T>, val at1: Arr32<T>) {
	override fun toString() = "array33"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr32<T>.() -> Arr32<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr32<T>) = update(bit) { value }
}
fun <T> arr33(at0: Arr32<T>, at1: Arr32<T>) = Arr33(at0, at1)
fun <T : Any> nullArr33() = (null as T?).arr33
val <T> T.arr33 get() = arr32.run { arr33(this, this) }

data class Arr34<T>(val at0: Arr33<T>, val at1: Arr33<T>) {
	override fun toString() = "array34"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr33<T>.() -> Arr33<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr33<T>) = update(bit) { value }
}
fun <T> arr34(at0: Arr33<T>, at1: Arr33<T>) = Arr34(at0, at1)
fun <T : Any> nullArr34() = (null as T?).arr34
val <T> T.arr34 get() = arr33.run { arr34(this, this) }

data class Arr35<T>(val at0: Arr34<T>, val at1: Arr34<T>) {
	override fun toString() = "array35"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr34<T>.() -> Arr34<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr34<T>) = update(bit) { value }
}
fun <T> arr35(at0: Arr34<T>, at1: Arr34<T>) = Arr35(at0, at1)
fun <T : Any> nullArr35() = (null as T?).arr35
val <T> T.arr35 get() = arr34.run { arr35(this, this) }

data class Arr36<T>(val at0: Arr35<T>, val at1: Arr35<T>) {
	override fun toString() = "array36"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr35<T>.() -> Arr35<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr35<T>) = update(bit) { value }
}
fun <T> arr36(at0: Arr35<T>, at1: Arr35<T>) = Arr36(at0, at1)
fun <T : Any> nullArr36() = (null as T?).arr36
val <T> T.arr36 get() = arr35.run { arr36(this, this) }

data class Arr37<T>(val at0: Arr36<T>, val at1: Arr36<T>) {
	override fun toString() = "array37"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr36<T>.() -> Arr36<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr36<T>) = update(bit) { value }
}
fun <T> arr37(at0: Arr36<T>, at1: Arr36<T>) = Arr37(at0, at1)
fun <T : Any> nullArr37() = (null as T?).arr37
val <T> T.arr37 get() = arr36.run { arr37(this, this) }

data class Arr38<T>(val at0: Arr37<T>, val at1: Arr37<T>) {
	override fun toString() = "array38"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr37<T>.() -> Arr37<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr37<T>) = update(bit) { value }
}
fun <T> arr38(at0: Arr37<T>, at1: Arr37<T>) = Arr38(at0, at1)
fun <T : Any> nullArr38() = (null as T?).arr38
val <T> T.arr38 get() = arr37.run { arr38(this, this) }

data class Arr39<T>(val at0: Arr38<T>, val at1: Arr38<T>) {
	override fun toString() = "array39"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr38<T>.() -> Arr38<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr38<T>) = update(bit) { value }
}
fun <T> arr39(at0: Arr38<T>, at1: Arr38<T>) = Arr39(at0, at1)
fun <T : Any> nullArr39() = (null as T?).arr39
val <T> T.arr39 get() = arr38.run { arr39(this, this) }

data class Arr40<T>(val at0: Arr39<T>, val at1: Arr39<T>) {
	override fun toString() = "array40"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr39<T>.() -> Arr39<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr39<T>) = update(bit) { value }
}
fun <T> arr40(at0: Arr39<T>, at1: Arr39<T>) = Arr40(at0, at1)
fun <T : Any> nullArr40() = (null as T?).arr40
val <T> T.arr40 get() = arr39.run { arr40(this, this) }

data class Arr41<T>(val at0: Arr40<T>, val at1: Arr40<T>) {
	override fun toString() = "array41"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr40<T>.() -> Arr40<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr40<T>) = update(bit) { value }
}
fun <T> arr41(at0: Arr40<T>, at1: Arr40<T>) = Arr41(at0, at1)
fun <T : Any> nullArr41() = (null as T?).arr41
val <T> T.arr41 get() = arr40.run { arr41(this, this) }

data class Arr42<T>(val at0: Arr41<T>, val at1: Arr41<T>) {
	override fun toString() = "array42"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr41<T>.() -> Arr41<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr41<T>) = update(bit) { value }
}
fun <T> arr42(at0: Arr41<T>, at1: Arr41<T>) = Arr42(at0, at1)
fun <T : Any> nullArr42() = (null as T?).arr42
val <T> T.arr42 get() = arr41.run { arr42(this, this) }

data class Arr43<T>(val at0: Arr42<T>, val at1: Arr42<T>) {
	override fun toString() = "array43"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr42<T>.() -> Arr42<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr42<T>) = update(bit) { value }
}
fun <T> arr43(at0: Arr42<T>, at1: Arr42<T>) = Arr43(at0, at1)
fun <T : Any> nullArr43() = (null as T?).arr43
val <T> T.arr43 get() = arr42.run { arr43(this, this) }

data class Arr44<T>(val at0: Arr43<T>, val at1: Arr43<T>) {
	override fun toString() = "array44"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr43<T>.() -> Arr43<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr43<T>) = update(bit) { value }
}
fun <T> arr44(at0: Arr43<T>, at1: Arr43<T>) = Arr44(at0, at1)
fun <T : Any> nullArr44() = (null as T?).arr44
val <T> T.arr44 get() = arr43.run { arr44(this, this) }

data class Arr45<T>(val at0: Arr44<T>, val at1: Arr44<T>) {
	override fun toString() = "array45"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr44<T>.() -> Arr44<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr44<T>) = update(bit) { value }
}
fun <T> arr45(at0: Arr44<T>, at1: Arr44<T>) = Arr45(at0, at1)
fun <T : Any> nullArr45() = (null as T?).arr45
val <T> T.arr45 get() = arr44.run { arr45(this, this) }

data class Arr46<T>(val at0: Arr45<T>, val at1: Arr45<T>) {
	override fun toString() = "array46"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr45<T>.() -> Arr45<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr45<T>) = update(bit) { value }
}
fun <T> arr46(at0: Arr45<T>, at1: Arr45<T>) = Arr46(at0, at1)
fun <T : Any> nullArr46() = (null as T?).arr46
val <T> T.arr46 get() = arr45.run { arr46(this, this) }

data class Arr47<T>(val at0: Arr46<T>, val at1: Arr46<T>) {
	override fun toString() = "array47"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr46<T>.() -> Arr46<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr46<T>) = update(bit) { value }
}
fun <T> arr47(at0: Arr46<T>, at1: Arr46<T>) = Arr47(at0, at1)
fun <T : Any> nullArr47() = (null as T?).arr47
val <T> T.arr47 get() = arr46.run { arr47(this, this) }

data class Arr48<T>(val at0: Arr47<T>, val at1: Arr47<T>) {
	override fun toString() = "array48"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr47<T>.() -> Arr47<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr47<T>) = update(bit) { value }
}
fun <T> arr48(at0: Arr47<T>, at1: Arr47<T>) = Arr48(at0, at1)
fun <T : Any> nullArr48() = (null as T?).arr48
val <T> T.arr48 get() = arr47.run { arr48(this, this) }

data class Arr49<T>(val at0: Arr48<T>, val at1: Arr48<T>) {
	override fun toString() = "array49"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr48<T>.() -> Arr48<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr48<T>) = update(bit) { value }
}
fun <T> arr49(at0: Arr48<T>, at1: Arr48<T>) = Arr49(at0, at1)
fun <T : Any> nullArr49() = (null as T?).arr49
val <T> T.arr49 get() = arr48.run { arr49(this, this) }

data class Arr50<T>(val at0: Arr49<T>, val at1: Arr49<T>) {
	override fun toString() = "array50"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr49<T>.() -> Arr49<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr49<T>) = update(bit) { value }
}
fun <T> arr50(at0: Arr49<T>, at1: Arr49<T>) = Arr50(at0, at1)
fun <T : Any> nullArr50() = (null as T?).arr50
val <T> T.arr50 get() = arr49.run { arr50(this, this) }

data class Arr51<T>(val at0: Arr50<T>, val at1: Arr50<T>) {
	override fun toString() = "array51"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr50<T>.() -> Arr50<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr50<T>) = update(bit) { value }
}
fun <T> arr51(at0: Arr50<T>, at1: Arr50<T>) = Arr51(at0, at1)
fun <T : Any> nullArr51() = (null as T?).arr51
val <T> T.arr51 get() = arr50.run { arr51(this, this) }

data class Arr52<T>(val at0: Arr51<T>, val at1: Arr51<T>) {
	override fun toString() = "array52"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr51<T>.() -> Arr51<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr51<T>) = update(bit) { value }
}
fun <T> arr52(at0: Arr51<T>, at1: Arr51<T>) = Arr52(at0, at1)
fun <T : Any> nullArr52() = (null as T?).arr52
val <T> T.arr52 get() = arr51.run { arr52(this, this) }

data class Arr53<T>(val at0: Arr52<T>, val at1: Arr52<T>) {
	override fun toString() = "array53"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr52<T>.() -> Arr52<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr52<T>) = update(bit) { value }
}
fun <T> arr53(at0: Arr52<T>, at1: Arr52<T>) = Arr53(at0, at1)
fun <T : Any> nullArr53() = (null as T?).arr53
val <T> T.arr53 get() = arr52.run { arr53(this, this) }

data class Arr54<T>(val at0: Arr53<T>, val at1: Arr53<T>) {
	override fun toString() = "array54"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr53<T>.() -> Arr53<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr53<T>) = update(bit) { value }
}
fun <T> arr54(at0: Arr53<T>, at1: Arr53<T>) = Arr54(at0, at1)
fun <T : Any> nullArr54() = (null as T?).arr54
val <T> T.arr54 get() = arr53.run { arr54(this, this) }

data class Arr55<T>(val at0: Arr54<T>, val at1: Arr54<T>) {
	override fun toString() = "array55"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr54<T>.() -> Arr54<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr54<T>) = update(bit) { value }
}
fun <T> arr55(at0: Arr54<T>, at1: Arr54<T>) = Arr55(at0, at1)
fun <T : Any> nullArr55() = (null as T?).arr55
val <T> T.arr55 get() = arr54.run { arr55(this, this) }

data class Arr56<T>(val at0: Arr55<T>, val at1: Arr55<T>) {
	override fun toString() = "array56"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr55<T>.() -> Arr55<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr55<T>) = update(bit) { value }
}
fun <T> arr56(at0: Arr55<T>, at1: Arr55<T>) = Arr56(at0, at1)
fun <T : Any> nullArr56() = (null as T?).arr56
val <T> T.arr56 get() = arr55.run { arr56(this, this) }

data class Arr57<T>(val at0: Arr56<T>, val at1: Arr56<T>) {
	override fun toString() = "array57"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr56<T>.() -> Arr56<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr56<T>) = update(bit) { value }
}
fun <T> arr57(at0: Arr56<T>, at1: Arr56<T>) = Arr57(at0, at1)
fun <T : Any> nullArr57() = (null as T?).arr57
val <T> T.arr57 get() = arr56.run { arr57(this, this) }

data class Arr58<T>(val at0: Arr57<T>, val at1: Arr57<T>) {
	override fun toString() = "array58"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr57<T>.() -> Arr57<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr57<T>) = update(bit) { value }
}
fun <T> arr58(at0: Arr57<T>, at1: Arr57<T>) = Arr58(at0, at1)
fun <T : Any> nullArr58() = (null as T?).arr58
val <T> T.arr58 get() = arr57.run { arr58(this, this) }

data class Arr59<T>(val at0: Arr58<T>, val at1: Arr58<T>) {
	override fun toString() = "array59"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr58<T>.() -> Arr58<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr58<T>) = update(bit) { value }
}
fun <T> arr59(at0: Arr58<T>, at1: Arr58<T>) = Arr59(at0, at1)
fun <T : Any> nullArr59() = (null as T?).arr59
val <T> T.arr59 get() = arr58.run { arr59(this, this) }

data class Arr60<T>(val at0: Arr59<T>, val at1: Arr59<T>) {
	override fun toString() = "array60"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr59<T>.() -> Arr59<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr59<T>) = update(bit) { value }
}
fun <T> arr60(at0: Arr59<T>, at1: Arr59<T>) = Arr60(at0, at1)
fun <T : Any> nullArr60() = (null as T?).arr60
val <T> T.arr60 get() = arr59.run { arr60(this, this) }

data class Arr61<T>(val at0: Arr60<T>, val at1: Arr60<T>) {
	override fun toString() = "array61"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr60<T>.() -> Arr60<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr60<T>) = update(bit) { value }
}
fun <T> arr61(at0: Arr60<T>, at1: Arr60<T>) = Arr61(at0, at1)
fun <T : Any> nullArr61() = (null as T?).arr61
val <T> T.arr61 get() = arr60.run { arr61(this, this) }

data class Arr62<T>(val at0: Arr61<T>, val at1: Arr61<T>) {
	override fun toString() = "array62"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr61<T>.() -> Arr61<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr61<T>) = update(bit) { value }
}
fun <T> arr62(at0: Arr61<T>, at1: Arr61<T>) = Arr62(at0, at1)
fun <T : Any> nullArr62() = (null as T?).arr62
val <T> T.arr62 get() = arr61.run { arr62(this, this) }

data class Arr63<T>(val at0: Arr62<T>, val at1: Arr62<T>) {
	override fun toString() = "array63"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr62<T>.() -> Arr62<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr62<T>) = update(bit) { value }
}
fun <T> arr63(at0: Arr62<T>, at1: Arr62<T>) = Arr63(at0, at1)
fun <T : Any> nullArr63() = (null as T?).arr63
val <T> T.arr63 get() = arr62.run { arr63(this, this) }

data class Arr64<T>(val at0: Arr63<T>, val at1: Arr63<T>) {
	override fun toString() = "array64"
	fun at(bit: Bit) = if (bit.isZero) at0 else at1
	fun update(bit: Bit, fn: Arr63<T>.() -> Arr63<T>) = if (bit.isZero) copy(at0 = at0.fn()) else copy(at1 = at1.fn())
	fun put(bit: Bit, value: Arr63<T>) = update(bit) { value }
}
fun <T> arr64(at0: Arr63<T>, at1: Arr63<T>) = Arr64(at0, at1)
fun <T : Any> nullArr64() = (null as T?).arr64
val <T> T.arr64 get() = arr63.run { arr64(this, this) }

fun <T> Arr8<T>.at(index: Byte) = this
	.at(index.bit7)
	.at(index.bit6)
	.at(index.bit5)
	.at(index.bit4)
	.at(index.bit3)
	.at(index.bit2)
	.at(index.bit1)
	.at(index.bit0)

fun <T> Arr8<T>.update(index: Byte, fn: T.() -> T) =
	update(index.bit7) {
		update(index.bit6) {
			update(index.bit5) {
				update(index.bit4) {
					update(index.bit3) {
						update(index.bit2) {
							update(index.bit1) {
								update(index.bit0) {
									fn()
								}
							}
						}
					}
				}
			}
		}
	}

fun <T> Arr8<T>.put(index: Byte, value: T) =
	update(index) { value }

fun <T> Arr16<T>.at(index: Short) = this
	.at(index.bit15)
	.at(index.bit14)
	.at(index.bit13)
	.at(index.bit12)
	.at(index.bit11)
	.at(index.bit10)
	.at(index.bit9)
	.at(index.bit8)
	.at(index.bit7)
	.at(index.bit6)
	.at(index.bit5)
	.at(index.bit4)
	.at(index.bit3)
	.at(index.bit2)
	.at(index.bit1)
	.at(index.bit0)

fun <T> Arr16<T>.update(index: Short, fn: T.() -> T) =
	update(index.bit15) {
		update(index.bit14) {
			update(index.bit13) {
				update(index.bit12) {
					update(index.bit11) {
						update(index.bit10) {
							update(index.bit9) {
								update(index.bit8) {
									update(index.bit7) {
										update(index.bit6) {
											update(index.bit5) {
												update(index.bit4) {
													update(index.bit3) {
														update(index.bit2) {
															update(index.bit1) {
																update(index.bit0) {
																	fn()
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

fun <T> Arr16<T>.put(index: Short, value: T) =
	update(index) { value }

fun <T> Arr32<T>.at(index: Int) = this
	.at(index.bit31)
	.at(index.bit30)
	.at(index.bit29)
	.at(index.bit28)
	.at(index.bit27)
	.at(index.bit26)
	.at(index.bit25)
	.at(index.bit24)
	.at(index.bit23)
	.at(index.bit22)
	.at(index.bit21)
	.at(index.bit20)
	.at(index.bit19)
	.at(index.bit18)
	.at(index.bit17)
	.at(index.bit16)
	.at(index.bit15)
	.at(index.bit14)
	.at(index.bit13)
	.at(index.bit12)
	.at(index.bit11)
	.at(index.bit10)
	.at(index.bit9)
	.at(index.bit8)
	.at(index.bit7)
	.at(index.bit6)
	.at(index.bit5)
	.at(index.bit4)
	.at(index.bit3)
	.at(index.bit2)
	.at(index.bit1)
	.at(index.bit0)

fun <T> Arr32<T>.update(index: Int, fn: T.() -> T) =
	update(index.bit31) {
		update(index.bit30) {
			update(index.bit29) {
				update(index.bit28) {
					update(index.bit27) {
						update(index.bit26) {
							update(index.bit25) {
								update(index.bit24) {
									update(index.bit23) {
										update(index.bit22) {
											update(index.bit21) {
												update(index.bit20) {
													update(index.bit19) {
														update(index.bit18) {
															update(index.bit17) {
																update(index.bit16) {
																	update(index.bit15) {
																		update(index.bit14) {
																			update(index.bit13) {
																				update(index.bit12) {
																					update(index.bit11) {
																						update(index.bit10) {
																							update(index.bit9) {
																								update(index.bit8) {
																									update(index.bit7) {
																										update(index.bit6) {
																											update(index.bit5) {
																												update(index.bit4) {
																													update(index.bit3) {
																														update(index.bit2) {
																															update(index.bit1) {
																																update(index.bit0) {
																																	fn()
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

fun <T> Arr32<T>.put(index: Int, value: T) =
	update(index) { value }

fun <T> Arr64<T>.at(index: Long) = this
	.at(index.bit63)
	.at(index.bit62)
	.at(index.bit61)
	.at(index.bit60)
	.at(index.bit59)
	.at(index.bit58)
	.at(index.bit57)
	.at(index.bit56)
	.at(index.bit55)
	.at(index.bit54)
	.at(index.bit53)
	.at(index.bit52)
	.at(index.bit51)
	.at(index.bit50)
	.at(index.bit49)
	.at(index.bit48)
	.at(index.bit47)
	.at(index.bit46)
	.at(index.bit45)
	.at(index.bit44)
	.at(index.bit43)
	.at(index.bit42)
	.at(index.bit41)
	.at(index.bit40)
	.at(index.bit39)
	.at(index.bit38)
	.at(index.bit37)
	.at(index.bit36)
	.at(index.bit35)
	.at(index.bit34)
	.at(index.bit33)
	.at(index.bit32)
	.at(index.bit31)
	.at(index.bit30)
	.at(index.bit29)
	.at(index.bit28)
	.at(index.bit27)
	.at(index.bit26)
	.at(index.bit25)
	.at(index.bit24)
	.at(index.bit23)
	.at(index.bit22)
	.at(index.bit21)
	.at(index.bit20)
	.at(index.bit19)
	.at(index.bit18)
	.at(index.bit17)
	.at(index.bit16)
	.at(index.bit15)
	.at(index.bit14)
	.at(index.bit13)
	.at(index.bit12)
	.at(index.bit11)
	.at(index.bit10)
	.at(index.bit9)
	.at(index.bit8)
	.at(index.bit7)
	.at(index.bit6)
	.at(index.bit5)
	.at(index.bit4)
	.at(index.bit3)
	.at(index.bit2)
	.at(index.bit1)
	.at(index.bit0)

fun <T> Arr64<T>.update(index: Long, fn: T.() -> T) =
	update(index.bit63) {
		update(index.bit62) {
			update(index.bit61) {
				update(index.bit60) {
					update(index.bit59) {
						update(index.bit58) {
							update(index.bit57) {
								update(index.bit56) {
									update(index.bit55) {
										update(index.bit54) {
											update(index.bit53) {
												update(index.bit52) {
													update(index.bit51) {
														update(index.bit50) {
															update(index.bit49) {
																update(index.bit48) {
																	update(index.bit47) {
																		update(index.bit46) {
																			update(index.bit45) {
																				update(index.bit44) {
																					update(index.bit43) {
																						update(index.bit42) {
																							update(index.bit41) {
																								update(index.bit40) {
																									update(index.bit39) {
																										update(index.bit38) {
																											update(index.bit37) {
																												update(index.bit36) {
																													update(index.bit35) {
																														update(index.bit34) {
																															update(index.bit33) {
																																update(index.bit32) {
																																	update(index.bit31) {
																																		update(index.bit30) {
																																			update(index.bit29) {
																																				update(index.bit28) {
																																					update(index.bit27) {
																																						update(index.bit26) {
																																							update(index.bit25) {
																																								update(index.bit24) {
																																									update(index.bit23) {
																																										update(index.bit22) {
																																											update(index.bit21) {
																																												update(index.bit20) {
																																													update(index.bit19) {
																																														update(index.bit18) {
																																															update(index.bit17) {
																																																update(index.bit16) {
																																																	update(index.bit15) {
																																																		update(index.bit14) {
																																																			update(index.bit13) {
																																																				update(index.bit12) {
																																																					update(index.bit11) {
																																																						update(index.bit10) {
																																																							update(index.bit9) {
																																																								update(index.bit8) {
																																																									update(index.bit7) {
																																																										update(index.bit6) {
																																																											update(index.bit5) {
																																																												update(index.bit4) {
																																																													update(index.bit3) {
																																																														update(index.bit2) {
																																																															update(index.bit1) {
																																																																update(index.bit0) {
																																																																	fn()
																																																																}
																																																															}
																																																														}
																																																													}
																																																												}
																																																											}
																																																										}
																																																									}
																																																								}
																																																							}
																																																						}
																																																					}
																																																				}
																																																			}
																																																		}
																																																	}
																																																}
																																															}
																																														}
																																													}
																																												}
																																											}
																																										}
																																									}
																																								}
																																							}
																																						}
																																					}
																																				}
																																			}
																																		}
																																	}
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

fun <T> Arr64<T>.put(index: Long, value: T) =
	update(index) { value }
