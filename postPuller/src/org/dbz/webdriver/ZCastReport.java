package org.dbz.webdriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbz.webdriver.pages.ForumPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ZCastReport {
    private static List<String> teamList = new ArrayList<>();

    static {
        teamList.add("Namek");
        teamList.add("Cold");
        teamList.add("Ginyu");
        teamList.add("Derp");
        teamList.add("Muscle");
        teamList.add("Earth's Defenders");
        teamList.add("Rugrats");
        teamList.add("Saiyans");
        teamList.add("Afterlife");
        teamList.add("DB Warriors");
        teamList.add("Majins");
        teamList.add("Buus");
        teamList.add("Gohans");
        teamList.add("Androids");
        teamList.add("Valkyries");
        teamList.add("Blades");
    }

	
	public static void main(String [] args) throws IOException{
        System.setProperty("webdriver.chrome.driver", "/media/jordan/Opt/lib/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("load-extension=/home/jordan/.config/google-chrome/Default/Extensions/gighmmpiobklfepjocnamgkkbiglidom/3.10.0_0");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        ChromeDriver driver = new ChromeDriver(capabilities);
//        WebDriver driver = new ChromeDriver();
        ForumPage startPage = new ForumPage(driver);
		long start_time = new Date().getTime();
        int numWeeksToPull = 15;
		startPage.start();
		Map<String, List<String>> builds = startPage.getMajorLeagueTeamBuilds(teamList, numWeeksToPull);
		teardown(driver);
		long end_time = new Date().getTime();
		
		System.out.println("Time to iterate and retrieve build info: " + (end_time - start_time));
//		for(String team : builds.keySet()){
//			System.out.println("team: " + team + " and their build: " + builds.get(team));
//		}

		ExcelFormatter formatter = new ExcelFormatter();
		formatter.writeZCastReport(builds, "/media/jordan/Opt/dbz_league/season 8/", "week15_test.xls");
		
	}
	
	public static void teardown(WebDriver driver){
		driver.close();
		driver.quit();

	}
}
