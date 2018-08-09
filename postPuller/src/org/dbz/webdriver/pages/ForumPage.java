package org.dbz.webdriver.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbz.filter.BuildFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ForumPage {

    private WebDriver driver;

    private static final By teamCustLink = By.linkText("Team Customization");
    private static final By finalCustLink = By.linkText("Final Customization");
    private static final By postText = By.className("content");
    private static final By minorLeagueCustLink = By.linkText("Minor League Final Customization");

    public ForumPage() {

    }

    public ForumPage(final WebDriver driverInUse) {
        driver = driverInUse;
    }

    public ForumPage start() {
        driver.get("https://www.tapatalk.com/groups/squees_lair/index.php");
        pageWaitByElement(teamCustLink);
        return this;
    }

    public Map<String, List<String>> getMajorLeagueTeamBuilds(List<String> teams, int weeks) {
        ForumPage buildsPage = this.navigateToMajorLeagueFinalCustomization();
        return getAllTeamsBuilds(teams, weeks, buildsPage, "major");
    }

    public Map<String, List<String>> getMinorLeagueTeamBuilds(List<String> teams, int weeks) {
        ForumPage buildsPage = this.navigateToMinorLeagueFinalCustomization();
        return getAllTeamsBuilds(teams, weeks, buildsPage, "minor");
    }

    public Map<String, List<String>> getNarutoLeagueTeamBuilds(List<String> teams, int weeks) {
        ForumPage buildsPage = this.navigateToNarutoLeagueFinalCustomization();
        return getAllTeamsBuilds(teams, weeks, buildsPage, "naruto");
    }

    public ForumPage navigateToMajorLeagueFinalCustomization() {
        driver.findElements(teamCustLink).get(0).click();
        pageWaitByElement(finalCustLink);
        driver.findElement(finalCustLink).click();
        return new ForumPage(driver);
    }

    public ForumPage navigateToNarutoLeagueFinalCustomization() {
        pageWaitByElement(finalCustLink);
        driver.findElements(finalCustLink).get(1).click();
        return new ForumPage(driver);
    }

    public ForumPage navigateToMinorLeagueFinalCustomization() {
        pageWaitByElement(minorLeagueCustLink);
        driver.findElement(minorLeagueCustLink).click();
        return new ForumPage(driver);
    }

    public ForumPage navigateToTeamPage(String teamName) throws TimeoutException {
        By team = By.linkText(teamName);
        try {
            pageWaitByElement(team);
            driver.findElement(team).click();
            pageWaitByElement(postText);
            String url = driver.getCurrentUrl();
            String [] parts = url.split("\\.");
            if(teamName.equals("Afterlife")) {
                parts[parts.length-2] = parts[parts.length-2] + "-s30";
            } else {
                parts[parts.length-2] = parts[parts.length-2] + "-s90";
            }
            String newUrl = "";
            for(String part: parts) {
                newUrl += part;
                newUrl+= ".";
            }
            driver.get(newUrl);
            pageWaitByElement(postText);

        } catch (Exception e) {

        }
        
        return new ForumPage(driver);
    }

    // since I'm the only one using this, remember that the page numbering is 0
    // based
    public ForumPage navigateToThreadPage(int pageNum) {
        String page = String.valueOf(pageNum+1);
        By correctLink = By.linkText(page);
        driver.findElement(correctLink).click();
        return new ForumPage(driver);
    }

    /**
     * old method to get the builds for a particular team. use the getAllBuilds method in place of it.
     * 
     * @return
     */

    public List<String> getBuilds() {
        return getAllBuilds(5, 1);
    }

    public List<String> getAllBuilds(int numOfWeeks, int startPageNumber) {
        List<String> builds = new ArrayList<>();
        try {
            List<WebElement> posts = driver.findElements(postText);
            int pageIndex = startPageNumber;
            do{
                for (WebElement post : posts) {
                    builds.add(post.getText());
                }
                navigateToThreadPage((pageIndex + 1));
                pageIndex++;
                posts = driver.findElements(postText);
            } while(posts.size() > 0);
            // assuming that if I'm falling into the catch block, it's because i
            // don't have a link to find
        } catch (Exception e) {
            // e.printStackTrace();
        }
        builds = BuildFilter.filterBuilds(builds, numOfWeeks);

        return builds;
    }
 
    private Map<String, List<String>> getAllTeamsBuilds(List<String> teams, int weeks, ForumPage buildsPage,
            String reportType) {
        Map<String, List<String>> builds = new HashMap<>();
        for (String team : teams) {
            System.out.println("Pulling team builds for: " + team);
            buildsPage = buildsPage.navigateToTeamPage(team);
            int startPage = 9;
            if(team.equals("Afterlife")) {
                startPage = 3;
            }
            List<String> teamBuilds = buildsPage.getAllBuilds(weeks, startPage);
            builds.put(team, teamBuilds);
            if (reportType.equalsIgnoreCase("major")) {
                buildsPage = buildsPage.navigateToMajorLeagueFinalCustomization();
            } else if (reportType.equalsIgnoreCase("minor")) {
                buildsPage = buildsPage.navigateToMinorLeagueFinalCustomization();
            }
        }
        return builds;
    }

    public void pageWaitByElement(final By elementName) throws TimeoutException {
        new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(elementName));
    }

}
