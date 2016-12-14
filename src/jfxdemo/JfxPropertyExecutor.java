/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxdemo;

import java.util.concurrent.Executor;

/**
 *
 * @author stevenyi
 */
public interface JfxPropertyExecutor extends Executor {
    public boolean runsOnCurrentThread(); 
}
