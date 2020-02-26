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
double toothSpacing =60
double rampSupportlength = dowlerCenterToRampEnd*2-50
println "Ramp angle = "+rampAngle+" height "+heightOfPlateAtTipping

def dowel = new Cylinder(dowelRadius,rampWidth).toCSG()
			.rotx(90)
			.movey(-rampWidth/2)
			.movez(heightOfPlateAtTipping-dowelDropcenter)
def tooth = new Cube(toothSpacing/2,woodThickness,woodThickness).toCSG()

def tippingTopCutout = new Cube(dowlerCenterToRampEnd*2,rampWidth,rampRise).toCSG()
					.toZMin()
					.movez(-woodThickness)


def tippingFrame = new Transform()
		.roty(-rampAngle)
		.movez(heightOfPlateAtTipping)
def bottomTeeth = []
def downTooth = tooth.toZMax().toXMax()
for(Double i=(rampRun-dowlerCenterToRampEnd);i>-dowlerCenterToRampEnd+toothSpacing/2;i-=toothSpacing){

	def moved = downTooth.movex(i)
	bottomTeeth.add(moved)
}
def topTeeth = []
def upTooth = tooth.toZMax().toXMax()
			.movey(woodThickness)

for(Double i=(rampSupportlength/2);i>-rampSupportlength/2;i-=toothSpacing){

	def moved = upTooth.movex(i)
	topTeeth.add(moved)
}


def tippingRib = new Cube(rampSupportlength,woodThickness,dowelDropcenter*2).toCSG()
					.toZMax()	
					.movez(-woodThickness)
					.movey(woodThickness)
					.union(topTeeth)
					.transformed(tippingFrame)
					.difference(dowel)
def lowerRib = new Wedge(rampRun,woodThickness,rampRise).toCSG()
				.movey(-woodThickness/2)
				.movex(-dowlerCenterToRampEnd)
				.difference(tippingTopCutout
					.transformed(tippingFrame)
					)
				.difference(tippingTopCutout
					.movez(heightOfPlateAtTipping)
					)
				.difference(dowel)
				.union(bottomTeeth)
def tippingTop = new Cube(dowlerCenterToRampEnd*2,rampWidth,woodThickness).toCSG()
					.toZMax()
					.roty(-rampAngle)
					.movez(heightOfPlateAtTipping)
					.difference(tippingRib)
return [lowerRib,tippingTop,dowel,tippingRib,tooth]