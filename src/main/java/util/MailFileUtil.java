package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MailFileUtil {
    public static void saveMailAttachment(String root, String fname, byte[] data) throws IOException {
        String mailDir = root + "/mail_attachments";
        File dir = new File(mailDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 하위 디렉토리까지 생성
        }
        File file = new File(dir, fname);
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(data);
        }
    }
}