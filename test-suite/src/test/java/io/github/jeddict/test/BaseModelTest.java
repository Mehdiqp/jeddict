/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
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
package io.github.jeddict.test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinter;
import static io.github.jeddict.jcode.console.Console.BOLD;
import static io.github.jeddict.jcode.console.Console.FG_DARK_BLUE;
import static io.github.jeddict.jcode.console.Console.FG_DARK_CYAN;
import static io.github.jeddict.jcode.console.Console.FG_DARK_GREEN;
import static io.github.jeddict.jcode.console.Console.FG_DARK_MAGENTA;
import static io.github.jeddict.jcode.console.Console.FG_DARK_RED;
import static io.github.jeddict.jcode.console.Console.FG_DARK_YELLOW;
import static io.github.jeddict.jcode.console.Console.FG_RED;
import static io.github.jeddict.jcode.console.Console.wrap;
import static io.github.jeddict.jcode.util.Constants.JAVA_EXT;
import static io.github.jeddict.jcode.util.Constants.JAVA_EXT_SUFFIX;
import io.github.jeddict.jpa.modeler.initializer.JPAModelerUtil;
import static io.github.jeddict.jpa.modeler.initializer.JPAModelerUtil.getEntityMapping;
import io.github.jeddict.jpa.spec.DefaultClass;
import io.github.jeddict.jpa.spec.Embeddable;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.EntityMappings;
import io.github.jeddict.jpa.spec.MappedSuperclass;
import io.github.jeddict.jpa.spec.bean.BeanClass;
import io.github.jeddict.jpa.spec.extend.JavaClass;
import io.github.jeddict.jpa.spec.sync.JavaClassSyncHandler;
import io.github.jeddict.orm.generator.compiler.def.ClassDefSnippet;
import io.github.jeddict.orm.generator.service.BeanClassGenerator;
import io.github.jeddict.orm.generator.service.ClassGenerator;
import io.github.jeddict.orm.generator.service.DefaultClassGenerator;
import io.github.jeddict.orm.generator.service.EmbeddableGenerator;
import io.github.jeddict.orm.generator.service.EmbeddableIdClassGenerator;
import io.github.jeddict.orm.generator.service.EntityGenerator;
import io.github.jeddict.orm.generator.service.MappedSuperClassGenerator;
import io.github.jeddict.reveng.klass.ClassWizardDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.ProgressReporter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;


/**
 * Test floutil - PaUtilitiesdel
 * <br>
 * - Generates the source code
 * <br>
 * - Validate using java parser for compilation issue
 * <br>
 * - Compthor jGauravGupta
 */
public class BaseModelTest {

    protected void testModelerFile(String fileName) throws Exception {
        File file = Utilities.toFile(this.getClass().getResource(fileName).toURI());
        EntityMappings entityMappings = getEntityMapping(file);
        assertNotNull(entityMappings);

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        generateClasses(entityMappings, fileName);
    }

    protected void generateClasses(EntityMappings entityMappings, String fileName) throws Exception {
        String packageName = this.getClass().getPackage().getName();
        for (Entity clazz : entityMappings.getEntity()) {
            testClass(clazz, new EntityGenerator(clazz, packageName), entityMappings, fileName);
        }
        for (MappedSuperclass clazz : entityMappings.getMappedSuperclass()) {
            testClass(clazz, new MappedSuperClassGenerator(clazz, packageName), entityMappings, fileName);
        }
        for (Embeddable clazz : entityMappings.getEmbeddable()) {
            testClass(clazz, new EmbeddableGenerator(clazz, packageName), entityMappings, fileName);
        }
        for (DefaultClass clazz : entityMappings.getDefaultClass()) {
            if (clazz.isEmbeddable()) {
                testClass(clazz, new EmbeddableIdClassGenerator(clazz, packageName), entityMappings, fileName);
            } else {
                testClass(clazz, new DefaultClassGenerator(clazz, packageName), entityMappings, fileName);
            }
        }
        for (BeanClass clazz : entityMappings.getBeanClass()) {
            testClass(clazz, new BeanClassGenerator(clazz, packageName), entityMappings, fileName);
        }
    }

    private void testClass(JavaClass javaClass, ClassGenerator generator, EntityMappings entityMappings, String fileName) throws Exception {
        PrettyPrinter prettyPrinter = new PrettyPrinter();

        String existingSource;
        CompilationUnit existingUnit;

        String newSource = null;
        CompilationUnit newUnit;

        try (InputStream existingSourceStream
                = this.getClass().getResourceAsStream(javaClass.getClazz() + JAVA_EXT_SUFFIX);) {
            existingSource = IOUtils.toString(existingSourceStream, UTF_8);
            existingUnit = JavaParser.parse(existingSource);
            assertNotNull(existingUnit);
            existingSource = prettyPrinter.print(existingUnit);
        }

        if (fileName == null) {
            JavaClassSyncHandler
                    .getInstance(javaClass)
                    .syncExistingSnippet(existingUnit);
            fileName = wrap("Reverse Engineering", FG_DARK_MAGENTA);
        } else {
            fileName = wrap(fileName, FG_DARK_BLUE);
        }

        try {
            ClassDefSnippet classDef = generator.getClassDef();
            classDef.setJaxbSupport(entityMappings.getJaxbSupport());
            newSource = classDef.getSnippet();
            assertNotNull(newSource);
            newUnit = JavaParser.parse(newSource);
            assertNotNull(newUnit);
            newSource = prettyPrinter.print(newUnit);

            try (BufferedReader existingSourceReader = new BufferedReader(new StringReader(existingSource));
                    BufferedReader newSourceReader = new BufferedReader(new StringReader(newSource));) {

                String existingSourceLine;
                String newSourceLine;
                int lineNumber = 0;
                while ((existingSourceLine = existingSourceReader.readLine()) != null && (newSourceLine = newSourceReader.readLine()) != null) {
                    ++lineNumber;
                    assertEquals(existingSourceLine, newSourceLine,
                            '\n'
                            + wrap("Failed : " + javaClass.getFQN() + " [" + fileName + "]", FG_DARK_RED, BOLD)
                            + '\n'
                            + wrap("Line number : " + lineNumber, FG_RED, BOLD)
                            + '\n'
                            + wrap("existingSourceLine : " + existingSourceLine, FG_DARK_RED)
                            + '\n'
                            + wrap("newSourceLine : " + newSourceLine, FG_DARK_RED)
                            + '\n'
                    );
                }
            }

            System.out.println(wrap("Passed : ", FG_DARK_GREEN, BOLD)
                    + wrap(javaClass.getFQN(), FG_DARK_CYAN)
                    + " [" + fileName + "]"
            );
        } catch (ParseProblemException ex) {
            fail(wrap(
                    "Compilation Failed : "
                    + javaClass.getFQN() + " [" + fileName + "]"
                    + '\n'
                    + "---------------------"
                    + '\n'
                    + newSource
                    + '\n'
                    + "---------------------"
                    + ex.getMessage()
                    + "---------------------",
                    FG_RED
            ));
        }
    }

    protected void reverseEngineeringTest(String... classes) {
        try {
            NbTestCase nbtest = new NbTestCase(this.getClass().getSimpleName()) {
            };
            nbtest.clearWorkDir();
            FileObject projectFileObject = FileUtil.toFileObject(nbtest.getWorkDir());
            writeFile(projectFileObject,
                    "pom.xml",
                    "<project xmlns='http://maven.apache.org/POM/4.0.0'>"
                    + "  <modelVersion>4.0.0</modelVersion>"
                    + "  <groupId>grp</groupId>"
                    + "  <artifactId>art</artifactId>"
                    + "  <packaging>jar</packaging>"
                    + "  <version>1.0-SNAPSHOT</version>"
                    + "  <name>Test</name>"
                    + "  <dependencies>\n"
                    + "      <dependency>\n"
                    + "          <groupId>javax.persistence</groupId>\n"
                    + "          <artifactId>javax.persistence-api</artifactId>\n"
                    + "          <version>2.2</version>\n"
                    + "      </dependency>\n"
                    + "  </dependencies>\n"
                    + "  <properties>\n"
                    + "      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n"
                    + "      <maven.compiler.source>1.8</maven.compiler.source>\n"
                    + "      <maven.compiler.target>1.8</maven.compiler.target>\n"
                    + "  </properties>"
                    + "</project>");
            Project project = ProjectManager.getDefault().findProject(projectFileObject);
            FileObject src = FileUtil.createFolder(projectFileObject, "src/main/java");

            String packageName = this.getClass().getPackage().getName();
            Set<String> classFqns = new HashSet<>();
            for (String clazz : classes) {
                FileObject classPackage = src;

                for (String folder : packageName.split("\\.")) {
                    FileObject childFolder = classPackage.getFileObject(folder);
                    if (childFolder == null) {
                        classPackage = classPackage.createFolder(folder);
                    } else {
                        classPackage = childFolder;
                    }
                }

                classFqns.add(packageName + '.' + clazz);
                File classFile = Utilities.toFile(this.getClass().getResource(clazz + JAVA_EXT_SUFFIX).toURI());
                FileObject classFileObject = FileUtil.toFileObject(classFile);
                classFileObject.copy(classPackage, clazz, JAVA_EXT);
            }

            EntityMappings entityMappings = EntityMappings.getNewInstance(JPAModelerUtil.getModelerFileVersion());
            entityMappings.setEntityPackage(packageName);
            boolean includeReference = false;

            ClassWizardDescriptor descriptor = new ClassWizardDescriptor();
            ProgressReporter reporter = (message, index) -> System.out.println(wrap(message, FG_DARK_YELLOW));
            descriptor.loadSource(reporter, entityMappings, src, classFqns, includeReference);
            generateClasses(entityMappings, null);
        } catch (Exception ex) {
            fail(Arrays.toString(classes) + " reverse engineering failed", ex);
        }
    }

    /**
     * Create a new data file with specified initial contents.No file events
     * should be fired until the resulting file is complete (see
     * {@link FileObject#createAndOpen}).
     *
     * @param root a root folder which should already exist
     * @param path a /-separated path to the new file within that root
     * @param body the complete contents of the new file (in UTF-8 encoding)
     * @return
     * @throws java.io.IOException
     */
    public static FileObject writeFile(FileObject root, String path, String body) throws IOException {
        int slash = path.lastIndexOf('/');
        if (slash != -1) {
            root = FileUtil.createFolder(root, path.substring(0, slash));
            path = path.substring(slash + 1);
        }
        FileObject existing = root.getFileObject(path);
        OutputStream os = existing != null ? existing.getOutputStream() : root.createAndOpen(path);
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, UTF_8));
            pw.print(body);
            pw.flush();
        } finally {
            os.close();
        }
        return root.getFileObject(path);
    }
}