/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.jellytools.nodes;

import javax.swing.tree.TreePath;
import org.netbeans.jellytools.actions.*;
import org.netbeans.jemmy.operators.JTreeOperator;

/** Node representing unrecognized file */
public class UnrecognizedNode extends Node {

    /** creates new UnrecognizedNode
     * @param treeOperator JTreeOperator tree
     * @param treePath String tree path */
    public UnrecognizedNode(JTreeOperator treeOperator, String treePath) {
       super(treeOperator, treePath);
    }

    /** creates new UnrecognizedNode
     * @param parent parent Node
     * @param treeSubPath String tree path from parent node */    
    public UnrecognizedNode(Node parent, String treeSubPath) {
       super(parent, treeSubPath);
    }

    /** creates new UnrecognizedNode
     * @param treeOperator JTreeOperator tree
     * @param path TreePath */    
    public UnrecognizedNode(JTreeOperator treeOperator, TreePath path) {
       super(treeOperator, path);
    }

    static final OpenAction openAction = new OpenAction();
    static final CutAction cutAction = new CutAction();
    static final CopyAction copyAction = new CopyAction();
    static final PasteAction pasteAction = new PasteAction();
    static final DeleteAction deleteAction = new DeleteAction();
    static final RenameAction renameAction = new RenameAction();
    static final SaveAsTemplateAction saveAsTemplateAction = new SaveAsTemplateAction();
    static final PropertiesAction propertiesAction = new PropertiesAction();

    /** tests popup menu items for presence */    
    public void verifyPopup() {
        verifyPopup(new Action[]{
            openAction,
            cutAction,
            copyAction,
            pasteAction,
            deleteAction,
            renameAction,
            saveAsTemplateAction,
            propertiesAction
        });
    }
    
    /** performs OpenAction with this node */    
    public void open() {
        openAction.perform(this);
    }

    /** performs CutAction with this node */    
    public void cut() {
        cutAction.perform(this);
    }

    /** performs CopyAction with this node */    
    public void copy() {
        copyAction.perform(this);
    }

    /** performs PasteAction with this node */    
    public void paste() {
        pasteAction.perform(this);
    }

    /** performs DeleteAction with this node */    
    public void delete() {
        deleteAction.perform(this);
    }

    /** performs RenameAction with this node */    
    public void rename() {
        renameAction.perform(this);
    }

    /** performs SaveAsTemplateAction with this node */    
    public void saveAsTemplate() {
        saveAsTemplateAction.perform(this);
    }
    
    /** performs PropertiesAction with this node */    
    public void properties() {
        propertiesAction.perform(this);
    }
   
}
