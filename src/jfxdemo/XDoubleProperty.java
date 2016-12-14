/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxdemo;

import com.sun.javafx.binding.ExpressionHelper;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author stevenyi
 */
public class XDoubleProperty extends SimpleDoubleProperty {

    private boolean valid = true;

    Map<JfxPropertyExecutor, ExpressionHelper<Number>> listenerMap = new HashMap<>();

    private void initializeListenerMap(){
        listenerMap.put(JfxPropertyExecutors.INHERIT, null);
        listenerMap.put(JfxPropertyExecutors.JFX, null);
        listenerMap.put(JfxPropertyExecutors.SWING, null);
    }

    public XDoubleProperty(double val){
        super(val);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        addListener(JfxPropertyExecutors.INHERIT, listener);
    }


    public void addListener(JfxPropertyExecutor propThread, InvalidationListener listener) {
        ExpressionHelper<Number> temp = listenerMap.get(propThread);
        temp = ExpressionHelper.addListener(temp, this, listener);
        listenerMap.put(propThread, temp);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        for(Map.Entry<JfxPropertyExecutor, ExpressionHelper<Number>> temp : listenerMap.entrySet()) {
            ExpressionHelper<Number> res = ExpressionHelper.removeListener(temp.getValue(), listener);

            if(res != null) {
                listenerMap.put(temp.getKey(), res);
                break;
            }
        }
    }

    @Override
    public void addListener(ChangeListener<? super Number> listener) {
        addListener(JfxPropertyExecutors.INHERIT, listener);
    }

    public void addListener(JfxPropertyExecutor propThread, ChangeListener<? super Number> listener) {
        ExpressionHelper<Number> temp = listenerMap.get(propThread);
        temp = ExpressionHelper.addListener(temp, this, listener);
        listenerMap.put(propThread, temp);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
        for(Map.Entry<JfxPropertyExecutor, ExpressionHelper<Number>> temp : listenerMap.entrySet()) {
            ExpressionHelper<Number> res = ExpressionHelper.removeListener(temp.getValue(), listener);

            if(res != null) {
                listenerMap.put(temp.getKey(), res);
                break;
            }
        }
    }

    /**
     * Sends notifications to all attached
     * {@link javafx.beans.InvalidationListener InvalidationListeners} and
     * {@link javafx.beans.value.ChangeListener ChangeListeners}.
     *
     * This method is called when the value is changed, either manually by
     * calling {@link #set(double)} or in case of a bound property, if the
     * binding becomes invalid.
     */
    protected void fireValueChangedEvent() {
        for(Map.Entry<JfxPropertyExecutor, ExpressionHelper<Number>> temp : listenerMap.entrySet()) {
            if(temp.getKey().runsOnCurrentThread()) {
                ExpressionHelper.fireValueChangedEvent(temp.getValue());
            } else {
                ExpressionHelper<Number> helper = temp.getValue();
                temp.getKey().execute(() -> 
                    ExpressionHelper.fireValueChangedEvent(helper));
            } 
        }
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    } 
}
