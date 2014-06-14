package eu.styriagames;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Frame extends javax.swing.JFrame implements Runnable {

    public static final String version = "Alpha 0.01";
    public static final String configName = "sourcemod.watcher.cfg";
    public static final String compileName = "compile.exe";
    public static final String spcompName = "spcomp.exe";

    private final Thread thread;

    private static File compilerDir = null;
    private static File moveToDir = null;
    private File watchingDir = new File(".");
    private boolean running = false;
    private boolean watching = false;
    private boolean once = false;
    private List<Long> lastModified = new ArrayList<>();

    public Frame() {
        initComponents();

        this.setTitle(String.format("SourceMod: Watcher (%s)", version));
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(btWatch);

        try (BufferedReader br = new BufferedReader(new FileReader(configName))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.contains("=")) {
                    continue;
                }
                
                String[] split = line.split("=");
                String variable = split[0];
                String value = split[1].substring(1, split[1].length() - 1);

                if (variable.equals("compilerDirectory")) {
                    compilerDir = new File(value);
                }
                
                if (variable.equals("moveToDirectory")) {
                    moveToDir = new File(value);
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    String.format("Couldn't load the config file, proceeding without loading.\n\nError Message:\n%s", ex.getMessage()),
                    "Error loading config file",
                    JOptionPane.ERROR_MESSAGE);
        }

        checkConfig();

        btWatch.requestFocus();
        tbStatus.setText("Idling ...");
        tbStatus.setOpaque(false);
        tbStatus.setBorder(null);
        tbStatus.setEditable(false);
        tbStatus.setMaximumSize(new Dimension(tbStatus.getWidth(), tbStatus.getHeight()));
        tbDirectory.setText(new File(System.getProperty("user.home")).getAbsolutePath());
        tbDirectory.setMaximumSize(new Dimension(tbDirectory.getWidth(), tbDirectory.getHeight()));
        taConsole.setEditable(false);

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public static void setCompilerDirectory(File dir) {
        compilerDir = dir;

        System.out.println(String.format("SourceMod compiler directory: '%s'", compilerDir.getAbsolutePath()));
    }
    
    public static void setMoveToDirectory(File dir) {
        moveToDir = dir;

        System.out.println(String.format("Compiled files will be moved to: '%s'", moveToDir.getAbsolutePath()));
    }

    private void checkConfig() {
        boolean openDialog = false;

        if (compilerDir != null) {
            if (compilerDir.exists() && compilerDir.isDirectory()) {
                File[] files = compilerDir.listFiles();
                boolean foundComplie = false,
                        foundSpcomp = false;

                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().equals(compileName)) {
                            foundComplie = true;
                        } else if (file.getName().equals(spcompName)) {
                            foundSpcomp = true;
                        }
                    }
                }

                if (!foundComplie || !foundSpcomp) {
                    openDialog = true;
                }
            } else {
                openDialog = true;
            }
        } else {
            openDialog = true;
        }
    
        if (moveToDir != null) {
            if (moveToDir.exists() && moveToDir.isDirectory()) {
            } else {
                openDialog = true;
            }
        } else {
            openDialog = true;
        }

        if (openDialog) {
            CompilerDialog compilerDialog = new CompilerDialog(this, true);
            compilerDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }
            });
            compilerDialog.setVisible(true);
        } else {
            System.out.println(String.format("[%s] Loaded config '%s'", getCurrentTime(), configName));
            System.out.println(String.format("[%s] SourceMod compiler directory: '%s'", getCurrentTime(), compilerDir.getAbsolutePath()));
            System.out.println(String.format("[%s] Compiled files will be moved to: '%s'", getCurrentTime(), moveToDir.getAbsolutePath()));
            taConsole.append(String.format("[%s] Loaded config '%s'\n", getCurrentTime(), configName));
            taConsole.append(String.format("[%s] SourceMod compiler directory: '%s'\n", getCurrentTime(), compilerDir.getAbsolutePath()));
            taConsole.append(String.format("[%s] Compiled files will be moved to: '%s'\n", getCurrentTime(), moveToDir.getAbsolutePath()));
        }
    }

    private void watch(File dir) {
        File[] files = dir.listFiles();
        String extension = "";
        int j = 0, k = 0;
        
        if (!once) {
            for (File file : files) {
                if (file.isFile()) {
                    int li = file.getName().lastIndexOf('.');
                    if (li > 0) {
                        extension = file.getName().substring(li);
                    }

                    if (extension.equals(".sp")) {
                        lastModified.add(k, file.lastModified());
                        
                        k++;
                    }
                }
            }
            
            once = true;
        }

        for (File file : files) {
            if (file.isFile()) {
                int li = file.getName().lastIndexOf('.');
                if (li > 0) {
                    extension = file.getName().substring(li);
                }

                if (extension.equals(".sp")) {
                    long lastModifiedFile = lastModified.get(j);

                    if (file.lastModified() > lastModifiedFile) {                            
                        taConsole.append(String.format("[%s] '%s' changed, compiling ...\n", getCurrentTime(), file.getName()));

                        String compiledName = file.getName().replace(".sp", ".smx");

                        taConsole.append(executeCommand(String.format("cmd /C cd \"%s\" && spcomp \"%s\\%s\" && move \"%s\\%s\" \"%s\"", compilerDir, watchingDir, file.getName(), compilerDir, compiledName, moveToDir)));
                        taConsole.setCaretPosition(taConsole.getDocument().getLength());

                        lastModified.remove(j);
                        lastModified.add(j, file.lastModified());
                    }

                    j++;
                }
            }
        }
    }
    
    private Time getCurrentTime() {
        return new Time(System.currentTimeMillis());
    }

    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        return output.toString();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btBrowse = new javax.swing.JButton();
        tbDirectory = new javax.swing.JTextField();
        btWatch = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tbStatus = new javax.swing.JTextField();
        btChangeCompilerSettings = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taConsole = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Directory to watch:");

        btBrowse.setText("Browse");
        btBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowseActionPerformed(evt);
            }
        });

        btWatch.setText("Watch!");
        btWatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btWatchActionPerformed(evt);
            }
        });

        tbStatus.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N

        btChangeCompilerSettings.setText("Change Config");
        btChangeCompilerSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChangeCompilerSettingsActionPerformed(evt);
            }
        });

        taConsole.setColumns(20);
        taConsole.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        taConsole.setRows(5);
        jScrollPane1.setViewportView(taConsole);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addComponent(tbStatus)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tbDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btChangeCompilerSettings)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btWatch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(btBrowse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btBrowse)
                    .addComponent(tbDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btWatch)
                    .addComponent(btChangeCompilerSettings))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowseActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            tbDirectory.setText(selectedFile.getAbsolutePath());

            System.out.println(String.format("[%s] Set watching directory to: '%s'", getCurrentTime(), selectedFile.getAbsolutePath()));
        }
    }//GEN-LAST:event_btBrowseActionPerformed

    private void btWatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btWatchActionPerformed
        File dir = new File(tbDirectory.getText());

        if (dir.exists() && dir.isDirectory()) {
            once = false;
            lastModified.clear();
            watchingDir = new File(tbDirectory.getText());
            watching = true;

            tbStatus.setText(String.format("Watching '%s'", watchingDir));
            taConsole.append(String.format("[%s] Watching '%s'\n", getCurrentTime(), watchingDir));
            taConsole.setCaretPosition(taConsole.getDocument().getLength());
        } else {
            tbStatus.setText(String.format("'%s' is not valid!", tbDirectory.getText()));
        }

        tbStatus.setCaretPosition(0);
    }//GEN-LAST:event_btWatchActionPerformed

    private void btChangeCompilerSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangeCompilerSettingsActionPerformed
        CompilerDialog compilerDialog = new CompilerDialog(this, true);
        compilerDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        compilerDialog.setVisible(true);
    }//GEN-LAST:event_btChangeCompilerSettingsActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBrowse;
    private javax.swing.JButton btChangeCompilerSettings;
    private javax.swing.JButton btWatch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea taConsole;
    private javax.swing.JTextField tbDirectory;
    private javax.swing.JTextField tbStatus;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (running) {
            try {
                if (watching) {
                    watch(watchingDir);
                }

                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
