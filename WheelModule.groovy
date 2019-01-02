//Your code here

CSGDatabase.clear()
LengthParameter printerOffset 			= new LengthParameter("printerOffset",0.5,[1.2,0])
def mm(def inches){
	return inches*25.4
}
double washerThick = 1
def motorOptions = []
def shaftOptions = []
for(String vitaminsType: Vitamins.listVitaminTypes()){
	HashMap<String, Object> meta = Vitamins.getMeta(vitaminsType);
	if(meta !=null && meta.containsKey("actuator"))
		motorOptions.add(vitaminsType);
	if(meta !=null && meta.containsKey("shaft"))
		shaftOptions.add(vitaminsType);
}

StringParameter motors = new StringParameter("Motor Type","roundMotor",motorOptions)
StringParameter shafts = new StringParameter("Shaft Type","dShaft",shaftOptions)
StringParameter motorSize = new StringParameter("Motor Size","WPI-gb37y3530-50en",Vitamins.listVitaminSizes(motors.getStrValue()))
StringParameter shaftSize = new StringParameter("Shaft Size","WPI-gb37y3530-50en",Vitamins.listVitaminSizes(shafts.getStrValue()))

def motorBlank= Vitamins.get(motors.getStrValue(),motorSize.getStrValue()).rotz(180)
def shaftBlank= Vitamins.get(shafts.getStrValue(),shaftSize.getStrValue())

double motorToMountPlane = motorBlank.getMaxZ()
double shaftLength=shaftBlank.getTotalZ()

motorBlank=motorBlank.movez(-motorToMountPlane)
double tireID = mm(1.0+(5.0/8.0))
double tireOD = mm(2.0)
double width = mm(3.0/16.0)
double sweepCenter = (double)(tireOD+tireID)/4.0
def tire = CSG.unionAll(
		Extrude.revolve(new Cylinder(width/2-printerOffset.getMM()/2,1.0).toCSG().roty(90),
		sweepCenter, // rotation center radius, if 0 it is a circle, larger is a donut. Note it can be negative too
		(double)360,// degrees through wich it should sweep
		(int)30)//number of sweep increments
		)
		.roty(90)
def bearing =Vitamins.get("ballBearing","695zz").hull().makeKeepaway(printerOffset.getMM()).toZMin()
LengthParameter boltlen 			= new LengthParameter("Bolt Length",0.675,[1.2,0])
boltlen.setMM(45)
String size ="M5"
HashMap<String, Object>  boltData = Vitamins.getConfiguration( "capScrew",size)
def bolt =new Cylinder(boltData.outerDiameter/2+printerOffset.getMM()/2,boltlen.getMM()).toCSG()
double nursertHeight = 9.5
double nutsertRad = 6.4/2
def netsert=new Cylinder(nutsertRad,nursertHeight ).toCSG() 

double pitch = 3
int atheeth =16
// call a script from another library
List<Object> bevelGears = (List<Object>)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/GearGenerator.git", // git location of the library
            "bevelGear.groovy" , // file to load
            // Parameters passed to the funcetion
            [	  atheeth,// Number of teeth gear a
	            atheeth*3,// Number of teeth gear b
	            6,// thickness of gear A
	            pitch,// gear pitch in arch length mm
	           90
            ]
            )
println "Bevel gear radius A " + bevelGears.get(2)
println "Bevel gear radius B " + bevelGears.get(3)
def gearBThickness = bevelGears.get(6)
def wheelSectionThickness = width*1.5
def wheelCenterlineX = -wheelSectionThickness/2-bevelGears.get(2)
def wheelCore = new Cylinder(sweepCenter,wheelSectionThickness).toCSG()
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex( -wheelSectionThickness-bevelGears.get(2))
def wheelwell = new Cylinder(sweepCenter+width,wheelSectionThickness+gearBThickness).toCSG()
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex( -wheelSectionThickness-bevelGears.get(2)-washerThick)			
tire=tire .movez(  bevelGears.get(3))
		.movex( wheelCenterlineX)
bearing=bearing
		.roty(90)
		.movez(  bevelGears.get(3))
		.movex( gearBThickness-bevelGears.get(2))
bearing2 = bearing.movex(bearing.getTotalX() -gearBThickness-wheelSectionThickness)
CSG outputGear = bevelGears.get(0)
CSG adrive = bevelGears.get(1)
def axelBolt = bolt
			.movez(-boltlen.getMM()/2)
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex(motorBlank.getCenterX() )
def motorY = sweepCenter+width+nursertHeight +Math.abs(motorBlank.getMinY())
def motorPlate = new Cube(boltlen.getMM(),motorY,motorToMountPlane).toCSG()
				.movex(motorBlank.getCenterX() )
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
def leftHeight = motorPlate.getMaxX()-bevelGears.get(1).getMaxX()-printerOffset.getMM()-washerThick
def baseSupportRad = 17
def leftCone = new Cylinder(baseSupportRad, // Radius at the bottom
                      		boltData.outerDiameter/2+1, // Radius at the top
                      		leftHeight, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(90)
                      		.toXMax()
                      		.movex(motorPlate.getMaxX())
                      		.movez(  bevelGears.get(3))
def rightHeight = Math.abs(motorPlate.getMinX()-wheelwell.getMinX())-printerOffset.getMM()-washerThick
def rightCone = new Cylinder(baseSupportRad, // Radius at the bottom
                      		boltData.outerDiameter/2+1, // Radius at the top
                      		rightHeight, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(-90)
                      		.toXMin()
                      		.movex(motorPlate.getMinX())
                      		.movez(  bevelGears.get(3))	
def  sideWallTHickness=motorPlate.getMaxX()-bevelGears.get(0).getMaxX()-5

def sideWallPuckL = new Cylinder(baseSupportRad, // Radius at the bottom
                      		baseSupportRad, // Radius at the top
                      		sideWallTHickness, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(90)
                      		.toXMax()
                      		.movex(motorPlate.getMaxX())
                      		.movez(  bevelGears.get(3))
 def sideWallPuckR = new Cylinder(baseSupportRad, // Radius at the bottom
                      		baseSupportRad, // Radius at the top
                      		sideWallTHickness, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(-90)
                      		.toXMin()
                      		.movex(motorPlate.getMinX())
                      		.movez(  bevelGears.get(3)) 
def mountWallBar =new Cube(sideWallTHickness,nursertHeight, bevelGears.get(3)+nutsertRad*2).toCSG()
				.toZMin()

def mountWallBarL = mountWallBar
				.toXMax()
				.movex(motorPlate.getMaxX())
				.toYMax()
				.movey(motorPlate.getMaxY())
def mountWallBarR=mountWallBar
				.toXMin()
				.movex(motorPlate.getMinX())
				.toYMax()
				.movey(motorPlate.getMaxY())
def sideWallBarL = new Cube(sideWallTHickness,motorY,motorToMountPlane).toCSG()
				.toXMax()
                    .movex(motorPlate.getMaxX())
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
				.union([sideWallPuckL,mountWallBarL]).hull()
 def sideWallBarR = new Cube(sideWallTHickness,motorY,motorToMountPlane).toCSG()
				.toXMin()
                     .movex(motorPlate.getMinX())
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
				.union([sideWallPuckR,mountWallBarR]).hull()  
def backPlate =  mountWallBarR.union(   mountWallBarL).hull()   
def gearHole =  new Cylinder(bevelGears.get(0).getMaxX()+1,motorToMountPlane).toCSG() 
				.toZMax()     

// FInal assembly section				
def bracket = CSG.unionAll([motorPlate,leftCone,rightCone,sideWallBarL,sideWallBarR,backPlate

])  .difference([axelBolt,wheelwell,motorBlank,gearHole

])	

def wheelAsmb = CSG.unionAll([adrive,wheelCore
]).difference([axelBolt,tire,bearing,bearing2

])
def driveGear = outputGear.difference([shaftBlank,motorBlank])
// Attach production scripts
wheelAsmb.setName("wheel")
	.setManufacturing({ toMfg ->
	return toMfg
			.roty(-90)
			.toXMin()
			.toYMin()
			.toZMin()
})

bracket.setName("bracket")
	.setManufacturing({ toMfg ->
	return toMfg
			.toXMin()
			.toYMin()
			.toZMin()
})
driveGear.setName("driveGear")
	.setManufacturing({ toMfg ->
	return toMfg
			.toXMin()
			.toYMin()
			.toZMin()
})
return [driveGear,bracket,wheelAsmb]