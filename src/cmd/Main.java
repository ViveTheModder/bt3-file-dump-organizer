package cmd;
//BT3 File Dump Organizer v1.0 by ViveTheModder
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main 
{
	static final String[] CHARA_SUFFIX = 
	{"1p","1p_dmg","2p","2p_dmg","3p","3p_dmg","4p","4p_dmg","anm","eff","img","tag_P1","tag_P2","Voice_US","Voice_JP"};
	static final String[] LANG_OPTION = {"DE","FR","IT","JP","SP","UK","UP","US"};
	static final String[] GSC_PREFIX = {"GSC","LPS","TXT","VIC"};
	static String absPath, extension, fileName, fileNameNoExt, output="";
	static String[] afsNames = {"PZS3US1","PZS3US2"};
	public static void checkCharaFiles(File dir) throws IOException
	{
		if (!extension.toLowerCase().equals("adx"))
		{
			for (int i=0; i<CHARA_SUFFIX.length; i++)
			{
				if (fileNameNoExt.toLowerCase().contains("map") || fileNameNoExt.contains("chara")) continue;
				if (fileNameNoExt.endsWith(CHARA_SUFFIX[i]))
				{
					String charaName = fileName.replace(CHARA_SUFFIX[i]+"."+extension, "");
					charaName = charaName.substring(0,charaName.length()-1);
					String absCharaPath = absPath.replace("/PZS3US1", "/Characters/");
					new File(absCharaPath).mkdir();
					File newFolderRef = new File(absCharaPath+charaName);
					if (!newFolderRef.exists())
					{
						newFolderRef.mkdir();
						output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
						+"New folder named "+'"'+charaName+'"'+" created!\n";
					}
					output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
					+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
					Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
				}
			}
		}
		else
		{
			String charaName = fileName;
			charaName = charaName.substring(0,charaName.length()-7);
			char[] charaNameArray = charaName.toCharArray();
			if (charaNameArray[charaNameArray.length-3]=='5' && (fileNameNoExt.endsWith("US") || fileNameNoExt.endsWith("JP")))
			{
				String absCharaPath = absPath.replace("/PZS3US2", "/Characters/");
				new File(absCharaPath).mkdir();
				File newFolderRef = new File(absCharaPath+'/'+charaName.substring(0,charaName.length()-4)+"/ADX/");
				if (!newFolderRef.exists())
				{
					newFolderRef.mkdir();
					output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
					+"New ADX subfolder added to "+'"'+charaName+'"'+" folder!\n";
				}
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
				Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
			}
		}
	}
	public static void checkGSCFiles(File dir, int afsIndex) throws IOException
	{
		String prefix = fileNameNoExt.substring(0,3);
		String[] fileNameArray = fileNameNoExt.split("-");
		String absGscPath = absPath.replace('/'+afsNames[afsIndex], "/Scenarios/");
		new File(absGscPath).mkdir();
		int index = Arrays.binarySearch(GSC_PREFIX, prefix);
		int gscIndex=0;
		if (index>=0)
		{
			if (prefix.equals("VIC")) gscIndex=Integer.parseInt(fileNameArray[1]);
			else if (prefix.equals("TXT") || prefix.equals("LPS")) gscIndex=Integer.parseInt(fileNameArray[3]);
			else if (prefix.equals("GSC")) gscIndex=Integer.parseInt(fileNameArray[2]);
			File newFolderRef = new File(absGscPath+"GSC-B-"+gscIndex);
			if (afsIndex==0)
			{
				if (!newFolderRef.exists())
				{
					newFolderRef.mkdir();
					output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
					+"New folder named "+'"'+"GSC-B-"+gscIndex+'"'+" created!\n";
				}
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
				Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
			}
			else
			{
				File subfolderRef = new File(absGscPath+"GSC-B-"+gscIndex+"/ADX/");
				subfolderRef.mkdir();
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"New ADX subfolder added to "+'"'+"GSC-B-"+gscIndex+'"'+" folder!\n";
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
				Files.move(dir.toPath(), subfolderRef.toPath().resolve(fileName));
			}
		}
		else
		{
			if (extension.toLowerCase().equals("cdbt"))
			{
				if (prefix.equals("Dra") && prefix.contains("preview")) //get index from scenario portrait
					gscIndex=Integer.parseInt(fileNameNoExt.substring(fileNameNoExt.length()-2));
				File newFolderRef = new File(absGscPath+"GSC-B-"+gscIndex);
				if (!newFolderRef.exists())
				{
					newFolderRef.mkdir();
					output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
					+"New folder named "+'"'+"GSC-B-"+gscIndex+'"'+" created!\n";
				}
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
				Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
			}
		}
	}
	public static void checkMapFiles(File dir, int afsIndex) throws IOException
	{
		String fileNameNoExtLowercase = fileNameNoExt.toLowerCase();
		if (fileNameNoExtLowercase.contains("map"))
		{
			String mapName = fileNameNoExtLowercase;
			if (fileNameNoExtLowercase.startsWith("map")) mapName = mapName.substring(0,6);
			else if (fileNameNoExtLowercase.startsWith("se_map")) mapName = mapName.substring(3);
			else if (fileNameNoExtLowercase.startsWith("mapse")) mapName = mapName.substring(0,5);
			String absMapPath = absPath.replace('/'+afsNames[afsIndex], "/Maps/");
			File newFolderRef = new File(absMapPath+mapName);
			if (!newFolderRef.exists())
			{
				newFolderRef.mkdir();
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"New folder named "+'"'+mapName+'"'+" created!\n";
			}
			output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
			+"Moving "+'"'+fileName+'"'+" to "+'"'+mapName+'/'+newFolderRef.getName()+'"'+" folder...\n";
			Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
		}
	}
	public static void checkMenuFiles(File dir, int afsIndex) throws IOException
	{
		String language = fileNameNoExt.substring(fileNameNoExt.length()-2);
		int langIndex = Arrays.binarySearch(LANG_OPTION, language);
		String absMenuPath = absPath.replace('/'+afsNames[afsIndex], "/Menus/");
		new File(absMenuPath).mkdir();

		if (langIndex>=0)
		{
			String menuName = fileNameNoExt.substring(0, fileNameNoExt.length()-3);
			menuName = fileNameNoExt.replaceAll("\\d", ""); //remove digits
			menuName = menuName.replace(language, "");
			menuName = menuName.substring(0, menuName.length()-1); //remove last underscore
			//if underscores somehow still show up at the end, add ADX at the end too
			if (menuName.endsWith("_")) menuName = menuName.substring(0, menuName.length()-1)+"_ADX";
			//once again, just to be sure, I want to get rid of consecutive underscores
			menuName = menuName.replace("__", "_");
			
			File newFolderRef = new File(absMenuPath+menuName);
			if (!newFolderRef.exists())
			{
				newFolderRef.mkdir();
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"New folder named "+'"'+menuName+'"'+" created!\n";
			}
			output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
			+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
			Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
		}
		else //no language suffix found
		{
			if (extension.equals("cdbt") || extension.equals("cpak")) //look for compressed files
			{
				String cmpFileName = fileNameNoExt.split("_")[0];
				File newFolderRef = new File(absMenuPath+cmpFileName);
				if (!newFolderRef.exists())
				{
					newFolderRef.mkdir();
					output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
					+"New folder named "+'"'+cmpFileName+'"'+" created!\n";
				}
				output+=new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+": "
				+"Moving "+'"'+newFolderRef.getPath()+'"'+" folder...\n";
				Files.move(dir.toPath(), newFolderRef.toPath().resolve(fileName));
			}
		}
	}
	public static void setErrorLog(Exception e)
	{
		File errorLog = new File("errors.log");
		try {
			FileWriter logWriter = new FileWriter(errorLog,true);
			logWriter.append(new SimpleDateFormat("dd-MM-yy-hh-mm-ss").format(new Date())+":\n"+e.getMessage()+"\n");
			logWriter.close();
			Desktop.getDesktop().open(errorLog);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void setOutputLog()
	{
		File outputLog = new File("output.log");
		try {
			FileWriter logWriter = new FileWriter(outputLog,true);
			logWriter.append(output);
			logWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		try {
			String lang, root;
			Scanner sc = new Scanner(System.in);
			while (true)
			{
				System.out.println("Enter a valid directory containing AFS1 and AFS2 folders:");
				root = sc.nextLine();
				if (new File(root).isDirectory()) break;
			}
			while (true)
			{
				System.out.println("Enter a valid region name. It must be one of the following:");
				for (String l: LANG_OPTION) System.out.print(l+" ");
				System.out.println();
				sc = new Scanner(System.in);
				lang = sc.nextLine();
				int index = Arrays.binarySearch(LANG_OPTION, lang);
				if (index>=0)
				{
					for (int i=0; i<afsNames.length; i++) afsNames[i] = afsNames[i].replace("US", lang);
					sc.close(); break;
				}
			}
			for (int i=0; i<afsNames.length; i++)
			{
				output="";
				absPath = root+afsNames[i];
				File absPathRef = new File(absPath);
				File[] dirArray = absPathRef.listFiles();
				long start = System.currentTimeMillis();
				for (File dir: dirArray)
				{
					fileName = dir.getName();
					if (dir.isDirectory()) continue;
					String[] fileNameArray = fileName.split("\\.(?=[^\\.]+$)"); //separate file name from extension
					fileNameNoExt = fileNameArray[0];
					extension = fileNameArray[1];
					checkCharaFiles(dir);
					checkGSCFiles(dir, i);
					checkMapFiles(dir, i);
					checkMenuFiles(dir, i);
					absPathRef.renameTo(new File(root+"Misc. ("+afsNames[i]+")"));
				}
				long finish = System.currentTimeMillis();
				setOutputLog();
				System.out.println("Time for "+afsNames[i]+": "+(finish-start)/(double)1000+" s");
			}
		} catch (IOException e) {
			setErrorLog(e);
		}
	}
}