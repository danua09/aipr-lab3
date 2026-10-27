package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DictClient extends Frame implements ActionListener {

    TextField rusField = new TextField(20);
    TextField engField = new TextField(20);
    TextArea resultArea = new TextArea();
    Button translateRusButton = new Button("Рус -> Англ");
    Button translateEngButton = new Button("Англ -> Рус");
    Button downloadPptButton = new Button("Скачать PPT");
    Button exitButton = new Button("Exit");

    String serverUrl = "http://localhost:8080/lab_servlet/";

    public DictClient() {
        super("Dictionary Client");
        setLayout(null);
        setBackground(new Color(170, 204, 255));
        setSize(500, 450);

        Label rusLabel = new Label("Русское слово:");
        rusLabel.setBounds(20, 40, 120, 20);
        add(rusLabel);

        rusField.setBounds(150, 40, 200, 25);
        add(rusField);

        translateRusButton.setBounds(360, 40, 120, 25);
        translateRusButton.addActionListener(this);
        add(translateRusButton);

        Label engLabel = new Label("English word:");
        engLabel.setBounds(20, 80, 120, 20);
        add(engLabel);

        engField.setBounds(150, 80, 200, 25);
        add(engField);

        translateEngButton.setBounds(360, 80, 120, 25);
        translateEngButton.addActionListener(this);
        add(translateEngButton);

        resultArea.setBounds(20, 120, 450, 230);
        add(resultArea);

        downloadPptButton.setBounds(60, 370, 150, 30);
        downloadPptButton.addActionListener(this);
        add(downloadPptButton);

        exitButton.setBounds(280, 370, 150, 30);
        exitButton.addActionListener(this);
        add(exitButton);

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == exitButton) {
            System.exit(0);
        } else if (ae.getSource() == translateRusButton) {
            translate("txt", rusField.getText());
        } else if (ae.getSource() == translateEngButton) {
            translate("eng_txt", engField.getText());
        } else if (ae.getSource() == downloadPptButton) {
            downloadPpt();
        }
    }

    private void translate(String paramName, String word) {
        try {
            String urlStr = serverUrl + "MyServlet?" + paramName + "="
                + URLEncoder.encode(word.trim(), "UTF-8");

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();

            String text = sb.toString().replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
            resultArea.setText("Ответ сервера:\n" + text);
        } catch (Exception e) {
            resultArea.setText("Ошибка: " + e.getMessage());
        }
    }

    private void downloadPpt() {
        try {
            String urlStr = serverUrl + "PptServlet";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            File pptFile = new File(System.getProperty("user.home") + "/Desktop/dictionary.pptx");
            FileOutputStream fos = new FileOutputStream(pptFile);

            byte[] buf = new byte[4096];
            int n;
            while ((n = is.read(buf)) != -1) {
                fos.write(buf, 0, n);
            }
            fos.close();
            is.close();
            conn.disconnect();

            resultArea.setText("Презентация сохранена: " + pptFile.getAbsolutePath()
                + "\nОткрываю...");

            Desktop.getDesktop().open(pptFile);
        } catch (Exception e) {
            resultArea.setText("Ошибка скачивания: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DictClient();
    }
}
