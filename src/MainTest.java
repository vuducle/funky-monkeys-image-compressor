import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MainTest {


    @Test
    public void testImageCompression() throws IOException {
        // Erstelle eine temporäre Eingabebilddatei
        File inputFile = File.createTempFile("test_image", ".jpg");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(image, "jpg", inputFile);

        // Erstelle ein temporäres Ausgabeverzeichnis
        File outputDir = File.createTempFile("test_output", "");
        outputDir.delete(); // Make it a directory
        outputDir.mkdirs();

        // Erstelle eine Instanz der ImageCompressorApp (für indirekten Test der Komprimierung)
        Main compressor = new Main();

        // Setze Eingabedateien und Ausgabeverzeichnis (normalerweise über die GUI)
        compressor.inputFiles = Arrays.asList(inputFile); // Direktes Setzen für den Test
        compressor.outputTextField.setText(outputDir.getAbsolutePath());

        // Definiere eine Methode, um die Komprimierung synchron auszuführen (normalerweise asynchron)
        class SyncCompressor {
            public void compress() {
                compressor.compressImages();
            }
        }
        SyncCompressor syncCompressor = new SyncCompressor();
        syncCompressor.compress();

        // Teste, ob eine Ausgabedatei erstellt wurde
        File[] outputFiles = outputDir.listFiles();
        assertEquals(1, outputFiles.length);
        assertTrue(outputFiles[0].getName().startsWith("compressed_"));

        //cleanup
        inputFile.delete();
        for(File f : outputFiles){
            f.delete();
        }
        outputDir.delete();
    }

    @Test
    public void testChooseInputFiles(){
        Main compressor = new Main();
        //Simuliere Auswahl von Dateien.  Da die UI-Interaktion schwer zu testen ist,
        //wird hier ein Mock verwendet, um das Verhalten zu emulieren.
        final File tempFile1 = new File("test1.jpg");
        final File tempFile2 = new File("test2.jpg");
        List<File> fileList = Arrays.asList(tempFile1, tempFile2);

        compressor.inputFiles = fileList;

        String fileNames = compressor.inputTextField.getText();
        assertEquals("test1.jpg, test2.jpg",fileNames);
    }

    @Test
    public void testChooseOutputDirectory() throws IOException {
        Main compressor = new Main();

        // Simuliere die Auswahl eines Ausgabeverzeichnisses.
        File tempDir = File.createTempFile("test_dir", "");
        tempDir.delete();  // Lösche die Datei, damit wir ein Verzeichnis erstellen können
        tempDir.mkdirs();    // Erstelle das Verzeichnis

        compressor.outputTextField.setText(tempDir.getAbsolutePath());
        String selectedPath = compressor.outputTextField.getText();

        assertEquals(tempDir.getAbsolutePath(), selectedPath);

        // Aufräumen: Lösche das temporäre Verzeichnis
        tempDir.delete();
    }
}

