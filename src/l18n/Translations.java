package l18n;

import java.util.HashMap;
import java.util.Map;

public class Translations {
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        Map<String, String> de = new HashMap<>();
        de.put("title", "Funky Monkeys Image Compressor");
        de.put("inputLabel", "Eingabebilder:");
        de.put("outputLabel", "Ausgabeordner:");
        de.put("statusLabel", "Status:");
        de.put("qualityLabel", "Qualität:");
        de.put("widthLabel", "Breite:");
        de.put("inputButton", "Durchsuchen...");
        de.put("outputButton", "Durchsuchen...");
        de.put("compressButton", "Komprimieren");
        de.put("chooseInputTitle", "Eingabebilder auswählen");
        de.put("chooseOutputTitle", "Ausgabeordner wählen");
        de.put("invalidWidth", "Ungültige Breite eingegeben. Bitte eine Zahl eingeben.");
        de.put("compressionRunning", "Komprimierung läuft...");
        de.put("compressionFinished", "Komprimierung abgeschlossen.");
        de.put("fileError", "Fehler beim Komprimieren eines Bildes: ");
        de.put("noInputOutput", "Bitte wählen Sie Eingabebilder und einen Ausgabeordner.");
        de.put("outputDirError", "Ausgabeordner konnte nicht erstellt werden.");

        translations.put("de", de);

        Map<String, String> en = new HashMap<>();
        en.put("title", "Funky Monkeys Image Compressor");
        en.put("inputLabel", "Input Images:");
        en.put("outputLabel", "Output Folder:");
        en.put("statusLabel", "Status:");
        en.put("qualityLabel", "Quality:");
        en.put("widthLabel", "Width:");
        en.put("inputButton", "Browse...");
        en.put("outputButton", "Browse...");
        en.put("compressButton", "Compress");
        en.put("chooseInputTitle", "Select Input Images");
        en.put("chooseOutputTitle", "Select Output Folder");
        en.put("invalidWidth", "Invalid width entered. Please enter a number.");
        en.put("compressionRunning", "Compression running...");
        en.put("compressionFinished", "Compression finished.");
        en.put("fileError", "Error compressing an image: ");
        en.put("noInputOutput", "Please select input images and an output folder.");
        en.put("outputDirError", "Output folder could not be created.");
        translations.put("en", en);

        Map<String, String> vn = new HashMap<>();
        vn.put("title", "Trình nén hình ảnh Funky Monkeys");
        vn.put("inputLabel", "Hình ảnh đầu vào:");
        vn.put("outputLabel", "Thư mục đầu ra:");
        vn.put("statusLabel", "Trạng thái:");
        vn.put("qualityLabel", "Chất lượng:");
        vn.put("widthLabel", "Chiều rộng:");
        vn.put("inputButton", "Duyệt...");
        vn.put("outputButton", "Duyệt...");
        vn.put("compressButton", "Nén");
        vn.put("chooseInputTitle", "Chọn hình ảnh đầu vào");
        vn.put("chooseOutputTitle", "Chọn thư mục đầu ra");
        vn.put("invalidWidth", "Chiều rộng không hợp lệ. Vui lòng nhập một số.");
        vn.put("compressionRunning", "Đang nén...");
        vn.put("compressionFinished", "Nén hoàn tất.");
        vn.put("fileError", "Lỗi khi nén hình ảnh: ");
        vn.put("noInputOutput", "Vui lòng chọn hình ảnh đầu vào và thư mục đầu ra.");
        vn.put("outputDirError", "Thư mục đầu ra không thể được tạo.");
        translations.put("vn", vn);

    }

    /**
     * Get the translation for a given key in the specified language.
     * @param lang e.g. "en", "de", "vn"
     * @param key e.g. "title", "inputLabel"
     * @return The translated string or the key itself if not found. The fallback would be "en"
     */
    public static String getLanguage(String lang, String key) {
        Map<String, String> langMap = translations.get(lang);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.getOrDefault(key, key);
        }

        return translations.get("en").get(key); // Fallback to the key if the language is not found
    }
}
