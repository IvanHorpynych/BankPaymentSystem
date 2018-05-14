package service;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * Uses dao layer.
 *
 * @author JohnUkraine
 */
public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        if(instance == null) {
            instance = new ServiceFactory();
        }

        return instance;
    }


    public static UserService getUserService() {
        return UserService.getInstance();
    }

    /*public static DebitAccountService getAccountService() {
        return DebitAccountService.getInstance();
    }

    public static CardService getCardService() {
        return CardService.getInstance();
    }

    public static PaymentService getPaymentService() {
        return PaymentService.getInstance();
    }*/


}
