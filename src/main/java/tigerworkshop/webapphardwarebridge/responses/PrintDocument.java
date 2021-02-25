package tigerworkshop.webapphardwarebridge.responses;

import tigerworkshop.webapphardwarebridge.services.SettingService;
import tigerworkshop.webapphardwarebridge.utils.AnnotatedPrintable;

import java.util.ArrayList;
import java.util.HashMap;

public class PrintDocument {
    String type;
    String url;
    String id;
    String filter;
    Integer qty = 1;
    String file_content;
    String raw_content;
    ArrayList<AnnotatedPrintable.AnnotatedPrintableAnnotation> extras = new ArrayList<>();
    private final SettingService settingService = SettingService.getInstance();

    public String getType() {
//        If we need the document to be sent to the printer attached to a label 'DEFAULTq'
//        Setting setting = settingService.getSetting();
//        HashMap<String, String> printers = setting.getPrinters();
//        if (type.equalsIgnoreCase("") || !printers.keySet().contains(type)) {
//            return "DEFAULT";
//        }
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public Integer getQty() {
        return qty;
    }

    public String getFileContent() {
        return file_content;
    }

    public String getRawContent() {
        return raw_content;
    }

    public String getFilter() {
		return filter;
	}

	public ArrayList<AnnotatedPrintable.AnnotatedPrintableAnnotation> getExtras() {
        return extras;
    }

    @Override
    public String toString() {
        return "PrintDocument{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", qty=" + qty +
                ", filter=" + filter +
                ", file_content='" + file_content + '\'' +
                ", raw_content='" + raw_content + '\'' +
                ", extras=" + extras +
                '}';
    }
}