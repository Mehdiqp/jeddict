/**
 * Copyright [2014] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.jpa.modeler.jcre.wizard;

import java.io.IOException;
import javax.swing.ComboBoxModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jpa.modeler.collaborate.issues.ExceptionUtils;
import org.netbeans.jpa.modeler.source.JavaIdentifiers;
import org.netbeans.jpa.modeler.source.SourceGroups;
import org.netbeans.modules.j2ee.persistence.wizard.Util;
import org.netbeans.modules.j2ee.persistence.wizard.fromdb.SourceGroupUISupport;
import org.netbeans.spi.java.project.support.ui.PackageView;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

public class JPAModelSetupPanelVisual extends javax.swing.JPanel implements DocumentListener {

    private WizardDescriptor wizard;
    private Project project;
    private JTextComponent packageComboBoxEditor;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * Creates new form CrudSetupPanel
     */
    public JPAModelSetupPanelVisual(Project project, WizardDescriptor wizard) {
        this.project = project;
        this.wizard = wizard;
        initComponents();

        packageComboBoxEditor = ((JTextComponent) packageComboBox.getEditor().getEditorComponent());
        Document packageComboBoxDocument = packageComboBoxEditor.getDocument();
        packageComboBoxDocument.addDocumentListener(this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerLabel = new javax.swing.JLabel();
        projectLabel = new javax.swing.JLabel();
        projectTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox();
        packageLabel = new javax.swing.JLabel();
        packageComboBox = new javax.swing.JComboBox();
        fileNameLabel = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();

        setName(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_JPA_Model")); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/netbeans/jpa/modeler/jcre/wizard/Bundle"); // NOI18N
        headerLabel.setText(bundle.getString("MSG_JPA_File_Location")); // NOI18N

        projectLabel.setLabelFor(projectTextField);
        projectLabel.setText(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Project")); // NOI18N

        projectTextField.setEditable(false);

        locationLabel.setLabelFor(locationComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(locationLabel, org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N

        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        packageLabel.setLabelFor(packageComboBox);
        packageLabel.setText(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Package")); // NOI18N

        packageComboBox.setEditable(true);

        fileNameLabel.setText("FileName :");

        fileNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fileNameTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerLabel)
                .addGap(0, 361, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(locationLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fileNameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(packageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(projectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(locationComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(projectTextField)
                    .addComponent(packageComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectLabel)
                    .addComponent(projectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationLabel)
                    .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileNameLabel)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(packageLabel)
                    .addComponent(packageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(122, Short.MAX_VALUE))
        );

        headerLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "MSG_JPA_File_Location")); // NOI18N
        headerLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "MSG_JPA_File_Location")); // NOI18N
        projectLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Project")); // NOI18N
        projectLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Project")); // NOI18N
        projectTextField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ACSD_Project")); // NOI18N
        locationLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N
        locationLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_SrcLocation")); // NOI18N
        locationComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ACSD_Location")); // NOI18N
        packageLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Package")); // NOI18N
        packageLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "LBL_Package")); // NOI18N
        packageComboBox.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ACSD_Package")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        locationChanged();
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void fileNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fileNameTextFieldKeyReleased
        changeSupport.fireChange();
    }//GEN-LAST:event_fileNameTextFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JComboBox locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JComboBox packageComboBox;
    private javax.swing.JLabel packageLabel;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JTextField projectTextField;
    // End of variables declaration//GEN-END:variables

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    boolean valid(WizardDescriptor wizard) {
        if (Util.isContainerManaged(project)) {
            ClassPath cp = ClassPath.getClassPath(getLocationValue().getRootFolder(), ClassPath.COMPILE);
            ClassLoader cl = cp.getClassLoader(true);
            try {
                Class.forName("javax.transaction.UserTransaction", false, cl);
            } catch (ClassNotFoundException cnfe) {
                wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_UserTransactionUnavailable"));
                return false;
            } catch (java.lang.UnsupportedClassVersionError err) {
                wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_UserTransactionJavaVersion"));
                return false;
            }
        }

        String fileName = getFileName();
        if (fileName.trim().equals("")) { // NOI18N
            wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_JavaTargetChooser_CantUseEmptyFileName"));
            return false;
        }

        String packageName = getPackage();
        if (packageName.trim().equals("")) { // NOI18N
            wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_JavaTargetChooser_CantUseDefaultPackage"));
            return false;
        }

        if (!JavaIdentifiers.isValidPackageName(packageName)) {
            wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_JavaTargetChooser_InvalidPackage")); //NOI18N
            return false;
        }

        if (!SourceGroups.isFolderWritable(getLocationValue(), packageName)) {
            wizard.putProperty("WizardPanel_errorMessage", NbBundle.getMessage(JPAModelSetupPanelVisual.class, "ERR_JavaTargetChooser_UnwritablePackage")); //NOI18N
            return false;
        }
        wizard.putProperty("WizardPanel_errorMessage", ""); // NOI18N
        return true;
    }

    public SourceGroup getLocationValue() {
        return (SourceGroup) locationComboBox.getSelectedItem();
    }

    public String getFileName() {
        return fileNameTextField.getText();
    }

    public String getPackage() {
        return packageComboBoxEditor.getText();
    }

    private void locationChanged() {
        updateSourceGroupPackages();
//        changeSupport.fireChange();
    }

    void read(WizardDescriptor settings) {
//        jsfFolder.setText((String) settings.getProperty(WizardProperties.JSF_FOLDER));

//        project = Templates.getProject(settings);
        FileObject targetFolder = Templates.getTargetFolder(settings);

        projectTextField.setText(ProjectUtils.getInformation(project).getDisplayName());

        SourceGroup[] sourceGroups = SourceGroups.getJavaSourceGroups(project);
        SourceGroupUISupport.connect(locationComboBox, sourceGroups);

        packageComboBox.setRenderer(PackageView.listRenderer());

        updateSourceGroupPackages();

        // set default source group and package cf. targetFolder
//        if (targetFolder != null) {
//            SourceGroup targetSourceGroup = SourceGroupSupport.getFolderSourceGroup(sourceGroups, targetFolder);
//            if (targetSourceGroup != null) {
//                locationComboBox.setSelectedItem(targetSourceGroup);
//                String targetPackage = SourceGroupSupport.getPackageForFolder(targetSourceGroup, targetFolder);
//                if (targetPackage != null) {
//                    packageComboBoxEditor.setText(targetPackage);
//                }
//            }
//            if (FileUtil.isParentOf(WebModule.getWebModule(
//                    targetFolder).getDocumentBase(), targetFolder)) {
//                Sources s = (Sources) Templates.getProject(wizard).getLookup().lookup(Sources.class);
//                SourceGroup[] groups = s.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
//                jsfFolder.setText("/"+JSFConfigUtilities.getResourcePath(groups,targetFolder,'/',true));
//            }
//        }
    }

    void store(WizardDescriptor settings) {
        String pkg = getPackage();
        try {
            FileObject fo = getLocationValue().getRootFolder();
            String pkgSlashes = pkg.replace('.', '/');
            FileObject targetFolder = fo.getFileObject(pkgSlashes);
            if (targetFolder == null) {
                targetFolder = FileUtil.createFolder(fo, pkgSlashes);
            }
            Templates.setTargetFolder(settings, targetFolder);
            Templates.setTargetName(wizard, this.getFileName());
        } catch (IOException ex) {
            ExceptionUtils.printStackTrace(ex);
        }
    }

    private void updateSourceGroupPackages() {
        SourceGroup sourceGroup = (SourceGroup) locationComboBox.getSelectedItem();
        ComboBoxModel model = PackageView.createListView(sourceGroup);
        if (model.getSelectedItem() != null && model.getSelectedItem().toString().startsWith("META-INF")
                && model.getSize() > 1) { // NOI18N
            model.setSelectedItem(model.getElementAt(1));
        }
        packageComboBox.setModel(model);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

}
