import java.util.*;

public class Application {
	
public static void main( String args []) {
	
    /* put your own tests here ....... */
	 //if your implementaion is correct, you can do the following with polymorphic array accountList
		Account[] accountList;
		
		accountList = new Account[4];
		
		// buid 4 different accounts in the same array
		accountList[0] = new CheckingAccount("John Smith", 1500.0);
		accountList[1] = new SavingAccount("William Hurt", 1200.0);
        accountList[2] = new CDAccount("Woody Allison", 1000.0);
        accountList[3] = new LoanAccount("Judi Foster", -1500.0);
        

		for (int count = 0; count < accountList.length; count++) {
            try {
                double newBalance = accountList[count].computeInterest();
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            }catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());	
            }

            try {
                double newBalance = accountList[count].withdraw(500);
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            }catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());	
            }

            try {
                double newBalance = accountList[count].withdraw(1000);
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            }catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());	
            }

            try {
                double newBalance = accountList[count].withdraw(3000);
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            }catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());	
            }

            try {
                double newBalance = accountList[count].deposit(500);
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            }catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());	
            }
        }
        
        
	
}

static void stdExceptionPrinting(Exception e, double balance) {
    System.out.println("EXCEPTION: Banking system throws a " + e.getClass() +
                       " with message: \n\t" +
                       "MESSAGE: " + e.getMessage());
    System.out.println("\tAccount balance remains $" + balance + "\n");
}
}      