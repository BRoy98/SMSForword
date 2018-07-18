package sms.forword.app;

public interface SmsListener {
    public void messageReceived(String sender, String messageText);
}
