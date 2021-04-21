
import eu.mihosoft.vrl.v3d.*

double woodThickness = 3.2
double rampRun = 400
double rampRise =70

double dowelRadius = 5.5/2
double dowlerCenterToRampEnd = rampRun/4
double rampAngle =Math.toDegrees(Math.atan2(rampRise,rampRun))
double rampWidth = 200
double ribspacing =rampWidth-(woodThickness*5)
double radAngle =Math.tan(Math.toRadians(rampAngle))
double heightOfPlateAtTipping =radAngle*(rampRun-dowlerCenterToRampEnd)
double dowelDropcenter = dowelRadius*3
double toothSpacing =60
double rampSupportlength = dowlerCenterToRampEnd*2-50
double tippingtopX = dowlerCenterToRampEnd*2
double centerToRampEdge = rampRun-dowlerCenterToRampEnd
double rampTopLength =Math.sqrt( Math.pow(rampRun,2)+Math.pow(rampRise,2))-tippingtopX
double lipRun = Math.tan(Math.toRadians(90-rampAngle))*(woodThickness)


def lipWedge =new Wedge(lipRun,rampWidth/3,woodThickness).toCSG()

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
				//.difference(rampTop)
				.setColor(javafx.scene.paint.Color.BLUE);
def extrTippingTopLen = 40.0				
def tippingTop = new Cube(dowlerCenterToRampEnd*2+4+extrTippingTopLen,rampWidth,woodThickness).toCSG()
					.movex(-extrTippingTopLen/2.0)
					.toZMin()
					.roty(-rampAngle)
					.movez(heightOfPlateAtTipping)
					//.difference(tippingRib)
					.setColor(javafx.scene.paint.Color.WHITE);
def offset =woodThickness/Math.sin(Math.toRadians(rampAngle))+10
println "Extra Ramp len = "+offset 					
def rampTop = new Cube(rampTopLength+offset,rampWidth,woodThickness).toCSG()
			.toZMin()
			.toXMax()
			.movex(4+offset)
			.roty(-rampAngle)
			.movex(rampRun- dowlerCenterToRampEnd)
			//.difference(lowerRib)
def supportRibs = []
def tippingRibs = []
for(double i=rampWidth/2-10;i>-rampWidth/2;i-=ribspacing){
	println "Adding rib at "+i
	supportRibs.add(lowerRib.movey(i))
	tippingRibs.add(tippingRib.movey(i))
}
tippingTop=tippingTop.difference(tippingRibs)
rampTop=rampTop.difference(supportRibs)
def bottomPlate = new Cube(rampRun,rampWidth,woodThickness).toCSG()
				.toZMax()
				.toXMin()
				.movex(-dowlerCenterToRampEnd)
				.difference(supportRibs)
def lipFront=[]
for(int i=0;i<3;i++){
	lipFront.add(lipWedge
		.movex(centerToRampEdge)
		.movey(i*rampWidth/3-rampWidth/2)
		.difference(rampTop)
		.setColor(javafx.scene.paint.Color.PINK)
					
		)
	lipFront.add(lipWedge
		.movex(centerToRampEdge+lipRun)
		.movey(i*rampWidth/3-rampWidth/2)
		.movez(-woodThickness)
		.setColor(javafx.scene.paint.Color.PINK)
					
		)
}
tippingTop.addExportFormat("svg")// make an svg of the object
rampTop.addExportFormat("svg")// make an svg of the object
bottomPlate.addExportFormat("svg")// make an svg of the object
tippingTop.setName("tippingTop")
rampTop.setName("rampTop")
bottomPlate.setName("bottomPlate")
bottomPlate.setManufacturing({ toMfg ->
	return toMfg
			.toZMin()
			.toXMin()
			.toYMin()
})
tippingTop.setManufacturing({ toMfg ->
	return toMfg
			.transformed(tippingFrame.inverse())
			.toZMin()
			.toXMin()
			.toYMin()
})
rampTop.setManufacturing({ toMfg ->
	return toMfg
			.transformed(tippingFrame.inverse())
			.toZMin()
			.toXMin()
			.toYMin()
})
supportRibs.collect{
	it.setName("supportRibs")
	it.setManufacturing({ toMfg ->
	return toMfg
			.rotx(90)
			.toZMin()
			.toXMin()
			.toYMin()
})
it.addExportFormat("svg")// make an svg of the object
}
tippingRibs.collect{
	it.setManufacturing({ toMfg ->
	return toMfg
			.transformed(tippingFrame.inverse())
			.rotx(90)
			.toZMin()
			.toXMin()
			.toYMin()
})
	it.setName("tippingRibs")
	it.addExportFormat("svg")// make an svg of the object
}

lipFront.collect{
	it.setName("lips")
	it.setManufacturing({ toMfg ->
	return toMfg
			.toZMin()
			.toXMin()
			.toYMin()
})
}

return [tippingRibs,tippingTop,dowel,supportRibs,rampTop,bottomPlate]