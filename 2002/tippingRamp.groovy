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
double tippingtopX = dowlerCenterToRampEnd*2
double centerToRampEdge = rampRun-dowlerCenterToRampEnd
double rampTopLength =Math.sqrt( Math.pow(rampRun,2)+Math.pow(rampRise,2))-tippingtopX


def dowel = new Cylinder(dowelRadius,rampWidth).toCSG()
			.rotx(90)
			.movey(-rampWidth/2)
			.movez(heightOfPlateAtTipping-dowelDropcenter)
def tooth = new Cube(toothSpacing/2,woodThickness,woodThickness).toCSG()

def tippingTopCutout = new Cube(tippingtopX,rampWidth,rampRise).toCSG()
					.toZMin()


def tippingFrame = new Transform()
		.roty(-rampAngle)
		.movez(heightOfPlateAtTipping)
def bottomTeeth = []
def downTooth = tooth.toZMax().toXMax()
for(Double i=(rampRun-dowlerCenterToRampEnd)-toothSpacing/3;i>-dowlerCenterToRampEnd+toothSpacing/3;i-=toothSpacing){

	def moved = downTooth.movex(i)
	bottomTeeth.add(moved)
}
def topTeeth = []
def upTooth = tooth.toZMax().toXMin()
			.movey(woodThickness)
			.movez(woodThickness)

for(Double i=(rampSupportlength/2)-toothSpacing/2;i>-rampSupportlength/2;i-=toothSpacing){

	def moved = upTooth.movex(i)
	topTeeth.add(moved)
}

def rampTeeth = []
def rampTooth = tooth.toZMax().toXMin()
			.movez(woodThickness)

for(Double i=-toothSpacing;i>-rampTopLength;i-=toothSpacing){
	def moved = rampTooth.movex(i)
				.roty(-rampAngle)
				.movex(centerToRampEdge)
	rampTeeth.add(moved)
			
}



def tippingRib = new Cube(rampSupportlength,woodThickness,dowelDropcenter*2).toCSG()
					.toZMax()	
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
				.union(rampTeeth)
				.difference(rampTop)
				.setColor(javafx.scene.paint.Color.BLUE);
def tippingTop = new Cube(dowlerCenterToRampEnd*2,rampWidth,woodThickness).toCSG()
					.toZMin()
					.roty(-rampAngle)
					.movez(heightOfPlateAtTipping)
					.difference(tippingRib)
					.setColor(javafx.scene.paint.Color.WHITE);
					
def rampTop = new Cube(rampTopLength-4,rampWidth,woodThickness).toCSG()
			.toZMin()
			.toXMax()
			.roty(-rampAngle)
			.movex(rampRun- dowlerCenterToRampEnd)
			.difference(lowerRib)
return [lowerRib,tippingTop,dowel,tippingRib,rampTop,rampTeeth]