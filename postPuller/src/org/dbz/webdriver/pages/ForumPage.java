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
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ForumPage {

    private WebDriver driver;

    private final By finalCustLink = By.linkText("Final Customization");

    private final By minorLeagueCustLink = By.linkText("Minor League Final Customization");

    private static final String postLimit = "?x=100";

    public ForumPage() {

    }

    public ForumPage(final WebDriver driverInUse) {
        driver = driverInUse;
    }

    public ForumPage start() {
        driver.get("http://s4.zetaboards.com/Squees_Lair/index/");
        pageWaitByElement(finalCustLink);
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
        ForumPage buildsPage = this.navigateToMajorLeagueFinalCustomization();
        return getAllTeamsBuilds(teams, weeks, buildsPage, "naruto");
    }

    public ForumPage navigateToMajorLeagueFinalCustomization() {
        pageWaitByElement(finalCustLink);
        String url = driver.findElements(finalCustLink).get(0).getAttribute("href");
        url = url + "?x=20";
        driver.get(url);
//        driver.findElements(finalCustLink).get(0).click();
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
        By team = By.partialLinkText(teamName);
        try {
            pageWaitByElement(team);
            String teamPageUrl = driver.findElements(team).get(0).getAttribute("href");
            
            if(!teamPageUrl.contains(postLimit)) {
                teamPageUrl = teamPageUrl + postLimit;
            }
            driver.get(teamPageUrl);
//            driver.findElements(team).get(0).click();
            pageWaitByElement(By.className("c_post"));
            // String url = driver.getCurrentUrl();
//            String url = goToPage(driver.getCurrentUrl(), 1);

            // System.out.println(newUrl.toString());
//            driver.get(url);
            // assume it's the team on the 2nd page
        } catch (Exception e) {
            navigateToThreadPage(1);
            pageWaitByElement(team);
            driver.findElements(team).get(0).click();

        }
        return new ForumPage(driver);
    }

    // since I'm the only one using this, remember that the page numbering is 0
    // based
    public ForumPage navigateToThreadPage(int pageNum) {
        String page = String.valueOf(pageNum +1);
        By pageToNavigateTo = By.partialLinkText(page);

        // enforce that only one element is clicked
        ByChained correctLink = new ByChained(By.className("cat-pages"), pageToNavigateTo);
        driver.findElement(correctLink).click();
        return new ForumPage(driver);
    }

    private String goToPage(String url, int pageNum) {
        String[] urlPart = url.split("/");
        for(int i = 0; i < urlPart.length; i++) {
            if(urlPart[i].length() == 1) {
                urlPart[i] = Integer.toString(pageNum);
            }
        }
        
        StringBuilder newUrl = new StringBuilder();
        for (String part : urlPart) {
            newUrl.append(part).append("/");
        }
        if(!newUrl.toString().contains(postLimit)) {
            newUrl.append(postLimit);
        }
        System.out.println("going to: " + newUrl.toString());
        return newUrl.toString();
    }

    /**
     * old method to get the builds for a particular team. use the getAllBuilds method in place of it.
     * 
     * @return
     */

    public List<String> getBuilds() {
        return getAllBuilds(5);
    }

    public List<String> getAllBuilds(int numOfWeeks) {
        List<String> builds = new ArrayList<>();
        try {
            //note that the page indexes will need to change as time goes by to reflect the teams posting more builds
//            for (int pageIndex = 1; pageIndex <= 9; pageIndex++) {
//                List<WebElement> posts = driver.findElements(By.className("c_post"));
//                for (WebElement post : posts) {
//                    builds.add(post.getText());
//                }
//                driver.get(goToPage(driver.getCurrentUrl(), (pageIndex + 1)));
//            }
//         
            List<WebElement> posts = driver.findElements(By.className("c_post"));
            int pageIndex = 1;
            do{
                for (WebElement post : posts) {
                    builds.add(post.getText());
                }
                driver.get(goToPage(driver.getCurrentUrl(), (pageIndex + 1)));
                pageIndex++;
                posts = driver.findElements(By.className("c_post"));
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
            List<String> teamBuilds = buildsPage.getAllBuilds(weeks);
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
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(elementName));
    }

}
