import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

public class AudioReader
{
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException,IOException
    {
        Scanner scanner = new Scanner(System.in);
        File file = new File("Strong Self Esteem - Jeremy Korpas.wav");
        AudioInputStream AudioStream = AudioSystem.getAudioInputStream(file);

        Clip clip = AudioSystem.getClip();
        clip.open(AudioStream);

        String response = "";

        while(!response.equals("Q"))
        {
            System.out.println("(P) to Play, (S) to Stop, (R) to Reset, (Q) to Quit");
            System.out.print("Input action : ");

            response = scanner.next();
            response = response.toUpperCase();

            switch (response)
            {
                case "P":
                    clip.start();
                    break;
                case "S":
                    clip.stop();
                    break;
                case "R":
                    clip.setMicrosecondPosition(0);
                    break;
                case "Q":
                    clip.close();
                    break;
                default:
                    System.out.println("Invalid Response");
                    break;
            }
        }
        clip.start();
        scanner.close();
        System.out.println("Audio Quit...");
    }
}