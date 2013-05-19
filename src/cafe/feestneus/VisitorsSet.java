package cafe.feestneus;

public class VisitorsSet
{
        private String extractedUsersToday = "";
        private String extractedUsersTomorrow = "";
        private String extractedUsersDayAfter = "";
         
        public void setExtractedUsers(String extractedUsers)
        {
            this.extractedUsersToday += "- " + extractedUsers + "\n";
        }

        public void setExtractedUsersTomorrow(String extractedUsers)
        {
            this.extractedUsersTomorrow += "- " + extractedUsers + "\n";
        }

        public void setExtractedUsersDayAfter(String extractedUsers)
        {
            this.extractedUsersDayAfter += "- " + extractedUsers + "\n";
        }
 
        public String getToday()
        {
            return this.extractedUsersToday;
        }

        public String getTomorrow()
        {
            return this.extractedUsersTomorrow;
        }

        public String getDayAfter()
        {
            return this.extractedUsersDayAfter;
        }   
}