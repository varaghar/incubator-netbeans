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

package org.netbeans.modules.openide.loaders;

import java.io.IOException;
import java.util.concurrent.Callable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.EditCookie;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.LineCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.PrintCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileLock;
import org.openide.loaders.DataObject;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiDataObject.Entry;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node.Cookie;
import org.openide.text.DataEditorSupport;
import org.openide.windows.CloneableOpenSupport;

/** 
 * Basic editor support.
 *
 * @author Jaroslav Tulach
 */
public final class SimpleES extends DataEditorSupport 
implements OpenCookie, EditCookie, EditorCookie.Observable, 
PrintCookie, CloseCookie, SaveAsCapable, LineCookie {
    /** SaveCookie for this support instance. The cookie is adding/removing 
     * data object's cookie set depending on if modification flag was set/unset. */
    private final SaveCookie saveCookie = new SaveCookieImpl();
    
    private final CookieSet set;
    private final Callable<Pane> factory;
    
    /** Constructor. 
     * @param obj data object to work on
     * @param set set to add/remove save cookie from
     */
    public SimpleES (DataObject obj, MultiDataObject.Entry entry, CookieSet set) {
        this(obj, entry, set, null);
    }

    public SimpleES(DataObject obj, Entry entry, CookieSet set, Callable<Pane> paneFactory) {
        super(obj, obj.getLookup(), new Environment(obj, entry));
        this.set = set;
        this.factory = paneFactory;
    }

    @Override
    protected boolean asynchronousOpen() {
        return true;
    }
    
    @Override
    protected Pane createPane() {
        if (factory != null) {
            try {
                return factory.call();
            } catch (Exception ex) {
                throw new IllegalStateException("Cannot create factory for " + getDataObject(), ex);
            }
        }
        return super.createPane();
    }
    
    
    
    /** 
     * Overrides superclass method. Adds adding of save cookie if the document has been marked modified.
     * @return true if the environment accepted being marked as modified
     *    or false if it has refused and the document should remain unmodified
     */
    protected boolean notifyModified () {
        if (!super.notifyModified()) 
            return false;

        addSaveCookie();

        return true;
    }

    /** Overrides superclass method. Adds removing of save cookie. */
    @Override
    protected void notifyUnmodified () {
        super.notifyUnmodified();
        removeSaveCookie(true);
    }

    /** Helper method. Adds save cookie to the data object. */
    private void addSaveCookie() {
        DataObject obj = getDataObject();

        // Adds save cookie to the data object.
        if(obj.getCookie(SaveCookie.class) == null) {
            set.add(saveCookie);
            obj.setModified(true);
        }
    }

    /** Helper method. Removes save cookie from the data object. */
    private void removeSaveCookie(boolean setModified) {
        DataObject obj = getDataObject();
        
        // Remove save cookie from the data object.
        Cookie cookie = obj.getCookie(SaveCookie.class);

        if(cookie != null && cookie.equals(saveCookie)) {
            set.remove(saveCookie);
            if (setModified) {
                obj.setModified(false);
            }
        }
    }

    
    /** Nested class. Environment for this support. Extends
     * <code>DataEditorSupport.Env</code> abstract class.
     */
    
    private static class Environment extends DataEditorSupport.Env {
        private static final long serialVersionUID = 5451434321155443431L;
        
        private MultiDataObject.Entry entry;
        
        /** Constructor. */
        public Environment(DataObject obj, MultiDataObject.Entry entry) {
            super(obj);
            this.entry = entry;
        }

        
        /** Implements abstract superclass method. */
        protected FileObject getFile() {
            return entry.getFile();
        }

        /** Implements abstract superclass method.*/
        protected FileLock takeLock() throws IOException {
            return entry.takeLock();
        }

        /** 
         * Overrides superclass method.
         * @return text editor support (instance of enclosing class)
         */
        @Override
        public CloneableOpenSupport findCloneableOpenSupport() {
            return getDataObject().getCookie(SimpleES.class);
        }

        @Override
        public String getMimeType() {
            Object n = entry.getFile().getAttribute("javax.script.ScriptEngine");
            if (n instanceof String) {
                String name = (String) n;
                ScriptEngineManager m = new ScriptEngineManager();
                ScriptEngine eng = m.getEngineByName(name);
                if (eng != null) {
                    for (String mime : eng.getFactory().getMimeTypes()) {
                        return mime;
                    }
                }
            }
            return super.getMimeType();
        }
        
        
    } // End of nested Environment class.

    private class SaveCookieImpl implements SaveCookie, Unmodify {
        public SaveCookieImpl() {
        }

        /** Implements <code>SaveCookie</code> interface. */
        @Override
        public void save() throws IOException {
            SimpleES.this.saveDocument();
        }

        @Override
        public String toString() {
            return getDataObject().getPrimaryFile().getNameExt();
        }

        @Override
        public void unmodify() {
            removeSaveCookie(false);
        }
    }

}
