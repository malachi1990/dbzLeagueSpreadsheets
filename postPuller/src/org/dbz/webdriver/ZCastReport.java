package org.dbz.webdriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbz.webdriver.pages.ForumPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ZCastReport {
    private static List<String> teamList = new ArrayList<>();

    static {
        teamList.add("Earth's Defenders");
        teamList.add("Rugrats");
        teamList.add("Saiyans");
        teamList.add("Valkyries");
        
        teamList.add("Namek");
        teamList.add("Ginyu");
        teamList.add("Derp");
        teamList.add("Majins");
        
        teamList.add("Cold");
        teamList.add("Androids");
        teamList.add("DB Warriors");
        teamList.add("Gohans");
        
        teamList.add("Afterlife");
        teamList.add("Buus");
        teamList.add("Muscle");
        teamList.add("Blades");
    }

	
	public static void main(String [] args) throws IOException{
        System.setProperty("webdriver.gecko.driver", "/media/jordan/Opt2/lib/geckodriver");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("load-extension=/home/jordan/.config/google-chrome/Default/Extensions/gighmmpiobklfepjocnamgkkbiglidom/3.16.0_0");
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//        ChromeDriver driver = new ChromeDriver(capabilities);
        WebDriver driver = new FirefoxDriver();
        ForumPage startPage = new ForumPage(driver);
		long start_time = new Date().getTime();
        int numWeeksToPull = 16;
		startPage.start();
		Map<String, List<String>> builds = startPage.getMajorLeagueTeamBuilds(teamList, numWeeksToPull);
        teardown(driver);

		long end_time = new Date().getTime();
		
//		for(String team : builds.keySet()){
//			System.out.println("team: " + team + " and their build: " + builds.get(team));
//		}

		ExcelFormatter formatter = new ExcelFormatter();
		formatter.writeZCastReport(builds, "/media/jordan/Opt2/dbz_league/season 9/", "test_week2.xls");
        System.out.println("Time to get info and create spreadsheet: " + ((end_time - start_time)/1000) + " seconds" );

	}
	
	public static void teardown(WebDriver driver){
	    //adding the try/catch because something changed in selenium which was causing these to throw exceptions . . .
	    try {
		driver.close();
		driver.quit();
	    }catch (Exception e) {
	        
	    }
	}
}
