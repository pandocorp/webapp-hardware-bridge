package tigerworkshop.webapphardwarebridge.utils;

public class PandoResponse {
    public Integer printedCount;
    public Integer exceptionCount;
    public StringBuilder exceptionMessages;

    public PandoResponse() {
        this.printedCount = 0;
        this.exceptionCount = 0;
        this.exceptionMessages = new StringBuilder();
    }
}
