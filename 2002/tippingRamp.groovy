double woodThickness = 5.34
double rampRun = 800
double rampRise =140
double dowelRadius = 25.4/4.0
double dowlerCenterToRampEnd = 200
double rampAngle =Math.toDegrees(Math.atan2(rampRise,rampRun))
double rampWidth = 500
double radAngle =Math.tan(Math.toRadians(rampAngle))
double heightOfPlateAtTipping =radAngle*(rampRun-dowlerCenterToRampEnd)
double dowelDropcenter = dowelRadius*3
println "Ramp angle = "+rampAngle+" height "+heightOfPlateAtTipping

def dowel = new Cylinder(dowelRadius,rampWidth).toCSG()
			.rotx(90)
			.movey(-rampWidth/2)
			.movez(heightOfPlateAtTipping-dowelDropcenter)

def tippingTopCutout = new Cube(dowlerCenterToRampEnd*2,rampWidth,rampRise).toCSG()
					.toZMin()
					.movez(-woodThickness)

def tippingTop = new Cube(dowlerCenterToRampEnd*2,rampWidth,woodThickness).toCSG()
					.toZMax()
					.roty(-rampAngle)
					.movez(heightOfPlateAtTipping)

def lowerRib = new Wedge(rampRun,woodThickness,rampRise).toCSG()
				.movex(-dowlerCenterToRampEnd)
				.difference(tippingTopCutout
					.roty(-rampAngle)
					.movez(heightOfPlateAtTipping)
					)
				.difference(tippingTopCutout
					
					.movez(heightOfPlateAtTipping)
					)

return [lowerRib,tippingTop,dowel]