package data

import leo.binary.*

fun New.array1(fn: () -> Data) = data(fn(), fn())
fun New.array2(fn: () -> Data) = data(array1(fn), array1(fn))
fun New.array3(fn: () -> Data) = data(array2(fn), array2(fn))
fun New.array4(fn: () -> Data) = data(array3(fn), array3(fn))
fun New.array5(fn: () -> Data) = data(array4(fn), array4(fn))
fun New.array6(fn: () -> Data) = data(array5(fn), array5(fn))
fun New.array7(fn: () -> Data) = data(array6(fn), array6(fn))
fun New.array8(fn: () -> Data) = data(array7(fn), array7(fn))

fun Data.array1At(index: Int) = at(index.bit0)
fun Data.array2At(index: Int) = at(index.bit1).array1At(index)
fun Data.array3At(index: Int) = at(index.bit2).array2At(index)
fun Data.array4At(index: Int) = at(index.bit3).array3At(index)
fun Data.array5At(index: Int) = at(index.bit4).array4At(index)
fun Data.array6At(index: Int) = at(index.bit5).array5At(index)
fun Data.array7At(index: Int) = at(index.bit6).array6At(index)
fun Data.array8At(index: Int) = at(index.bit7).array7At(index)
