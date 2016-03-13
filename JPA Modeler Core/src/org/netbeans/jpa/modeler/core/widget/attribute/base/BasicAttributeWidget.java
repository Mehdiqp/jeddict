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
package org.netbeans.jpa.modeler.core.widget.attribute.base;

import org.netbeans.jpa.modeler.core.widget.attribute.AttributeWidget;
import org.netbeans.jpa.modeler.properties.PropertiesHandler;
import org.netbeans.jpa.modeler.spec.Basic;
import org.netbeans.jpa.modeler.spec.extend.FetchTypeHandler;
import org.netbeans.jpa.modeler.specification.model.scene.JPAModelerScene;
import org.netbeans.jpa.modeler.specification.model.util.JPAModelerUtil;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;

/**
 *
 * @author Gaurav Gupta
 */
public class BasicAttributeWidget extends BaseAttributeWidget<Basic> {

    public BasicAttributeWidget(JPAModelerScene scene, IPNodeWidget nodeWidget, PinWidgetInfo pinWidgetInfo) {
        super(scene, nodeWidget, pinWidgetInfo);
        this.setImage(JPAModelerUtil.BASIC_ATTRIBUTE);
//        this.addPropertyVisibilityHandler("length", new PropertyVisibilityHandler<String>() {
//            @Override
//            public boolean isVisible() {
//                Basic basicAttribute = (Basic) BasicAttributeWidget.this.getBaseElementSpec();
//                return "String".equals(basicAttribute.getAttributeType());
//            }
//        });
//        this.addPropertyVisibilityHandler("precision", new PropertyVisibilityHandler<String>() {
//            @Override
//            public boolean isVisible() {
//                Basic basicAttribute = (Basic) BasicAttributeWidget.this.getBaseElementSpec();
//                return basicAttribute.isPrecisionAttributeType();
//            }
//        });
//        this.addPropertyVisibilityHandler("scale", new PropertyVisibilityHandler<String>() {
//            @Override
//            public boolean isVisible() {
//                Basic basicAttribute = (Basic) BasicAttributeWidget.this.getBaseElementSpec();
//                return basicAttribute.isScaleAttributeType();
//            }
//        });

    }

    @Override
    public void createPropertySet(ElementPropertySet set) {
        super.createPropertySet(set);
        set.put("BASIC_PROP", PropertiesHandler.getFetchTypeProperty(this.getModelerScene(), (FetchTypeHandler) this.getBaseElementSpec()));

    }

    public static PinWidgetInfo create(String id, String name, IBaseElement baseElement) {
        PinWidgetInfo pinWidgetInfo = AttributeWidget.create(id, name, baseElement);
        pinWidgetInfo.setDocumentId(BasicAttributeWidget.class.getSimpleName());
        return pinWidgetInfo;
    }

}
