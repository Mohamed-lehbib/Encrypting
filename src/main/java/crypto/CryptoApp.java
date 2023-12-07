package crypto;
import javax.swing.*;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoApp {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("CryptoApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CryptoPanel());
        frame.pack();
        frame.setVisible(true);
    }
}

