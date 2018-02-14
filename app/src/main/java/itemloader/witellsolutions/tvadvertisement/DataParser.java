package itemloader.witellsolutions.tvadvertisement;

/**
 * Created by Sumudu on 5/9/2016.
 */
public class DataParser {
    private String emailAddressBlock;
    private boolean receivedNotification = false;

    public String getEmailAddressBlock() {
        return emailAddressBlock;
    }

    public void setEmailAddressBlock(String emailAddressBlock) {
        this.emailAddressBlock = emailAddressBlock;
    }

    public boolean isReceivedNotification() {
        return receivedNotification;
    }

    public void setReceivedNotification(boolean receivedNotification) {
        this.receivedNotification = receivedNotification;
    }
}
