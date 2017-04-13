package org.dbz.filter;

import java.util.ArrayList;
import java.util.List;

public class BuildFilter {

    /**
     * Filters out old builds that I may not be concerned about.
     * 
     * The numOfWeeks variable tells this to pull the last X number of weeks.
     * 
     * @param builds
     * @param numOfWeeks
     * @return
     */
    public static List<String> filterBuilds(List<String> builds, int numOfWeeks) {
        List<String> filteredList = new ArrayList<>();
        if (builds.size() > numOfWeeks) {
            for (int i = numOfWeeks; i > 0; i--) {
                filteredList.add(builds.get(builds.size() - i));
            }
        } else {
            filteredList = builds;
        }
        return filteredList;
    }
}
