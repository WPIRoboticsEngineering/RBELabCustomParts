def bracket = new Cube(40,40,40).toCSG()
			.toZMin()

bracket = bracket.difference(new Cube(25,25,bracket.getTotalZ()-17).toCSG()
			.toZMax()
			.movez(bracket.getMaxZ()))
def slot=new Cube(0.7,9.5,bracket.getTotalZ()).toCSG()
		.toZMin()
bracket=bracket.difference(slot)
return bracket