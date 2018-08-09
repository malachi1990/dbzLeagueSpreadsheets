package org.dbz.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbz.webdriver.ExcelFormatter;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.dbz.webdriver.pages.ForumPage;
import org.openqa.selenium.WebDriver;

public class MinorLeagueReport {
    private static List<String> teamList = new ArrayList<>();


    static {
        teamList.add("Evolution");
        teamList.add("The Rad Rocketeers");
        teamList.add("Wrestlemania");
        teamList.add("The Frieza Force");
        teamList.add("Little Green Men");
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
//        System.setProperty("webdriver.chrome.driver", "/media/jordan/Opt/lib/chromedriver");
//      ChromeOptions options = new ChromeOptions();
//      options.addArguments("load-extension=/home/jordan/.config/google-chrome/Default/Extensions/gighmmpiobklfepjocnamgkkbiglidom/3.16.0_0");
//      DesiredCapabilities capabilities = new DesiredCapabilities();
//      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//      ChromeDriver driver = new ChromeDriver(capabilities);
//      WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.gecko.driver", "/media/jordan/Opt/lib/geckodriver");
//      ChromeOptions options = new ChromeOptions();
//      options.addArguments("load-extension=/home/jordan/.config/google-chrome/Default/Extensions/gighmmpiobklfepjocnamgkkbiglidom/3.16.0_0");
//      DesiredCapabilities capabilities = new DesiredCapabilities();
//      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//      ChromeDriver driver = new ChromeDriver(capabilities);
      WebDriver driver = new FirefoxDriver();       
      final ForumPage startPage = new ForumPage(driver);      
      long start_time = new Date().getTime();
      int numWeeksToPull = 15;
      startPage.start();
      Map<String, List<String>> builds = startPage.getMinorLeagueTeamBuilds(teamList, numWeeksToPull);
      teardown(driver);
      long end_time = new Date().getTime();
      
      System.out.println("Time to iterate and retrieve build info: " + (end_time - start_time));
//    for(String team : builds.keySet()){
//        System.out.println("team: " + team + " and their build: " + builds.get(team));
//    }

      ExcelFormatter formatter = new ExcelFormatter();
      formatter.writeZCastReport(builds, "/media/jordan/Opt/dbz_league/season 8/", "minors.xls");
		
	}
	
	public static void teardown(WebDriver driver){
		driver.close();
		driver.quit();

	}
}
