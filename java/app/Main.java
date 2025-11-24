package app;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.Loop_reader;
import Entities.Procedure;
import Entities.Section;
import Entities.Technique;
import Entities.Transformation;

public class Main {

	public static final int POPULATION_SIZE = 8;
	public static final String POLY_BENCHMARK = "Poly";
	public static final String NASA_BENCHMARK = "Nasa";
	public static final String TUNING = "Tuning";
	public static final String TUNING_FLAG = "-Tuning_Analysis";
	public static final String INLINING_EXPANTION = "tinline=mode=1:depth:0:pragma=0:foronly=1:complement=0:functions=";
	public static final String TUNING_TIME_INSTRUMENT = "TimeInstrument_Tuning_section_";

	// private static boolean bestoptions;
	public static final String INLINE_VALUE = "1";
	public static final String INDUCTION_VALUE = "3";
	public static final String PRIVATIZATION_VALUE = "2";
	public static final String PARALLELIZATION_VALUE = "1";
	public static final String REDUCTION_VALUE = "2";

	public static final String RANGE_VALUE = "1";
	public static final String ALIAS = "alias";
	public static final String INLINE = "tinline";
	public static final String INDUCTION = "induction";
	public static final String PRIVATIZATION = "privatize";
	public static final String PARALLELIZATION = "parallelize-loops";
	public static final String REDUCTION = "reduction";
	public static final String LOOP_INTERCHNAGE = "loop_interchange";
	public static final String RANGE = "range";
	public static final String DUMMY_OPT = "dummy_opt";
	public static final int TOTAL_OPTNMNMIZATIONS = 7;
	// test

	private static String nameApplication;
	private static boolean isPoly = false;

	private static String exeuctionTimes_id = "";
	private static Map<String, Technique> techniquesSection;
	private static Map<String, ArrayList<Transformation>> RENegativeSec;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// System.out.print("Please choose what you want to do: \n (1) Genetic Algorithm
		// \n (2) Exhaustive Search \n (3) Combine Elimnation");
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// String[] paramters = br.readLine().split(" ");
		// System.out.print("Please write the application you want to apply the
		// algorithm to: ");

		String algorithm = args[0];
		String benchMark = args[1];
		nameApplication = args[2];
		String className = args[3];
		String tuningFlag = args[4];
		String loopBegining = args[5];
		String windowsSize = args[6];
		String fileName = args[7];
		techniquesSection = new HashMap<String, Technique>();
		if (benchMark.toLowerCase().equals(POLY_BENCHMARK.toLowerCase())) {
			isPoly = true;
		}
		System.out.println(
				"Entry4: " + algorithm + " " + benchMark + " " + nameApplication + " " + className + " " + tuningFlag+ " " + loopBegining+ " " + windowsSize+ " " + fileName);

		if (algorithm.equals("3")) {

			combinedElimination(nameApplication, className, algorithm, benchMark, tuningFlag, loopBegining,
					windowsSize,fileName);
		}
		System.out.println("FINAL HOLA");

	}

	public static void exportInfo(String nameApplication, String results, String exportPath, String timeTaken,
			String className, String algorithm) {

		ProcessBuilder processBuilder = new ProcessBuilder(exportPath, nameApplication.toUpperCase(),
				nameApplication.toLowerCase(), results, timeTaken, className, algorithm);

		// processBuilder.directory(new File(path));
		try {
			Process process = processBuilder.start();
			process.waitFor();
			process.destroy();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static double valueSpeedUp(String nameApplication, String className, String algorithm,
			String procedureNameAndSection,
			String windowsSize) throws IOException {

		/* String currentDir = "/lustre/parot/users/2639/TuningSystemAll/TuningSystemJasonFinal-Section"; */
		/* String currentDir = "/work/parot/miguelrosas/Section_Level_Tuning/TuningSystemJasonFinal-Section"; */
		String currentDir = "/lustre/parot/MiguelRosas/Section_Level_Tuning/TuningSystemJasonFinal-Section";
		String benchmarkType ="";
		File file = null;
		if(isPoly){
			benchmarkType = "/PolyBenchC-4.2.1/"	;
		}
		else{
			benchmarkType = "/SNU_NPB-1.0.3/NPB3.3-SER-C/";
		}
		file = new File(currentDir + benchmarkType + nameApplication + "/logFiles/log_"
				+ nameApplication.toLowerCase() + "_" + className + "_" + algorithm + "_" + procedureNameAndSection
				+ "_w_"
				+ windowsSize + ".txt");

		BufferedReader br = new BufferedReader(new FileReader(file));
		String st = "";
		String result = "";
		while ((st = br.readLine()) != null) {
			// System.out.println(st);
			if (st.contains("TimeInstrument")) {
				String[] speedup = st.replace(" ", "").split("=");

				result = speedup[1];

			}
		}
		return Double.parseDouble(result);

	}

	public static ArrayList<Section> callCetus(String techniques, String Path, String nameApplication, String className,
			String algorithm, String windowsSize, String executionTimes_ID) {

		ArrayList<Section> sectionAndExecutionTime = new ArrayList<Section>();
		try {
			// Create a ProcessBuilder instance with the Python interpreter and the path to
			// the Python scripts
			String benchmark = "";
			if(isPoly)
			{
				benchmark = "Poly";
			}
			else {
				benchmark = "Nasa";
			}

			ProcessBuilder pb = new ProcessBuilder("python3", Path, benchmark, nameApplication.toUpperCase(),
					nameApplication.toLowerCase(), techniques, className, algorithm, windowsSize,
					executionTimes_ID);

			double defaultFitnessValue = 0.0;
			String[] procedures = executionTimes_ID.split(";");

			//System.out.println("RESULT PYTHON3: " + pb.toString());
			// Start the process
			Process process = pb.start();
			int exitCode = process.waitFor();

			BufferedReader readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			System.out.println("####ERROR");
			String errorline ="";
			while ((errorline = readerError.readLine()) != null)
			{
				System.out.println(errorline);
			}
			
			System.out.println("####ERROR¡EMD");
			System.out.println();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}

			for (int i = 0; i < procedures.length; i++) {
				String procedureNameAndSection = procedures[i];

				if (!procedureNameAndSection.equals("")) {
					defaultFitnessValue = valueSpeedUp(nameApplication, className, algorithm, procedureNameAndSection,
							windowsSize);
					Section section = new Section(procedureNameAndSection, defaultFitnessValue);
					sectionAndExecutionTime.add(section);

					System.out.println(procedureNameAndSection + " The Execution Time is: " + defaultFitnessValue);

				}

			}
			// Wait for the process to finish
			System.out.println("Python script exited with code " + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sectionAndExecutionTime;
	}

	public static void combinedElimination(String nameApplication, String className, String algorithm, String benchMark,
			String tuningFlag, String loopBegining, String windowsSize, String fileName) {

		String currentDir = System.getProperty("user.dir");
		String path = "";
		String pathAnswer = "";

	
		/* path = "/lustre/parot/users/2639/TuningSystemAll/TuningSystemJasonFinal-Section"
		+ "/bashFiles/python.py";  */

		path = "/lustre/parot/MiguelRosas/Section_Level_Tuning/TuningSystemJasonFinal-Section"
					+ "/bashFiles/combineElimination.py";
		pathAnswer = "/lustre/parot/MiguelRosas/Section_Level_Tuning/TuningSystemJasonFinal-Section"
					+ "/bashFiles/cetusAnswer.py";

			/* path = "/work/parot/miguelrosas/Section_Level_Tuning/TuningSystemJasonFinal-Section"
					+ "/bashFiles/combineElimination.py";
			pathAnswer = "/work/parot/miguelrosas/Section_Level_Tuning/TuningSystemJasonFinal-Section"
					+ "/bashFiles/cetusAnswer.py"; */


		String aliasNumber = "0";

		ArrayList<Procedure> procedures = readFileProcedureAndSections(fileName);


		
		ArrayList<Procedure> proceduresList_opti_eliminate = updateArrayOpt(procedures);

		ArrayList<String> visitedIdSection = visitedSectionIds(procedures);
		String techniques = createFlags(procedures, windowsSize, visitedIdSection);

		ArrayList<Section> defualtETSections = callCetus(techniques, path, nameApplication, className,
				algorithm, windowsSize, exeuctionTimes_id);

		RENegativeSec = createREperSection(procedures);

		// RENegativeSec = new HashMap<String, ArrayList<Transformation>>();

		long startTime = System.currentTimeMillis();

		ArrayList<Procedure> answer = secondV3(RENegativeSec, path, defualtETSections, nameApplication, className,
				algorithm, windowsSize,
				procedures, proceduresList_opti_eliminate);

		/* ArrayList<Procedure> answer = procedures; */

		String tuningFlagTechnique = "-alias=2 ";

		if (tuningFlag.equals(TUNING)) {
			tuningFlagTechnique += TUNING_FLAG;
		}

		long finalTime = System.currentTimeMillis();
		String timeTaken = (finalTime - startTime) + "";
		System.err.println("Time in millisec taken for the tunning: " + timeTaken);

		for (int i = 0; i < answer.size(); i++) {

			Procedure procedure = answer.get(i);
			ArrayList<Section> sections = procedure.getSections();

			for (int j = 0; j < sections.size(); j++) {

				Section section = sections.get(j);
				ArrayList<Technique> techniquesList = section.getTechniques();

				System.err.println("La section is : " + section.getSectionId() + " " + techniquesList.toString());
				writeAnswerFile(pathAnswer, nameApplication,
						techniquesList.toString() + " Windows Size: " + windowsSize,
						className, algorithm, windowsSize, section.getSectionId());
			}

		}

	}

	public static ArrayList<Procedure> updateArrayOpt(ArrayList<Procedure> procedures) {

		ArrayList<Procedure> proceduresNewArray = new ArrayList<Procedure>();

		for (int index = 0; index < procedures.size(); index++) {

			ArrayList<Section> listSectionsNew = updateSections(procedures.get(index).getSections());
			Procedure procedureEntity = new Procedure(procedures.get(index).getProcedureId(), listSectionsNew);
			proceduresNewArray.add(procedureEntity);

		}
		return proceduresNewArray;
	}

	public static ArrayList<Section> updateSections(ArrayList<Section> sectionsProcedure) {

		ArrayList<Section> sectionProcedureNewList = new ArrayList<Section>();

		for (int i = 0; i < sectionsProcedure.size(); i++) {

			ArrayList<Technique> techniques = updateTechniqueInSection(sectionsProcedure.get(i).getTechniques());
			Section sectionNew = new Section(sectionsProcedure.get(i).getSectionId(), techniques);
			sectionProcedureNewList.add(sectionNew);

		}

		return sectionProcedureNewList;

	}

	public static ArrayList<Technique> updateTechniqueInSection(ArrayList<Technique> techniquesSection) {
		ArrayList<Technique> techniquesSectionNewList = new ArrayList<Technique>();

		for (int i = 0; i < techniquesSection.size(); i++) {

			Technique techniqueNew = new Technique(techniquesSection.get(i).getName(),
					techniquesSection.get(i).getValue());
			techniquesSectionNewList.add(techniqueNew);

		}

		return techniquesSectionNewList;

	}

	public static ArrayList<Procedure> readFileProcedureAndSections(String fileName){
		String currentDir = System.getProperty("user.dir");
		String path = currentDir + "/"+fileName+".txt";

		ArrayList<Procedure> procedures = new ArrayList<Procedure>();
		ArrayList<Section> sections = new ArrayList<Section>();
		

		String functionName = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
			int sectionId = 0;
            while ((line = br.readLine()) != null) {

				if(line.contains("FUNCTION_NAME:")){
					String[] line_Function=line.split(":");
					functionName=line_Function[1];

				}
				else{
					if(line.contains("LOOP_ID:")){
						String[] line_Loop=line.split(";");
						String loopId = line_Loop[0].replace("LOOP_ID:", "");
						String originalLoopOrder = line_Loop[1].replace("ORIGINAL:", "");
						String desiredLoopOrder = line_Loop[2].replace("DESIRED:", "");
						Loop_reader loop_reader = new Loop_reader(loopId, functionName, originalLoopOrder, desiredLoopOrder);
						createSectionFromFile(loop_reader,  sections,  sectionId);
						sectionId++;
					}
				}
            }

			Procedure procedure = new Procedure(functionName, sections);
            procedures.add(procedure);

        } catch (IOException e) {
            e.printStackTrace();
        }

		return procedures;

	}

	public static void createSectionFromFile(Loop_reader loop_reader, ArrayList<Section> sections_exact_rhs, int sectionId) {
		
		String loopId = loop_reader.getLoopId();
		String procedureName = loop_reader.getProcedureName();

		ArrayList<Technique> techniques = new ArrayList<Technique>();
        createTechnique(techniques, loopId, loop_reader.getOriginalLoopOrder(),  procedureName);
        Section section = new Section("TimeInstrument_Tuning_section_"+procedureName+"_S"+sectionId, techniques);
        section.setFinish(false);
        sections_exact_rhs.add(section);

	}

	/* public static ArrayList<Procedure> readFileProcedureAndSections() {

		// cREATE A LIST WITH ALL LOOPS INSIDE
		// cREATE A LIST WITH ALL LOOPS INSIDE
		ArrayList<Procedure> procedures = new ArrayList<Procedure>();
		/////////////////// PROCEDURES /////////////////////////
		ArrayList<Section> sections_exact_rhs = new ArrayList<Section>();

        ArrayList<Technique> section00 = new ArrayList<Technique>();

        createTechnique(section00, "exact_rhs#0", "exact_rhs");
        Section section0 = new Section("TimeInstrument_Tuning_section_exact_rhs_S0", section00);
        section0.setFinish(false);
        sections_exact_rhs.add(section0);

        ArrayList<Technique> section01 = new ArrayList<Technique>();

        createTechnique(section01, "exact_rhs#1", "exact_rhs");
        Section section1 = new Section("TimeInstrument_Tuning_section_exact_rhs_S1", section01);
        section1.setFinish(false);
        sections_exact_rhs.add(section1);

        ArrayList<Technique> section02 = new ArrayList<Technique>();

        createTechnique(section02, "exact_rhs#2", "exact_rhs");
        Section section2 = new Section("TimeInstrument_Tuning_section_exact_rhs_S2", section02);
        section2.setFinish(false);
        sections_exact_rhs.add(section2);

        ArrayList<Technique> section03 = new ArrayList<Technique>();

        createTechnique(section03, "exact_rhs#3", "exact_rhs");
        Section section3 = new Section("TimeInstrument_Tuning_section_exact_rhs_S3", section03);
        section3.setFinish(false);
        sections_exact_rhs.add(section3);

        ArrayList<Technique> section04 = new ArrayList<Technique>();

        createTechnique(section04, "exact_rhs#4", "exact_rhs");
        Section section4 = new Section("TimeInstrument_Tuning_section_exact_rhs_S4", section04);
        section4.setFinish(false);
        sections_exact_rhs.add(section4);

        Procedure procedure = new Procedure("exact_rhs", sections_exact_rhs);
        procedures.add(procedure);
  
		/////////////////////////////////////////////////////////////////////

		return procedures;

	} */

	public static String createFlags(ArrayList<Procedure> procedures, String windowsSize,
			ArrayList<String> visitedIdSection) {

		String techniques = "-alias=2 " + TUNING_FLAG + "=" + windowsSize + ";";
		exeuctionTimes_id = "";
		for (int i = 0; i < procedures.size(); i++) {

			// Iterate over each entry in the HashMap

			String procedureName = procedures.get(i).getProcedureId();

			techniques += procedureName + ">";

			int sectionsProcedure = procedures.get(i).getSections().size();

			for (int j = 0; j < sectionsProcedure; j++) {

				String sectionIDD = "S" + j;

				Section section = procedures.get(i).getSections().get(j);

				String unionVisited = procedureName + "_" + sectionIDD;
				if (!visitedIdSection.contains(unionVisited) && !section.isFinish()) {

					techniques += sectionIDD + "<[";

					if (!section.isFinish()) {
						exeuctionTimes_id += TUNING_TIME_INSTRUMENT + procedureName + "_" + "S" + j;
						if (!(procedures.size() == i + 1 && j + 1 == sectionsProcedure)) {
							exeuctionTimes_id += ";";
						}

					}

					for (int index = 0; index < section.getTechniques().size(); index++) {
						int position = index;

						String[] loopIdName_and_technique_name = section.getTechniques().get(index).getName()
								.split("%");

						String loopId = loopIdName_and_technique_name[0];
						// String techiniqueName = loopIdName_and_technique_name[1];
						if (!techniques.contains(loopId)) {
							techniques += loopId + "%";
						}
						techniques += section.getTechniques().get(index).toString().replace(loopId + "%", "");
						if ((position + 1) % TOTAL_OPTNMNMIZATIONS != 0) {
							techniques += ",";
						} else if (index != section.getTechniques().size() - 1) {
							techniques += "&";
						}

					}

					if (j != (sectionsProcedure - 1)) {
						techniques += "]/";
					} else {
						techniques += "]";
					}
				}

			}

			if (i != procedures.size() - 1) {
				techniques += ";";
			}

		}
		/*
		 * if(techniques.endsWith("/;;;")){
		 * techniques=techniques.replace("/;;;", ";");
		 * }
		 */
		System.out.println(techniques);
		return techniques;

	}

	public static ArrayList<Technique> createTechnique(ArrayList<Technique> lisTechniquesSection, String loopId,
			String originalLoopOrder, String procedureName) {

		Technique alias = new Technique(loopId + "%" + "alias", "2");
		alias.setVisited(true);
		Technique inline = new Technique(loopId + "%" + "tinline", INLINE_VALUE, procedureName);
		inline.setVisited(false);
		Technique induction = new Technique(loopId + "%" + "induction", INDUCTION_VALUE);
		induction.setVisited(false);
		Technique privatization = new Technique(loopId + "%" + "privatize", PRIVATIZATION_VALUE);
		privatization.setVisited(false);
		Technique parallelization = new Technique(loopId + "%" + "parallelize-loops", PARALLELIZATION_VALUE);
		parallelization.setVisited(false);
		Technique reduction = new Technique(loopId + "%" + "reduction", REDUCTION_VALUE);
		reduction.setVisited(false);
		Technique loopInterchange = new Technique(loopId + "%" + "loop_interchange", originalLoopOrder);
		loopInterchange.setVisited(false);
		/* Technique range = new Technique(loopId + "%" + "range", RANGE_VALUE);
		range.setVisited(false); */

		lisTechniquesSection.add(alias);
		lisTechniquesSection.add(inline);
		lisTechniquesSection.add(induction);
		lisTechniquesSection.add(privatization);
		lisTechniquesSection.add(parallelization);
		lisTechniquesSection.add(reduction);
		lisTechniquesSection.add(loopInterchange);
		/* lisTechniquesSection.add(range); */
		return lisTechniquesSection;

	}

	public static ArrayList<String> windowsSize(int Wsize) {

		ArrayList<String> listOptimizations = new ArrayList<String>();
		for (int i = 0; i <= Wsize; i++) {
			String optAlias = "alias#" + i;
			String inline = "tinline";
			String optInduction = "induction#" + i;
			String optPrivatization = "privatize#" + i;
			String optParallelize = "parallelize-loops#" + i;
			String optReduction = "reduction#" + i;
			String optLoopInter = "loop_interchange#" + i;
			String optRange = "range#" + i;
			listOptimizations.add(optAlias);
			listOptimizations.add(inline);
			listOptimizations.add(optInduction);
			listOptimizations.add(optPrivatization);
			listOptimizations.add(optParallelize);
			listOptimizations.add(optReduction);
			listOptimizations.add(optLoopInter);
			listOptimizations.add(optRange);
		}

		return listOptimizations;

	}

	public static void writeAnswerFile(String Path, String nameApplication, String answer,
			String ClassName, String algorithm, String windowsSize, String sectionId) {

		ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", Path, nameApplication.toUpperCase(),
				nameApplication.toLowerCase(), answer, ClassName, algorithm, windowsSize,
				sectionId);
		try {
			Process process = processBuilder.start();
			process.waitFor();
			process.destroy();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Procedure> secondV3(Map<String, ArrayList<Transformation>> RENegativeSec, String path,
			ArrayList<Section> defualtETSections,
			String nameApplication,
			String className, String algorithm, String windowsSize,
			ArrayList<Procedure> procedures, ArrayList<Procedure> proceduresList_opti_eliminate) {

		// System.out.println(listTechniqueWithValues.toString());

		// ArrayList<Procedure> proceduresTreeCopy = new ArrayList<Procedure>();
		// proceduresTreeCopy.addAll(procedures);

		boolean allSectionCompleDone = allSectionCompleted(procedures);

		if (!allSectionCompleDone) {

			Map<String, Technique> sectionTechinque = new HashMap<String, Technique>();
			for (int i = 0; i < procedures.size(); i++) {

				ArrayList<Section> sectionsProcedure = procedures.get(i).getSections();
				for (int j = 0; j < sectionsProcedure.size(); j++) {

					Section sectionPro = sectionsProcedure.get(j);
					// is it really disabeling the technique?
					if (!sectionPro.isFinish()) {
						Technique techDIsable = disableOptimizations(sectionPro);
						if (techDIsable != null) {
							// String sectionProcedureName = getSectionIdRunTime(procedureName,
							// sectionPro.getSectionId());
							sectionTechinque.put(sectionPro.getSectionId(), techDIsable);
						}

					}

				}

			}

			ArrayList<String> visitedIdSection = visitedSectionIds(procedures);

			String techniquesFlags = createFlags(procedures, windowsSize, visitedIdSection);

			ArrayList<Section> Section_techiquesET = callCetus(techniquesFlags, path,
					nameApplication,
					className, algorithm, windowsSize, exeuctionTimes_id);

			//--------------------------------------------------------------------------
			//--------------disable the technique after running-------------------------
			for (Map.Entry<String, Technique> entry : sectionTechinque.entrySet()) {
				String sectionId = entry.getKey();
				Technique sectionTargetSection = entry.getValue();
				disableOpt(procedures, sectionId,  sectionTargetSection);
			}
			//--------------------------------------------------------------------------
			for (int j = 0; j < Section_techiquesET.size(); j++) {

				String sectionName = Section_techiquesET.get(j).getSectionId();
				double IndividualTechniqueET = Section_techiquesET.get(j).getExecutionTime();
				double defualtET = -1;
				for (int index = 0; index < defualtETSections.size(); index++) {
					if (defualtETSections.get(index).getSectionId().equals(sectionName)) {
						defualtET = defualtETSections.get(index).getExecutionTime();
						break;
					}
				}

				double RE_Section_technique = relativeImprovementPercentage(IndividualTechniqueET, defualtET);

				System.out
						.println("Section Techniques: " + sectionName + " " + IndividualTechniqueET + " " + defualtET
								+ " "
								+ RE_Section_technique);

				// There is a case where we have visited all the nodes, for example with ws 1
				if (RE_Section_technique < 0 && sectionTechinque.get(sectionName) != null) {

					ArrayList<Transformation> RENegative_individual = RENegativeSec.get(sectionName);
					Technique techniqueNegativeImpact = sectionTechinque.get(sectionName);
					String techniqueName = techniqueNegativeImpact.getName();
					Transformation technique = new Transformation(techniqueName, RE_Section_technique, IndividualTechniqueET );
					RENegative_individual.add(technique);
					// i t has to be with loopId and not sectionaName
					RENegativeSec.put(sectionName, RENegative_individual);
					// RENegative_individual.put(RE_Section_technique, techniqueName);
				}

			}

			// EnableTechniques
			for (Map.Entry<String, Technique> sectionTech : sectionTechinque.entrySet()) {
				String sectionId = sectionTech.getKey();
				Technique techni = sectionTech.getValue();
				String[] ProcedureAndSection = sectionId.split("TimeInstrument_Tuning_section_");
				String procedureId = ProcedureAndSection[1].substring(0, ProcedureAndSection[1].length() - 3);
				/*
				 * String section =
				 * ProcedureAndSection[1].substring(ProcedureAndSection[1].length() - 2,
				 * ProcedureAndSection[1].length());
				 */
				enableSpecificTechiques(procedures, procedureId, sectionId, techni.getName());
				// Process key-value pair
			}
			boolean visitedAll = allVisitedTree(procedures);

			if (!visitedAll) {
				// enableSpecificTechnqiues(sectionTechinque);
				secondV3(RENegativeSec, path,
						defualtETSections,
						nameApplication,
						className, algorithm, windowsSize,
						procedures, proceduresList_opti_eliminate);

			} else {
				System.out.println("all visited");
				setVisitedFalseAgain(procedures);

			}

			// recursive
			// Each secsion will have Transformations with negative impact
			boolean allSectionComple = allSectionCompleted(procedures);
			if (!allSectionComple) {

				System.out.println("RENegativeSec != 0");
				for (Map.Entry<String, ArrayList<Transformation>> reNegativeAllSetions : RENegativeSec.entrySet()) {
					String sectionId = reNegativeAllSetions.getKey();
					ArrayList<Transformation> reNegative_IndividualSection_list = reNegativeAllSetions.getValue();
					if (reNegative_IndividualSection_list.size() != 0) {

						SortRE_List_section(reNegative_IndividualSection_list);

					} else {
						finishSection(procedures, sectionId);
						System.out.println("This section is done tuning: " + sectionId);
						Section sectionFound = findSection(procedures, sectionId);
						System.out.println("Section FInished : "+sectionId + " Solution: "+sectionFound.toString());

					}
					// Process key-value pair
				}

				allSectionComple = allSectionCompleted(procedures);
				if (!allSectionComple) {
					ThirdNew(RENegativeSec, procedures, windowsSize, path, className, algorithm,
							proceduresList_opti_eliminate);

				}

			}
		}
		return procedures;
	}


	public static void disableOpt(ArrayList<Procedure> procedures, String sectionTargetSection, Technique TargetTechnique) {

		for(int i = 0 ; i< procedures.size(); i++)
		{
			ArrayList<Section> sections = procedures.get(i).getSections();

			for(int j =0 ; j< sections.size() ; j++)
			{
				String sectionId = sections.get(j).getSectionId();
				if(sectionId.equals(sectionTargetSection)){

					ArrayList<Technique> techniques = sections.get(j).getTechniques();
					for(int k =0 ; k< techniques.size(); k++)
					{
						String techniqueName = techniques.get(k).getName();
						if(techniqueName.equals(TargetTechnique.getName())){
							techniques.get(k).setVisited(true);	
						}
					}
				
				}
			}
		}		

	}

	public static Section findSection(ArrayList<Procedure> procedures, String sectionIdLook){

		boolean found = false;

		for(int i =0; i< procedures.size() && !found; i++)
		{
			ArrayList<Section> sections = procedures.get(i).getSections();

			for(int j =0 ; j< sections.size() ; j++)
			{
				String sectionId = sections.get(j).getSectionId();
				if(sectionId.equals(sectionIdLook)){
					found = true;
					return sections.get(j);
				}



			}

		}

		return null;


	}

	public static boolean allSectionCompleted(ArrayList<Procedure> procedures) {

		boolean allCompleted = true;

		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);
			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);

				if (!section.isFinish()) {
					allCompleted = false;
					System.out.println("Sections not completed because of : " + section.getSectionId());

				}

			}

		}
		return allCompleted;

	}

	public static void ThirdNew(Map<String, ArrayList<Transformation>> reNegativeAllSetions,
			ArrayList<Procedure> procedures, String windowsSize, String path, String className, String algorithm,
			ArrayList<Procedure> proceduresList_opti_eliminate) {

		// EACH SECTION MUST HAVE ALIST OF TECHNIQUES
		HashMap<String, Transformation> deletedTechniques = new HashMap<String, Transformation>();

		for (Map.Entry<String, ArrayList<Transformation>> reNegativeSection : reNegativeAllSetions.entrySet()) {
			String sectionId = reNegativeSection.getKey();
			ArrayList<Transformation> reNegative_IndividualSection_list = reNegativeSection.getValue();
			if (reNegative_IndividualSection_list.size() != 0) {

				// Sort the Array of RE
				Transformation transformationHighestIMpact = reNegative_IndividualSection_list.get(0);

				System.out.println("Dtelete array: " + sectionId);
				deletedTechniques.put(sectionId, transformationHighestIMpact);
				deleteTehcniqueFromOptimizationsDummyList(proceduresList_opti_eliminate, transformationHighestIMpact);
				// reNegative_IndividualSection_list.re>move(0);
				// reNegativeAllSetions.put(sectionId, reNegative_IndividualSection_list);

			}
			// Process key-value pair
		}

		// print
		System.out.println("Bfeore editing the procedure in Third\n");
		for (Map.Entry<String, Transformation> reNegativeSection : deletedTechniques.entrySet()) {
			String sectionId = reNegativeSection.getKey();
			Transformation deletedTech = reNegativeSection.getValue();
			System.out.println(sectionId + " : " + deletedTech.getName());

		}

		//

		editProcedureTree(procedures, deletedTechniques);
		ArrayList<String> visitedIdSection = visitedSectionIds(procedures);

		System.out.println("VISITED SECTIONS IN THIRD STEP -second\n");
		System.out.println(visitedIdSection.toString() + "\n");

		String techniquesFlags = createFlags(procedures, windowsSize, visitedIdSection);

		ArrayList<Section> Section_techiques_Default_ET = callCetus(techniquesFlags, path,
				nameApplication,
				className, algorithm, windowsSize, exeuctionTimes_id);

		HashMap<String, Transformation> deletedTechniquesSecond = new HashMap<String, Transformation>();

		for (Map.Entry<String, ArrayList<Transformation>> reNegativeSection : reNegativeAllSetions.entrySet()) {
			String sectionId = reNegativeSection.getKey();
			ArrayList<Transformation> trasnformationRENegativeList = reNegativeSection.getValue();
			Section sectionDefaultET = findSectionExecutionTime(Section_techiques_Default_ET, sectionId);

			for (int i = 1; i < trasnformationRENegativeList.size(); i++) {
				Transformation transformationReNegat = trasnformationRENegativeList.get(i);


				
				double defaultExecutionTime_section = sectionDefaultET.getExecutionTime();
			    /*double defaultExecutionTime_section = deletedTechniques.get(sectionId).getValue();*/
				double RE_Section_technique = relativeImprovementPercentage(transformationReNegat.getExecutionTime(),
						defaultExecutionTime_section);
						
				if (RE_Section_technique < 0) {
					deleteTehcniqueFromOptimizationsDummyList(proceduresList_opti_eliminate, transformationReNegat);
					deletedTechniquesSecond.put(sectionId, transformationReNegat);
				}
			}

		}
		editProcedureTree(procedures, deletedTechniquesSecond);

		/* ArrayList<String> visitedIdSection = visitedSectionIds(procedures); */
		ArrayList<String> visitedIdSectionTwo = visitedSectionIds(procedures);

		System.out.println("VISITED SECTIONS IN THIRD STEP -second\n");
		System.out.println(visitedIdSectionTwo.toString() + "\n");

		techniquesFlags = createFlags(procedures, windowsSize, visitedIdSectionTwo);

		Section_techiques_Default_ET = callCetus(techniquesFlags, path,
				nameApplication,
				className, algorithm, windowsSize, exeuctionTimes_id);

		// Where should I actually add this?
		RENegativeSec = createREperSection(procedures);
		/////////////////////////////////

		secondV3(RENegativeSec, path,
				Section_techiques_Default_ET,
				nameApplication,
				className, algorithm, windowsSize,
				procedures, proceduresList_opti_eliminate);

	}

	public static void setVisitedFalseAgain(ArrayList<Procedure> procedures) {
		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);
			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);
				ArrayList<Technique> listTechniques = section.getTechniques();
				if (!section.isFinish()) {
					for (int j = 0; j < listTechniques.size(); j++) {

						Technique technique = section.getTechniques().get(j);

						if (!technique.getName().contains(ALIAS) && !technique.getName().contains(PRIVATIZATION) && !technique.getValue().equals("0")) {
							System.out.println("La techniqueca es : " + technique.getName());
							technique.setVisited(false);
						}
					}
				}

			}

		}

	}

	public static void finishSection(ArrayList<Procedure> procedures, String sectionId) {
		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);
			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);

				if (section.getSectionId().equals(sectionId)) {
					section.setFinish(true);
					for (int j = 0; j < section.getTechniques().size(); j++) {
						Technique technique = section.getTechniques().get(j);
						technique.setVisited(true);
					}
					System.out.println("finish: " + section.getSectionId());
					break;
				}

			}

		}

	}

	public static void enableSpecificTechiques(ArrayList<Procedure> procedures, String procedureId, String sectionId,
			String techniqque) {

		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);
			if (procedure.getProcedureId().equals(procedureId)) {
				for (int i = 0; i < procedure.getSections().size(); i++) {

					Section section = procedure.getSections().get(i);
					if (section.getSectionId().equals(sectionId)) {
						ArrayList<Technique> listTechniques = section.getTechniques();
						for (int j = 0; j < listTechniques.size(); j++) {

							Technique techniqueName = section.getTechniques().get(j);

							if (techniqueName.getName().equals(techniqque)) {
								if (techniqueName.getName().contains(INLINE)) {
									techniqueName.setValue(INLINE_VALUE);
								} else if (techniqueName.getName().contains(INDUCTION)) {
									techniqueName.setValue(INDUCTION_VALUE);
								} else if (techniqueName.getName().contains(PRIVATIZATION)) {
									techniqueName.setValue(PRIVATIZATION_VALUE);
								} else if (techniqueName.getName().contains(PARALLELIZATION)) {
									techniqueName.setValue(PARALLELIZATION_VALUE);
								} else if (techniqueName.getName().contains(REDUCTION)) {
									techniqueName.setValue(REDUCTION_VALUE);
								} else if (techniqueName.getName().contains(LOOP_INTERCHNAGE)) {
									techniqueName.setValue(techniqueName.getOriginalLoopOrder());
								} else if (techniqueName.getName().contains(RANGE)) {
									techniqueName.setValue(RANGE_VALUE);
								}
								break;
							}
						}

					}

				}

			}

		}

	}

	/*
	 * public static void enableSpecificTechnqiues(Map<String, Technique>
	 * sectionTechinque) {
	 * 
	 * for (Map.Entry<String, Technique> reNegativeSection :
	 * sectionTechinque.entrySet()) {
	 * 
	 * Technique techniqueEnable = reNegativeSection.getValue();
	 * String techniqueName = techniqueEnable.getName();
	 * if (techniqueName.contains(INLINE)) {
	 * techniqueEnable.setValue(INLINE_VALUE);
	 * } else if (techniqueName.equals(INDUCTION)) {
	 * techniqueEnable.setValue(INDUCTION_VALUE);
	 * } else if (techniqueName.equals(PRIVATIZATION)) {
	 * techniqueEnable.setValue(PRIVATIZATION_VALUE);
	 * } else if (techniqueName.equals(PARALLELIZATION)) {
	 * techniqueEnable.setValue(PARALLELIZATION_VALUE);
	 * } else if (techniqueName.equals(REDUCTION)) {
	 * techniqueEnable.setValue(REDUCTION_VALUE);
	 * } else if (techniqueName.equals(LOOP_INTERCHNAGE)) {
	 * techniqueEnable.setValue(LOOP_INTERCHANGE_VALUE);
	 * } else if (techniqueName.equals(RANGE)) {
	 * techniqueEnable.setValue(RANGE_VALUE);
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	public static Section findSectionExecutionTime(ArrayList<Section> Section_techiques_Default_ET, String SectionId) {
		Section section = null;
		for (int i = 0; i < Section_techiques_Default_ET.size(); i++) {
			Section sectionInList = Section_techiques_Default_ET.get(i);
			if (sectionInList.getSectionId().equals(SectionId)) {
				section = sectionInList;
			}
		}
		return section;
	}

	public static void deleteTehcniqueFromOptimizationsDummyList(ArrayList<Procedure> proceduresList_opti_eliminate,
			Transformation transformation) {

		for (int index = 0; index < proceduresList_opti_eliminate.size(); index++) {
			Procedure procedure = proceduresList_opti_eliminate.get(index);

			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);
				ArrayList<Technique> listTechniques = section.getTechniques();
				for (int j = 0; j < listTechniques.size(); j++) {

					Technique technique = section.getTechniques().get(j);

					if (technique.getName().equals(transformation.getName())) {
						listTechniques.remove(technique);
						break;
					}
				}

			}

		}
	}

	public static boolean allVisitedTree(ArrayList<Procedure> procedures) {

		boolean visited = true;

		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);

			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);

				for (int j = 0; j < section.getTechniques().size(); j++) {

					Technique technique = section.getTechniques().get(j);

					if (technique.getVisited() == false) {
						System.out.println("It has not been visited :" + section.getSectionId() + " Techique :"
								+ technique.getName());
						visited = false;
						break;
					}
				}

			}

		}

		return visited;

	}

	public static ArrayList<String> visitedSectionIds(ArrayList<Procedure> procedures) {

		ArrayList<String> idsSections = new ArrayList<String>();
		for (int index = 0; index < procedures.size(); index++) {
			Procedure procedure = procedures.get(index);

			for (int i = 0; i < procedure.getSections().size(); i++) {

				Section section = procedure.getSections().get(i);
				boolean visited = visitIndividualSection(section);
				if (visited) {
					String[] sectionId = section.getSectionId().split("TimeInstrument_Tuning_section_");
					idsSections.add(sectionId[1]);

				}

			}

		}

		return idsSections;
	}

	public static boolean visitIndividualSection(Section section) {

		boolean visited = true;
		for (int j = 0; j < section.getTechniques().size(); j++) {

			Technique technique = section.getTechniques().get(j);

			if (technique.getVisited() == false) {
				visited = false;
				break;
			}
		}

		return visited;
	}

	public static void editProcedureTree(ArrayList<Procedure> procedures,
			HashMap<String, Transformation> deletedTechniques) {

		for (int i = 0; i < procedures.size(); i++) {

			String procedureName = procedures.get(i).getProcedureId();

			ArrayList<Section> sectionsProcedure = procedures.get(i).getSections();
			for (int j = 0; j < sectionsProcedure.size(); j++) {

				Section section = sectionsProcedure.get(j);
				// String idProcedure_and_section = TUNING_TIME_INSTRUMENT + procedureName + "_"
				// + section.getSectionId();

				Transformation techniqueDeleted = deletedTechniques.get(section.getSectionId());
				if (techniqueDeleted != null) {
					System.out.println("Procedure and Section edited to 0: " + section.getSectionId());
					for (int index = 0; index < section.getTechniques().size(); index++) {

						Technique tech = section.getTechniques().get(index);
						if (tech.getName().equals(techniqueDeleted.getName())) {
							System.out.println("Techique: " + techniqueDeleted.getName());
							tech.setValue("0");
							tech.setVisited(true);
						}

					}
				}

			}

		}

	}

	public static String getSectionIdRunTime(String procedureName, String sectionId) {
		String[] sectionProcedureName = exeuctionTimes_id.split(";");
		String foundSection = "";
		for (int index = 0; index < sectionProcedureName.length; index++) {
			String section = sectionProcedureName[index];
			if (section.equals(sectionId)) {
				foundSection = section;
				break;
			}
		}

		return foundSection;
	}

	public static Technique disableOptimizations(Section section) {

		Technique techniqueSection = new Technique();
		for (int i = 1; i < section.getTechniques().size(); i++) {

			Technique technique = section.getTechniques().get(i);

			if (technique.getVisited() == false && !technique.getValue().equals("0")) {

				technique.setValue("0");
				//technique.setVisited(true);
				techniqueSection = technique;
				return techniqueSection;
			}

		}

		return null;

	}

	/* public static Technique EnableOptimizations(Section section) {

		Technique techniqueSection = new Technique();
		for (int i = 1; i < section.getTechniques().size(); i++) {

			Technique technique = section.getTechniques().get(i);

			if (technique.getVisited() == false && technique.getValue() == "0") {

				if (technique.getName().contains(INLINE)) {
					technique.setValue(INLINE_VALUE);
				} else if (technique.getName().contains(INDUCTION)) {
					technique.setValue(INDUCTION_VALUE);
				} else if (technique.getName().contains(PRIVATIZATION)) {
					technique.setValue(PRIVATIZATION_VALUE);
				} else if (technique.getName().contains(PARALLELIZATION)) {
					technique.setValue(PARALLELIZATION_VALUE);
				} else if (technique.getName().contains(REDUCTION)) {
					technique.setValue(REDUCTION_VALUE);
				} else if (technique.getName().contains(LOOP_INTERCHNAGE)) {
					technique.setValue(LOOP_INTERCHANGE_VALUE);
				} else if (technique.getName().contains(RANGE)) {
					technique.setValue(RANGE_VALUE);
				}
				technique.setVisited(true);
				techniqueSection = technique;
				return techniqueSection;
			}

		}

		return null;

	} */

	public static Map<String, ArrayList<Transformation>> createREperSection(ArrayList<Procedure> procedures) {

		Map<String, ArrayList<Transformation>> RENegativeSections = new HashMap<String, ArrayList<Transformation>>();

		for (int i = 0; i < procedures.size(); i++) {

			Procedure procedure = procedures.get(i);
			String procedureName = procedure.getProcedureId();

			for (int j = 0; j < procedure.getSections().size(); j++) {

				Section section = procedure.getSections().get(j);

				if (!section.isFinish()) {
					String sectionId = section.getSectionId();
					// String procedureNameAndSectionId = TUNING_TIME_INSTRUMENT + procedureName +
					// "_" + sectionId;
					ArrayList<Transformation> RENegative_individual = new ArrayList<Transformation>();
					RENegativeSections.put(sectionId, RENegative_individual);

				}

			}

		}

		return RENegativeSections;

	}

	/* public static String techniquesCompleted(ArrayList<Technique> listTechniqueWithValues) {
		String answer = "";

		for (int i = 0; i < listTechniqueWithValues.size(); i++) {
			Technique technique = listTechniqueWithValues.get(i);

			if (technique.getName().startsWith("range#") && i + 1 == listTechniqueWithValues.size()) {
				answer += technique.getName() + "=" + technique.getValue();
			} else {
				answer += technique.toString() + ",";
			}

			if (technique.getName().startsWith("range#") && i + 1 < listTechniqueWithValues.size()) {
				answer += "&";
			}

		}

		return answer;

	} */

	/* public static String flagsEnableCetus(ArrayList<Technique> listTechniqueWithValues, String tuningFlag,
			String loopBegining,
			String windowsSize) {
		String TechniquesComplete = techniquesCompleted(listTechniqueWithValues);
		String TechniqueCompletedNoSpace = TechniquesComplete.replace(" ", "");
		String techniquesAll = tuningFlag + "=" + loopBegining + ";" + windowsSize + ";" + TechniqueCompletedNoSpace
				+ "";
		return techniquesAll;
	} */

	public static void delete(String ETtechniqueName, ArrayList<String> eachTechnique) {
		for (int i = 0; i < eachTechnique.size(); i++) {
			if (eachTechnique.get(i).equals(ETtechniqueName)) {
				eachTechnique.remove(i);
			}
		}
	}

	public static double relativeImprovementPercentage(double techiqueET, double defaultET) {

		double result = ((techiqueET - defaultET) / defaultET) * 100;
		return result;

	}

	public static double relativeImprovementPercentageGenetic(double defaultET, double techiqueET) {

		double result = ((defaultET - techiqueET) / defaultET) * 100;
		return result;

	}

	// Sorting asscending
	public static List<Transformation> SortRE_List_section(
			ArrayList<Transformation> reNegative_IndividualSection_list) {

		Collections.sort(reNegative_IndividualSection_list, new Comparator<Transformation>() {
			@Override
			public int compare(Transformation t1, Transformation t2) {
				// Sort in ascending order
				return Double.compare(t1.getRelativeImprovement(), t2.getRelativeImprovement());
			}
		});

		return reNegative_IndividualSection_list;
	}

}