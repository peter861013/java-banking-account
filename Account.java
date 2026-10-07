import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class BankingException extends Exception {
    BankingException () { super(); }
    BankingException (String s) { super(s); }
} 

interface BasicAccount {
    String name();
    double balance();	
}

interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;	
}

interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;	
}

interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;	
}

interface FullFunctionalAccount extends WithdrawableAccount,
                                        DepositableAccount,
                                        InterestableAccount {
}

public abstract class Account {
	
    // protected variables to store commom attributes for every bank accounts	
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;
    
    // public methods for every bank accounts
    public String name() {
    	return(accountName);	
    }	
    
    public double balance() {
        return(accountBalance);
    }
    
    public double deposit(double amount) throws BankingException{
        accountBalance += amount;
        return(accountBalance);
    } 
    
    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;
    
    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return(withdraw(amount, withdrawDate));
    }
    
    abstract double computeInterest(Date interestDate) throws BankingException;
    
    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return(computeInterest(interestDate));
    }
}

/*
 *  Derived class: CheckingAccount
 *
 *  Description:
 *      Interest is computed daily; there's no fee for
 *      withdraw; there is a minimum balance of $1000.
 */
                          
class CheckingAccount extends Account implements FullFunctionalAccount {

    CheckingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;	
    }
    
    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }	
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
    // minimum balance is 1000, raise exception if violated
        if ((accountBalance  - amount) < 1000) {
            throw new BankingException ("Underfraft from checking account name:" +
                                         accountName);
        } else {
            accountBalance -= amount;	
            return(accountBalance); 	
        }                                        	
    }
    
    public double computeInterest (Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name" +
                                        accountName);                            	
        }

        
        int numberOfDays = (int) ((interestDate.getTime() 
                                   - lastInterestDate.getTime())
                                   / 86400000.0);

        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                                      accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }
} 

/*
 *  Derived class: SavingAccount
 *
 *  Description:
 *      monthly interest; fee of $1 for every transaction, except
 *      the first three per month are free; no minimum balance.
 */


class SavingAccount extends Account implements FullFunctionalAccount {

    protected double times;
    protected int month;

    SavingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;
        month = 0 ;
    }
    
    SavingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
        month = 0 ;
    }	
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
    monthlyTest();
    double fee = 0;
        if(times<=0){
            fee = 1;
        }
        if ((accountBalance  - amount - fee) <= 0) {
            throw new BankingException ("Underfraft from saving account name:" +
                                         accountName);
        } else {
            accountBalance -= (amount + fee);
            times--;
            return(accountBalance); 	
        }                                        	
    }
    
    public double computeInterest (Date interestDate) throws BankingException {
        monthlyTest();
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name" +
                                        accountName);                            	
        }
        
        int numberOfDays = (int) ((interestDate.getTime() 
                                   - lastInterestDate.getTime())
                                   / 86400000.0);
        int numberOfMonths = (int) (numberOfDays / 30.0);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12.0 *
                                      accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }

    private void monthlyTest() throws BankingException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String dataString = sdf.format(now);
        if(month != Integer.parseInt(dataString)){
            times = 3;
            month = Integer.parseInt(dataString);
        }
    }   
} 

/*
 *  Derived class: CDAccount
 *
 *  Description:
 *      monthly interest; ixed amount and duration (e.g., you can open
 *      1 12-month CD for $5000; for the next 12 months you can't deposit
 *      anything and withdrawals cost a  $250 fee); at the end of the 
 *      duration the interest payments stop and you can withdraw w/o fee.
 */


class CDAccount extends Account implements FullFunctionalAccount {

    protected boolean duration;

    CDAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;
        duration = true;
    }
    
    CDAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
        duration = true;
    }	
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {

    double fee = 0;
        if(duration){
            fee = 250;
        }
        if ((accountBalance  - amount - fee) <= 0) {
            throw new BankingException ("Underfraft from saving account name:" +
                                         accountName);
        } else {
            accountBalance -= (amount + fee);
            return(accountBalance); 
        }	                                       	
    }
    
    public double deposit(double amount) throws BankingException {
        yearTest();
        if(duration){
            throw new BankingException ("Invalid date to deposit for account name" +
                                        accountName);
        }
        else{
            accountBalance += amount;
            return(accountBalance);
        }
    }
    
    public double computeInterest (Date interestDate) throws BankingException {
        yearTest();
        if(duration){
            if (interestDate.before(lastInterestDate)) {
                throw new BankingException ("Invalid date to compute interest for account name" +
                                            accountName);                            	
            }
            
            int numberOfDays = (int) ((interestDate.getTime() 
                                    - lastInterestDate.getTime())
                                    / 86400000.0);
            int numberOfMonths = (int) (numberOfDays / 30.0);
            System.out.println("Number of months since last interest is " + numberOfMonths);
            double interestEarned = (double) numberOfMonths / 12.0 *
                                        accountInterestRate * accountBalance;
            System.out.println("Interest earned is " + interestEarned); 
            lastInterestDate = interestDate;
            accountBalance += interestEarned;
            return(accountBalance);  
        }
        else{
            throw new BankingException ("Invalid date to compute interest for account name" +
                                            accountName);
        }                     
    } 

    private void yearTest() throws BankingException {
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH);
        cal.setTime(openDate);
        int openYear = cal.get(Calendar.YEAR);
        int openMonth = cal.get(Calendar.MONTH);
        if((thisYear-openYear==1)&&thisMonth>=openMonth || (thisYear-openYear)>1){
            duration = false;
        }
    }
}

class LoanAccount extends Account implements FullFunctionalAccount {
    LoanAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = new Date();
        lastInterestDate = openDate;
    }
    
    LoanAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
    }	
    
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        throw new BankingException ("Underfraft from saving account name:" +
                                      accountName); 	                                       	
    }
    
    public double computeInterest (Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name" +
                                            accountName);                            	
        }
        int numberOfDays = (int) ((interestDate.getTime() 
                                - lastInterestDate.getTime())
                                / 86400000.0);
        int numberOfMonths = (int) (numberOfDays / 30.0);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12.0 *
                                    accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);  

    } 
}
