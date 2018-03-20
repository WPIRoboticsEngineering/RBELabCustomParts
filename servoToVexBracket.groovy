import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
CSGDatabase.clear()
double vexGrid = (1.0/2.0)* 25.4
double shellThickness =2 
String servoType = "hv5932mg"
LengthParameter printerOffset 		= new LengthParameter("printerOffset",0.5,[1.2,0])                
LengthParameter boltLength		= new LengthParameter("Bolt Length",10,[180,10])
StringParameter bearingSizeParam 			= new StringParameter("Encoder Board Bearing","R8-60355K505",Vitamins.listVitaminSizes("ballBearing"))
StringParameter servoparam 			= new StringParameter("Serov to use","hv5932mg",Vitamins.listVitaminSizes("hobbyServo"))
StringParameter hornparam 			= new StringParameter("Horn type","hv6214mg_6",Vitamins.listVitaminSizes("hobbyServoHorn"))
StringParameter gearAParam 			 	= new StringParameter("Gear A","36T",Vitamins.listVitaminSizes("vexGear"))

HashMap<String, Object>  gearAMeasurments = Vitamins.getConfiguration( "vexGear",gearAParam.getStrValue())
HashMap<String, Object>  bearingData = Vitamins.getConfiguration("ballBearing",bearingSizeParam.getStrValue())
HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo",servoparam.getStrValue())

double washerThickness = 1

CSG gearA = Vitamins.get( "vexGear",gearAParam.getStrValue())
CSG vitaminFromScript = Vitamins.get("hobbyServo",servoparam.getStrValue());
CSG horn = Vitamins.get("hobbyServoHorn",hornparam.getStrValue())
				.rotx(180)
				.movez(vitaminFromScript.getMaxZ())
double bearingSurface = horn.getMaxZ()+2
CSG vshaft =  (CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "vexShaft.groovy" , // file to load
	                              [vitaminFromScript.getMaxZ()*2]
                        )
               .movez(bearingSurface)
boltLength.setMM(100)                       
CSG bolt = Vitamins.get("capScrew","8#32")
CSG bearing = Vitamins.get("ballBearing",bearingSizeParam.getStrValue())
			.makeKeepaway(printerOffset.getMM()/2)
			.toZMin()
			.movez(bearingSurface)


CSG pin =new Cylinder(bearingData.innerDiameter/2-(printerOffset.getMM()/2),bearingData.width+shellThickness+washerThickness).toCSG() // a one line Cylinder
			.movez(bearingSurface)
CSG washer =new Cylinder(bearingData.innerDiameter/2-(printerOffset.getMM()/2)+3,washerThickness).toCSG() // a one line Cylinder
			.movez(bearingSurface)
double topShellTHickness =bearingData.width+shellThickness
CSG bearingLug=new Cylinder(bearingData.outerDiameter/2+shellThickness,topShellTHickness).toCSG() // a one line Cylinder
			.movez(bearingSurface+washerThickness)
CSG boltLug=new Cylinder(2+shellThickness,topShellTHickness).toCSG()  	
		.movez(bearingSurface+washerThickness)

boltLug=CSG.unionAll([boltLug,
				boltLug.movey(vexGrid),
				boltLug.movey(-vexGrid)])
boltLug=CSG.unionAll([boltLug.movex(vexGrid*2),
				boltLug.movex(-vexGrid*2)])
				.hull()
double gridOffset = 2
CSG bolts = CSG.unionAll([bolt.movex(vexGrid*gridOffset),
					bolt.movex(-vexGrid*gridOffset)])
bolts=CSG.unionAll([bolts,
				bolts.movey(vexGrid),
				bolts.movey(-vexGrid)])
			.movez(bearingSurface+10)		
double bottomOfGear = vitaminData.bottomOfFlangeToTopOfBody+1
gearA=gearA
	.toZMax()
	.movez(bearingSurface)
	.union(gearA
			.toZMin()
			.movez(bottomOfGear))
	.union([washer,pin])
	.difference(vshaft)
	.difference(bearing)
	.difference(horn)
CSG caseTop = bearingLug
			.union(boltLug)
			.difference(pin.hull().makeKeepaway(shellThickness))
			.difference(bearing.hull())
			.difference(bolts)
CSG caseBottom = boltLug.toZMax()
				.movez(boltLug.getMinZ())
				.difference(gearA.hull().makeKeepaway(1))
				.difference(gearA.hull().makeKeepaway(1).movez(washerThickness))
				.difference(bolts)
println vitaminData
double servomountTHickness = vitaminData.bottomOfFlangeToTopOfBody-vitaminData.flangeThickness
CSG servoMpunt =new Cube(vitaminData.servoThinDimentionThickness,
				vitaminData.flangeLongDimention,
				servomountTHickness).toCSG()
				.toYMax()
				.movey(vitaminData.shaftToShortSideDistance+(vitaminData.flangeLongDimention-vitaminData.servoThickDimentionThickness)/2)
				.toZMin()
				.movez(vitaminData.flangeThickness)
CSG boltLugLower=new Cylinder(2+shellThickness,servomountTHickness).toCSG()  	
		.movez(vitaminData.flangeThickness)

boltLugLower=CSG.unionAll([boltLugLower,
				boltLugLower.movey(vexGrid),
				boltLugLower.movey(-vexGrid)])
boltLugLower=CSG.unionAll([boltLugLower.movex(vexGrid*2),
				boltLugLower.movex(-vexGrid*2)])
				.hull()
servoMpunt=servoMpunt.union(	boltLugLower).hull()					
caseBottom=CSG.unionAll([caseBottom,
					servoMpunt,
caseBottom.toZMin().movez(vitaminData.bottomOfFlangeToTopOfBody),
caseBottom.toZMin().movez(vitaminData.flangeThickness)
])
.minkowskiDifference(vitaminFromScript,printerOffset.getMM())
.difference(bolts)

gearA.setMfg({toMfg ->
	return toMfg.rotx(180)
		.toZMin()
})
caseTop.setMfg({toMfg ->
	return toMfg.rotx(180)
		.toZMin()
})
caseBottom.setMfg({toMfg ->
	return toMfg
		.toZMin()
})

gearA.setName("GearModule")
caseTop.setName("caseTop")
caseBottom.setName("caseBottom")
return [gearA,caseTop,caseBottom]