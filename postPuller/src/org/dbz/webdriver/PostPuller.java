package org.dbz.webdriver;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.dbz.webdriver.pages.ForumPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PostPuller {

	/*
	 * Final Customization thread names:
	 * 
	 * Namik
	 * Cold
	 * Ginyu
	 * Derp
	 * Muscle
	 * Earth's Defenders
	 * Rugrats
	 * Saiyans
	 * Super Saiyans
	 * DB Warriors
	 * Majins
	 * Buus
	 * Gohans
	 * Androids
	 * Valkyries
	 * Blades
	 */
	
	
    
    /**
     * Deprecated for the z cast report since that now pulls all builds for the # of weeks specified.
     * 
     * Might be worth coming back to this just so I can have 1 spreadsheet to focus on a given team.
     * @param args
     * @throws IOException
     */
	public static void main(String [] args) throws IOException{
		System.setProperty("webdriver.chrome.driver", "/media/jordan/Opt/lib/chromedriver");
		final WebDriver fireFoxDriver = new ChromeDriver();
		final ForumPage startPage = new ForumPage(fireFoxDriver);
		final long start_time = new Date().getTime();
		String filepath = "/media/jordan/Opt/dbz_league/season 8/";
		String filename = "Cold.xls";
		final String teamName = "Cold";
		startPage.start();
		ForumPage finalCustPage =startPage.navigateToMajorLeagueFinalCustomization();
		ForumPage teamPage = finalCustPage.navigateToTeamPage(teamName);
		List<String> builds = teamPage.getAllBuilds(10);
		
//		for(String build : builds){
			//System.out.println(build);
//			String [] lines = build.split("\\n");
//			System.out.println("Splitting string by newline delimiter: " +lines.length);
//		}
		PostPuller.teardown(fireFoxDriver);
		ExcelFormatter poiFormat = new ExcelFormatter();
		
		poiFormat.writeSingleTeamBuilds(builds, teamName, filepath, filename);
		
		long end_time = new Date().getTime();
		
		System.out.println("Total Time taken: " + (end_time - start_time));
	}
	
	
	public static void teardown(WebDriver driver){
		driver.close();
		driver.quit();

	}
	
}
