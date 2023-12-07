package crypto;
import javax.swing.*;
import java.awt.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class CryptoPanel extends JPanel {
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JComboBox<String> encryptionTypeComboBox;
    private SecretKey aesKey;
    private KeyPair rsaKeyPair;

    public CryptoPanel() {
        setLayout(new BorderLayout());

        inputTextArea = new JTextArea(10, 40);
        outputTextArea = new JTextArea(10, 40);
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        encryptionTypeComboBox = new JComboBox<>(new String[]{"AES", "RSA"});

        // Initialize keys
        initializeKeys();

        // Layout
        add(new JScrollPane(inputTextArea), BorderLayout.NORTH);
        JPanel controlPanel = new JPanel();
        controlPanel.add(encryptionTypeComboBox);
        controlPanel.add(encryptButton);
        controlPanel.add(decryptButton);
        add(controlPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputTextArea), BorderLayout.SOUTH);

        // Action Listeners
        encryptButton.addActionListener(e -> encryptData());
        decryptButton.addActionListener(e -> decryptData());
    }

    private void initializeKeys() {
        try {
            // AES Key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
            keyGen.init(256);
            aesKey = keyGen.generateKey();

            // RSA Key Pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGen.initialize(2048);
            rsaKeyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    private void encryptData() {
        try {
            String data = inputTextArea.getText();
            String encryptionType = (String) encryptionTypeComboBox.getSelectedItem();

            if ("AES".equals(encryptionType)) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
                cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(new byte[16]));
                byte[] encrypted = cipher.doFinal(data.getBytes());
                outputTextArea.setText(Base64.getEncoder().encodeToString(encrypted));
            } else if ("RSA".equals(encryptionType)) {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
                cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
                byte[] encrypted = cipher.doFinal(data.getBytes());
                outputTextArea.setText(Base64.getEncoder().encodeToString(encrypted));
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputTextArea.setText("Error in encryption");
        }
    }

    private void decryptData() {
        try {
            String data = inputTextArea.getText();
            String encryptionType = (String) encryptionTypeComboBox.getSelectedItem();

            if ("AES".equals(encryptionType)) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
                cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(new byte[16]));
                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
                outputTextArea.setText(new String(decrypted));
            } else if ("RSA".equals(encryptionType)) {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
                cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
                outputTextArea.setText(new String(decrypted));
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputTextArea.setText("Error in decryption");
        }
    }
}
