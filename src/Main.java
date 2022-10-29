import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        GameProgress progressVasya = new GameProgress(100, 24, 5, 253.6);
        GameProgress progressIrina = new GameProgress(14, 0, 3, 121.56);
        GameProgress progressStatas = new GameProgress(0, 0, 1, 10);

        String path = "resources\\Games\\savegames";

        //Создание сохранений
        saveGame(path, progressVasya, "saveVasya.dat");
        saveGame(path, progressIrina, "saveIrina.dat");
        saveGame(path, progressStatas, "saveStatas.dat");

        //Перенос сохранений в архив
        String zipPath = path.replace("\\savegames", "") + "\\zipSaves.zip";
        zipFiles(zipPath, new String[]{path + "\\saveVasya.dat", path + "\\saveIrina.dat"});

        //Удаляем файлы, не попавшие в архив
        File files = new File(path);
        for (File file : files.listFiles()) {
            file.delete();
        }
    }

    private static void saveGame(String path, GameProgress gameProgress, String saveName) throws FileNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path + "\\" + saveName)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <ex> void zipFiles(String path, String[] files) {
        //Открываем два потока данных
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String file : files) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file);
                zout.putNextEntry(entry);
                //Заносим в буфер
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                //Заносим в архив
                zout.write(buffer);
                //Закрываем запись
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}