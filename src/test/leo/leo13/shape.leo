define
	circle
	contains: radius: number

	rectangle
	contains
		width: number
		height: number

	shape
	contains: options: circle rectangle

	circle area
	gives: area
		pi number
		times: given circle radius number
		times: given circle radius number

	rectangle area
	gives: area
		given rectangle width number
		times: given rectangle height number
