package controller.schedulers;

import entity.DepositAccount;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import service.DepositAccountService;
import service.ServiceFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DepositJob implements Job {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private DepositAccountService depositAccountService = ServiceFactory.getDepositAccountService();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<DepositAccount> depositsAccounts =
                depositAccountService.findAllNotClosed();

        Date currDate = new Date();
        long diffInMillis;
        long difference;
        BigDecimal interestCharges;
        for(DepositAccount depositAccount: depositsAccounts){

            if(depositAccount.getMinBalance().compareTo(BigDecimal.ZERO)>0){
                diffInMillis = Math.abs(currDate.getTime() - depositAccount.getLastOperationDate().getTime());
                difference = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                if(difference>=29.5){
                    interestCharges = percentage(depositAccount.getMinBalance(),
                            depositAccount.getAnnualRate()/12);
                    depositAccount.setMinBalance(depositAccount.getBalance());
                    depositAccount.setLastOperationDate(currDate);
                    depositAccountService.accrue(depositAccount,interestCharges);
                }
            }
        }
    }

    private BigDecimal percentage(BigDecimal base, float pct) {
        return base.multiply(BigDecimal.valueOf(pct)).divide(ONE_HUNDRED);
    }
}
