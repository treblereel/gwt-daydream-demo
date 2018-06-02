package org.treblereel.gwt.ar.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import org.treblereel.gwt.three4g.resources.ThreeJsTextResource;

/**
 * @author Dmitrii Tikhomirov <chani@me.com>
 * Created by treblereel on 3/26/18.
 */
public interface JavascriptTextResource extends ClientBundle {

    JavascriptTextResource IMPL = GWT.create(JavascriptTextResource.class);

    default void load(String script) {
        ScriptInjector.fromString(script).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }
}
