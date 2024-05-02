package framework.engine.utils;

import io.qameta.allure.Attachment;

import java.nio.charset.StandardCharsets;

public class AllureReportUtil {

    private AllureReportUtil() {
    }

    @Attachment(value = "{desc}", type = "text/plain")
    public static byte[] attachJSONToAllure(String desc, String jsonObject) {
        return jsonObject.getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "{desc}", type = "text/plain")
    public static String attachTextToAllure(String desc, String text) {
        return text;
    }


    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot(byte[] screenShot) {
        return screenShot;
    }

}
