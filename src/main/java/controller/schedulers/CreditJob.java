package controller.schedulers;

import entity.CreditAccount;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import service.CreditAccountService;
import service.ServiceFactory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class CreditJob implements Job {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
     CreditAccountService creditAccountService = ServiceFactory.getCreditAccountService();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        int daysInMonth = new GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH);

        List<CreditAccount> creditAccounts =
                creditAccountService.findAllNotClosed();

        BigDecimal accruedInterest;

        for (CreditAccount creditAccount : creditAccounts) {
            accruedInterest = percentage(creditAccount.getBalance(),
                    creditAccount.getInterestRate()/daysInMonth);
            creditAccountService.accrue(creditAccount,accruedInterest);
        }
    }

    private BigDecimal percentage(BigDecimal base, float pct) {
        return base.multiply(BigDecimal.valueOf(pct)).divide(ONE_HUNDRED);
    }
}
