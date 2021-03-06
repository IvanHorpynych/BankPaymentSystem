package service;

import entity.CreditAccount;
import entity.CreditRequest;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * Uses dao layer.
 *
 * @author JohnUkraine
 */
public class ServiceFactory {
    private static volatile ServiceFactory instance;

    private ServiceFactory() {}
    
    public static ServiceFactory getInstance() {
        ServiceFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ServiceFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ServiceFactory();
                }
            }
        }
        return localInstance;
    }





    public static UserService getUserService() {
        return UserService.getInstance();
    }

    public static CreditAccountService getCreditAccountService() {
        return CreditAccountService.getInstance();
    }

    public static DebitAccountService getDebitAccountService() {
        return DebitAccountService.getInstance();
    }

    public static DepositAccountService getDepositAccountService() {
        return DepositAccountService.getInstance();
    }

    public static CardService getCardService() {
        return CardService.getInstance();
    }

    public static PaymentService getPaymentService() {
        return PaymentService.getInstance();
    }

    public static AccountsService getAccountsService() {
        return AccountsService.getInstance();
    }

    public static CreditRequestService getCreditRequestService() {
        return CreditRequestService.getInstance();
    }

    public static RateService getRateService() {
        return RateService.getInstance();
    }


}
