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
package io.github.jeddict.jpa.spec.extend;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import io.github.jeddict.bv.constraints.Constraint;
import static io.github.jeddict.jcode.JPAConstants.ID_FQN;
import static io.github.jeddict.jcode.JPAConstants.JOIN_COLUMNS_FQN;
import static io.github.jeddict.jcode.JPAConstants.JOIN_COLUMN_FQN;
import static io.github.jeddict.jcode.JPAConstants.MAPS_ID_FQN;
import static io.github.jeddict.jcode.util.JavaSourceHelper.getPackageName;
import static io.github.jeddict.jcode.util.JavaSourceHelper.getSimpleClassName;
import io.github.jeddict.jpa.spec.EntityMappings;
import io.github.jeddict.jpa.spec.ForeignKey;
import io.github.jeddict.jpa.spec.IdClass;
import io.github.jeddict.jpa.spec.IdentifiableClass;
import io.github.jeddict.jpa.spec.JoinColumn;
import io.github.jeddict.jpa.spec.PrimaryKeyJoinColumn;
import io.github.jeddict.source.AnnotationExplorer;
import io.github.jeddict.source.JavaSourceParserUtil;
import io.github.jeddict.source.MemberExplorer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 *
 * @author Gaurav Gupta
 */
@XmlType(propOrder = {
    "joinColumn",
    "foreignKey",
    "primaryKeyJoinColumn",
    "primaryKeyForeignKey"
})
public abstract class SingleRelationAttribute extends RelationAttribute implements JoinColumnHandler {

    @XmlElement(name = "join-column")
    private List<JoinColumn> joinColumn;
    @XmlElement(name = "fk")
    protected ForeignKey foreignKey;

    @XmlAttribute(name = "optional")
    protected Boolean optional;
    @XmlAttribute
    private Boolean primaryKey;//id=>primaryKey changed to prevent BaseElement.id field hiding
    @XmlAttribute(name = "maps-id")
    private String mapsId;//used in case of EmbeddedId

    @XmlElement(name = "pk-jc")
    protected List<PrimaryKeyJoinColumn> primaryKeyJoinColumn;
    @XmlElement(name = "pk-fk")
    protected ForeignKey primaryKeyForeignKey;//REVENG PENDING
    

    @Override
    @Deprecated
    public void loadAttribute(EntityMappings entityMappings, Element element, VariableElement variableElement, ExecutableElement getterElement, AnnotationMirror annotationMirror) {
        super.loadAttribute(entityMappings, element, variableElement, getterElement, annotationMirror);
        if (JavaSourceParserUtil.isAnnotatedWith(element, ID_FQN)) {
            this.setPrimaryKey(Boolean.TRUE);
        }

        AnnotationMirror joinColumnsAnnotationMirror = JavaSourceParserUtil.findAnnotation(element, JOIN_COLUMNS_FQN);
        if (joinColumnsAnnotationMirror != null) {
            List joinColumnsAnnot = (List) JavaSourceParserUtil.findAnnotationValue(joinColumnsAnnotationMirror, "value");
            if (joinColumnsAnnot != null) {
                for (Object joinColumnObj : joinColumnsAnnot) {
                    this.getJoinColumn().add(new JoinColumn().load(element, (AnnotationMirror) joinColumnObj));
                }
            }
        } else {
            AnnotationMirror joinColumnAnnotationMirror = JavaSourceParserUtil.findAnnotation(element, JOIN_COLUMN_FQN);
            if (joinColumnAnnotationMirror != null) {
                this.getJoinColumn().add(new JoinColumn().load(element, joinColumnAnnotationMirror));
            }
        }

        this.optional = (Boolean) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "optional");
        
        AnnotationMirror mapsIdAnnotationMirror = JavaSourceParserUtil.findAnnotation(element, MAPS_ID_FQN);
        AnnotationMirror idAnnotationMirror = JavaSourceParserUtil.findAnnotation(element, ID_FQN);

        this.primaryKey = mapsIdAnnotationMirror != null || idAnnotationMirror != null;
        if (mapsIdAnnotationMirror != null) {
            this.mapsId = (String) JavaSourceParserUtil.findAnnotationValue(mapsIdAnnotationMirror, "value");
        }
        
        AnnotationMirror foreignKeyValue = (AnnotationMirror) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "foreignKey");
        if (foreignKeyValue != null) {
            this.foreignKey = ForeignKey.load(element, foreignKeyValue);
        }
        
        DeclaredType declaredType = (DeclaredType) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "targetEntity");
        if (declaredType == null) { 
            if (variableElement.asType() instanceof ErrorType) { //variable => "<any>"
                throw new TypeNotPresentException(this.name + " type not found", null);
            }
            String fqn = variableElement.asType().toString();
            this.targetEntityPackage = getPackageName(fqn);
            this.targetEntity = getSimpleClassName(fqn);
        } else {
            String fqn = declaredType.asElement().asType().toString();
            this.targetEntityPackage = getPackageName(fqn);
            this.targetEntity = getSimpleClassName(fqn);
        }
        
    }

    @Override
    public void loadAttribute(MemberExplorer member, AnnotationExplorer annotation) {
        super.loadAttribute(member, annotation);

        this.getJoinColumn().addAll(JoinColumn.load(member));
        annotation.getBoolean("optional").ifPresent(this::setOptional);

        Optional<AnnotationExplorer> idAnnotationOpt = member.getAnnotation(javax.persistence.Id.class);
        Optional<AnnotationExplorer> mapsIdAnnotationOpt = member.getAnnotation(javax.persistence.MapsId.class);
        this.primaryKey = idAnnotationOpt.isPresent() || mapsIdAnnotationOpt.isPresent();

        if (mapsIdAnnotationOpt.isPresent()) {
            this.mapsId = mapsIdAnnotationOpt.get().getString("value").orElse(EMPTY);
        }

        annotation.getAnnotation("foreignKey").map(ForeignKey::load).ifPresent(this::setForeignKey);

        Optional<ResolvedReferenceTypeDeclaration> targetEntityOpt = annotation.getResolvedClass("targetEntity");
        ResolvedReferenceTypeDeclaration type;
        if (targetEntityOpt.isPresent()) {
            type = targetEntityOpt.get();
        } else {
            type = member.getTypeDeclaration();
        }

        this.targetEntityPackage = type.getPackageName();
        this.targetEntity = type.getClassName();
        this.primaryKeyJoinColumn = PrimaryKeyJoinColumn.load(member);
    }

    /**
     * Gets the value of the joinColumn property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the joinColumn property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJoinColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JoinColumn }
     *
     *
     */
    @Override
    public List<JoinColumn> getJoinColumn() {
        if (joinColumn == null) {
            joinColumn = new ArrayList<>();
        }
        return this.joinColumn;
    }

    @Override
    public void addJoinColumn(JoinColumn joinColumn_In) {
        if (joinColumn == null) {
            joinColumn = new ArrayList<>();
        }
        joinColumn.add(joinColumn_In);
    }

    @Override
    public void removeJoinColumn(JoinColumn joinColumn_In) {
        if (joinColumn == null) {
            joinColumn = new ArrayList<>();
        }
        joinColumn.remove(joinColumn_In);
    }

    /**
     * Gets the value of the foreignKey property.
     *
     * @return possible object is {@link ForeignKey }
     *
     */
    @Override
    public ForeignKey getForeignKey() {
        if(foreignKey==null){
            foreignKey = new ForeignKey();
        }
        return foreignKey;
    }

    /**
     * Sets the value of the foreignKey property.
     *
     * @param value allowed object is {@link ForeignKey }
     *
     */
    @Override
    public void setForeignKey(ForeignKey value) {
        this.foreignKey = value;
    }

    /**
     * Gets the value of the optional property.
     *
     * @return possible object is {@link Boolean }
     *
     */
    public Boolean getOptional() {//isOptional()
        if (optional == null) {
            optional = true;
        }
        return optional;
    }

    /**
     * Sets the value of the optional property.
     *
     * @param value allowed object is {@link Boolean }
     *
     */
    public void setOptional(Boolean value) {
        this.optional = value;
    }

    /**
     * @return the primaryKey
     */
    public Boolean isPrimaryKey() {
        if (primaryKey == null) {
            primaryKey = Boolean.FALSE;
        }
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public IdClass getIdClass(){
        if (isPrimaryKey()) {
            JavaClass javaClass = this.getJavaClass();
            if (javaClass instanceof IdentifiableClass) {
                IdentifiableClass identifiableClass = (IdentifiableClass) javaClass;
                return identifiableClass.getIdClass();
            }
        }
        return null;
    }
    
    
    /**
     * Gets the value of the mapsId property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMapsId() {
        return mapsId;
    }

    /**
     * Sets the value of the mapsId property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMapsId(String value) {
        this.mapsId = value;
    }
    
    @Override
    public Set<Class<? extends Constraint>> getAttributeConstraintsClass() {
        return getConstraintsClass(null);
    }

    /**
     * Gets the value of the primaryKeyJoinColumn property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the primaryKeyJoinColumn property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrimaryKeyJoinColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrimaryKeyJoinColumn }
     *
     *
     */
    public List<PrimaryKeyJoinColumn> getPrimaryKeyJoinColumn() {
        if (primaryKeyJoinColumn == null) {
            primaryKeyJoinColumn = new ArrayList<>();
        }
        return this.primaryKeyJoinColumn;
    }

    /**
     * Gets the value of the primaryKeyForeignKey property.
     *
     * @return possible object is {@link ForeignKey }
     *
     */
    public ForeignKey getPrimaryKeyForeignKey() {
        return primaryKeyForeignKey;
    }

    /**
     * Sets the value of the primaryKeyForeignKey property.
     *
     * @param value allowed object is {@link ForeignKey }
     *
     */
    public void setPrimaryKeyForeignKey(ForeignKey value) {
        this.primaryKeyForeignKey = value;
    }
}
