def bracket = new Cube(50,50,30).toCSG()
			.toZMin()

bracket = bracket.difference(new Cube(25,25,bracket.getTotalZ()-17).toCSG()
			.toZMax()
			.movez(bracket.getMaxZ()))
def slot=new Cube(0.93,10.5,bracket.getTotalZ()).toCSG()
		.toZMin()

def setscrew = new Cylinder(5.9/2,40).toCSG()
			.movez(-bracket.getTotalX()/2)
			.roty(90)
			.toZMin()
			.movez(18)
			

bracket=bracket.difference([slot,setscrew,setscrew.movez(-10)])
return bracket