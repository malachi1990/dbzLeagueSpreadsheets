package org.dbz.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbz.webdriver.ExcelFormatter;
import org.dbz.webdriver.pages.ForumPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MinorLeagueReport {
    private static List<String> teamList = new ArrayList<>();


    static {
        teamList.add("The Rad Rocketeers");
        teamList.add("Wrestlemania");
        teamList.add("The Frieza Force");
        teamList.add("Little Green Men");
        teamList.add("Evolution of Evil");
        teamList.add("The Lefties");
        teamList.add("The Baldies");
        teamList.add("Team Shortcake");
        teamList.add("Heroes in Training");
        teamList.add("Red Ribbon");
        teamList.add("Two and a Half Saiyans");
        teamList.add("Metal Mania");
        teamList.add("Mullet Club");
        teamList.add("We Three Kings");
        teamList.add("Blue Man Group");
        teamList.add("The Turtle Legacy");
    }
	
	public static void main(String [] args) throws IOException{
	    System.setProperty("webdriver.chrome.driver", "/media/jordan/Opt/lib/chromedriver");
		WebDriver fireFoxDriver = new ChromeDriver();
		
		ForumPage startPage = new ForumPage(fireFoxDriver);
		long start_time = new Date().getTime();
		startPage.start();
		int weekOfSeason = 3;
		Map<String, List<String>> builds = startPage.getMinorLeagueTeamBuilds(teamList, weekOfSeason);
		teardown(fireFoxDriver);
		long end_time = new Date().getTime();
		
		System.out.println("Time to iterate and retrieve build info: " + (end_time - start_time));
//		for(String team : builds.keySet()){
//			System.out.println("team: " + team + " and their build: " + builds.get(team));
//		}

		ExcelFormatter formatter = new ExcelFormatter();
		formatter.writeZCastReport(builds, "/media/jordan/Opt/dbz_league/minors/", "preseason week 3.xls");
		
	}
	
	public static void teardown(WebDriver driver){
		driver.close();
		driver.quit();

	}
}
