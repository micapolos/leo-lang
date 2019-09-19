define: circle has: radius: number

define: rectangle has
	width: number
	height: number

define: shape has
	either: circle
	either: rectangle

define
	area: circle
	gives
		pi number
		times: given area circle radius number
		times: given area circle radius number

define
	area: rectangle
	gives
		given area rectangle width number
		times: given area rectangle height number
