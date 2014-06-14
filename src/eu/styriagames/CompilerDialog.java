package eu.styriagames;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CompilerDialog extends javax.swing.JDialog {

    public CompilerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(btNext);

        btNext.requestFocus();
        tbStatus.setText("Idling ...");
        tbStatus.setOpaque(false);
        tbStatus.setBorder(null);
        tbStatus.setEditable(false);
        tbStatus.setMaximumSize(new Dimension(tbStatus.getWidth(), tbStatus.getHeight()));
        tbDirectory.setText(new File(System.getProperty("user.home")).getAbsolutePath());
        tbDirectory.setMaximumSize(new Dimension(tbDirectory.getWidth(), tbDirectory.getHeight()));
        tbDirectoryMove.setText(new File(System.getProperty("user.home")).getAbsolutePath());
        tbDirectoryMove.setMaximumSize(new Dimension(tbDirectoryMove.getWidth(), tbDirectoryMove.getHeight()));
        lbHint.setText(String.format("Where are '%s' and '%s' located?", Frame.compileName, Frame.spcompName));
        lbHintMove.setText(String.format("Where would you like the compiled files to be moved?"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tbDirectory = new javax.swing.JTextField();
        btBrowse = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        lbHint = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tbStatus = new javax.swing.JTextField();
        cbSave = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        lbHintMove = new javax.swing.JLabel();
        tbDirectoryMove = new javax.swing.JTextField();
        btBrowseMove = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Compiler Settings");

        jLabel1.setText("Choose directory:");

        btBrowse.setText("Browse");
        btBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowseActionPerformed(evt);
            }
        });

        btNext.setText("Next");
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextActionPerformed(evt);
            }
        });

        lbHint.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        lbHint.setText("LOCATION");

        tbStatus.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N

        cbSave.setText("Save settings");

        jLabel2.setText("Choose directory:");

        lbHintMove.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        lbHintMove.setText("LOCATION");

        btBrowseMove.setText("Browse");
        btBrowseMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowseMoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(tbStatus)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btNext, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbHintMove)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tbDirectoryMove, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btBrowseMove, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbHint)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tbDirectory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbHint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(lbHintMove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbDirectoryMove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBrowseMove)
                    .addComponent(jLabel2))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btNext)
                    .addComponent(cbSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
            tbDirectoryMove.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btBrowseActionPerformed

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        File dir = new File(tbDirectory.getText());
        File dirMove = new File(tbDirectoryMove.getText());

        if (dirMove.exists() && dirMove.isDirectory()) {
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                boolean foundComplie = false,
                        foundSpcomp = false;

                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().equals(Frame.compileName)) {
                            foundComplie = true;
                        } else if (file.getName().equals(Frame.spcompName)) {
                            foundSpcomp = true;
                        }
                    }
                }

                if (!foundComplie && !foundSpcomp) {
                    tbStatus.setText(String.format("Couldn't find '%s' and '%s' in '%s'", Frame.compileName, Frame.spcompName, tbDirectory.getText()));
                } else if (foundComplie && !foundSpcomp) {
                    tbStatus.setText(String.format("Couldn't find '%s' in '%s'", Frame.spcompName, tbDirectory.getText()));
                } else if (!foundComplie && foundSpcomp) {
                    tbStatus.setText(String.format("Couldn't find '%s' in '%s'", Frame.compileName, tbDirectory.getText()));
                } else {
                    if (cbSave.isSelected()) {
                        Writer writer = null;

                        try {
                            writer = new BufferedWriter(new OutputStreamWriter(
                                    new FileOutputStream(Frame.configName), "utf-8"));

                            writer.write(String.format("// DO NOT CHANGE ANYTHING IN THIS FILE, UNLESS YOU KNOW WHAT YOU ARE DOING!%s%s", System.lineSeparator(), System.lineSeparator()));
                            writer.write(String.format("compilerDirectory=\"%s\"%s", tbDirectory.getText(), System.lineSeparator()));
                            writer.write(String.format("moveToDirectory=\"%s\"", tbDirectoryMove.getText()));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    String.format("Couldn't save the config file, proceeding without saving.\n\nError Message:\n%s", ex.getMessage()),
                                    "Error saving config file",
                                    JOptionPane.ERROR_MESSAGE);
                        } finally {
                            try {
                                writer.close();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        String.format("Couldn't save the config file, proceeding without saving.\n\nError Message:\n%s", ex.getMessage()),
                                        "Error saving config file",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    Frame.setCompilerDirectory(dir);
                    Frame.setMoveToDirectory(dirMove);
                    this.setVisible(false);
                }
            } else {
                tbStatus.setText("Compiler location '" + tbDirectory.getText() + "' is not valid!");
            }
        } else {
            tbStatus.setText("Move location '" + tbDirectoryMove.getText() + "' is not valid!");
        }

        tbStatus.setCaretPosition(0);
    }//GEN-LAST:event_btNextActionPerformed

    private void btBrowseMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowseMoveActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(tbDirectoryMove.getText()));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            tbDirectoryMove.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btBrowseMoveActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CompilerDialog dialog = new CompilerDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBrowse;
    private javax.swing.JButton btBrowseMove;
    private javax.swing.JButton btNext;
    private javax.swing.JCheckBox cbSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbHint;
    private javax.swing.JLabel lbHintMove;
    private javax.swing.JTextField tbDirectory;
    private javax.swing.JTextField tbDirectoryMove;
    private javax.swing.JTextField tbStatus;
    // End of variables declaration//GEN-END:variables
}
