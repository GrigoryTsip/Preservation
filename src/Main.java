import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static GameProgress firstGame = new GameProgress(9, 15, 1, 0);
    public static GameProgress secondGame = new GameProgress(4, 8, 15, 315);
    public static GameProgress thirdGame = new GameProgress(11, 25, 21, 860);

    public static GameProgress[] games = {firstGame, secondGame, thirdGame};

    static Scanner scan = new Scanner(System.in);
    static String[] gameFile = new String[]{"firstgame", "secondgame", "thirdgame"};

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < gameFile.length; i++) {
            if (!saveGame("C://Games/savegames/" + gameFile[i] + ".dat", games[i])) {
                System.out.println("Ошибка записи файла " + gameFile[i]);
                break;
            }
        }
        System.out.print("Все игры сохранены. Архивировать сохраненные файлы? [Д/Н] ");
        String s = scan.nextLine();
        if (s.equals("д") || s.equals("Д")) {
            if (zipFiles("C://Games/savegames/", "Games", gameFile)) {
                System.out.println("Все игры в архиве");
                // если все заархивировалось - удалаям файлы сохраненных игр
                for (String val : gameFile) {
                    String fName = "C://Games/savegames/" + val + ".dat";
                    File f = new File(fName);
                    if (!f.delete()) System.out.println("Не удаляется файл " + fName);
                }
            } else System.out.println("Что-то пошло не так...");
        }
        System.out.println("Программа завершена");
    }


    public static boolean saveGame(String path, GameProgress game) {

        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean zipFiles(String path, String zipFile, String[] gameFile) {

        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(path + zipFile + ".zip"))) {
            for (String game : gameFile) {
                FileInputStream fis = new FileInputStream(path + game + ".dat");
                ZipEntry entry = new ZipEntry(game + ".dat");
                zout.putNextEntry(entry);

                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
