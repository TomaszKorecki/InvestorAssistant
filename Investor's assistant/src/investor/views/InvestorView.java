/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.views;

import javafx.scene.layout.Pane;

/**
 *
 * @author Tomasz
 */
public abstract class InvestorView {
    protected Pane pane;
    
    public Pane getPane(){
        return pane;
    }
    
    public abstract void InitView();
}
